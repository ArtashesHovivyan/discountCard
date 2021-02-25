package am.smarket.discountcardappweb.controller;

import am.smarket.discountcardappcommon.dto.UserForEmailDto;
import am.smarket.discountcardappcommon.model.Massage;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.service.CashService;
import am.smarket.discountcardappcommon.service.MassageService;
import am.smarket.discountcardappcommon.service.UserService;
import am.smarket.discountcardappweb.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MassageController {

    private MailService mailService;
    private UserService userService;
    private CashService cashService;
    private MassageService massageService;

    public MassageController(MailService mailService, UserService userService, CashService cashService, MassageService massageService) {
        this.mailService = mailService;
        this.userService = userService;
        this.cashService = cashService;
        this.massageService = massageService;
    }

    @GetMapping("/emailPage")
    public String emailInbox(ModelMap modelMap) {
        modelMap.addAttribute("users", creatUserForEmail(userService.findAllUsers()));
        return "emailPage";
    }

    @PostMapping("/sendMassage")
    public String sendMassage(ModelMap modelMap, @ModelAttribute Massage massage) {
        if (massage.getUsers().size() == 0) {
            modelMap.addAttribute("users", creatUserForEmail(userService.findAllUsers()));
            modelMap.addAttribute("massage", "Հաճախորդ ընտրված չէ");
            return "emailPage";
        }
        List<User> users = massage.getUsers();
        String[] emails = new String[users.size()];
        int i = 0;
        for (User user : users) {
            emails[i] = user.getEmail();
            i++;
        }
        mailService.sendSimpleMessage(emails, massage.getSubject(), massage.getText());
        massageService.save(massage);
        modelMap.addAttribute("massage", "Հաղորդագրությունները հաջողությամբ ուղարկվել են");
        return "index";
    }

    public List<UserForEmailDto> creatUserForEmail(List<User> users) {
        List<UserForEmailDto> userForEmailDtoList = new ArrayList<>();
        for (User allUser : users) {
            UserForEmailDto userForEmailDto = new UserForEmailDto();
            userForEmailDto.setId(allUser.getId());
            userForEmailDto.setName(allUser.getName());
            userForEmailDto.setEmail(allUser.getEmail());
            userForEmailDto.setSurname(allUser.getSurname());
            Double aDouble = cashService.cashSum(allUser.getId());
            if (aDouble == null){
                aDouble = 0.0;
            }
            userForEmailDto.setAllSum(aDouble);
            userForEmailDto.setPercent(allUser.getPercent());
            userForEmailDto.setPercentSum(allUser.getSum());
            userForEmailDto.setCreditPercent(allUser.getCreditPercent());
            userForEmailDto.setCreditSum(allUser.getCreditSum());
            userForEmailDtoList.add(userForEmailDto);
        }
        return userForEmailDtoList;
    }

    @GetMapping("/emailOutbox")
    public String emailOutbox(ModelMap modelMap) {
        modelMap.addAttribute("outcoming", massageService.findAll());
        return "emailOutbox";
    }

    @GetMapping("/emailRead")
    public String emailRead(ModelMap modelMap,
                            @RequestParam(value = "id") int id) {
        modelMap.addAttribute("mail", massageService.getOne(id));
        return "emailRead";
    }

    @PostMapping("/deleteEmail")
    public String deleteEmail(ModelMap modelMap,
                              @RequestParam(value = "sms", required = false) int[] sms) {
        if (sms==null) {
            modelMap.addAttribute("massage", "Հաղորդագրություն նշված չէ");
            modelMap.addAttribute("outcoming", massageService.findAll());
            return "emailOutbox";
        }
        for (int sm : sms) {
            massageService.deletEmail(sm);
        }
        modelMap.addAttribute("outcoming", massageService.findAll());
        return "emailOutbox";
    }
}
