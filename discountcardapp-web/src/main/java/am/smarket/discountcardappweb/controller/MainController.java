package am.smarket.discountcardappweb.controller;

import am.smarket.discountcardappcommon.dto.CleanHistoryDto;
import am.smarket.discountcardappcommon.model.*;
import am.smarket.discountcardappcommon.service.CashService;
import am.smarket.discountcardappcommon.service.CreditService;
import am.smarket.discountcardappcommon.service.MassageService;
import am.smarket.discountcardappcommon.service.UserService;
import am.smarket.discountcardappweb.security.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @Value("${password}")
    private String password;

    private final UserService userService;
    private final CashService cashService;
    private final CreditService creditService;
    private final MassageService massageService;
    private TableType tableType;

    public MainController(UserService userService, CashService cashService, CreditService creditService, MassageService massageService) {
        this.userService = userService;
        this.cashService = cashService;
        this.creditService = creditService;
        this.massageService = massageService;
    }

    @GetMapping("/")
    public String login(ModelMap modelMap,
                        @AuthenticationPrincipal CurrentUser currentUser) {
        modelMap.addAttribute("user", currentUser);
        return "index";
    }

    @GetMapping("/log")
    public String login(@ModelAttribute Secu user) {
        return "login";
    }

    @PostMapping("/log")
    public String loginFilure(ModelMap modelMap) {
        modelMap.addAttribute("massage", "Սխալ մուտքանունը կամ գաղտնաբառ․");
        return "login";
    }

    @PostMapping("/mainPage")
    public String mainPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser,
                           HttpSession session) {
        modelMap.addAttribute("user", currentUser);
        session.setAttribute("userSession", currentUser);
        return "index";
    }

    @GetMapping("/cleanCard")
    public String cleanCard(ModelMap modelMap,
                            @AuthenticationPrincipal CurrentUser currentUser,
                            @RequestParam(value = "id") int id) {
//        if (currentUser == null) {
//            return "redirect:/log";
//        }
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("cash", cashService.findAllByUserId(id));
        modelMap.addAttribute("cashSum", cashService.cashSum(id));
        modelMap.addAttribute("credit", creditService.findAllByUserId(id));
        Double aDouble = creditService.minusSum(userService.getUserById(id));
        if (aDouble == null) {
            aDouble = 0.0;
        }
        modelMap.addAttribute("minusSum", aDouble);
        modelMap.addAttribute("plusSum", creditService.plusSum(userService.getUserById(id)));
        modelMap.addAttribute("type", tableType);
        return "cleanCardPage";
    }

    @PostMapping("/cleanCardWarning")
    public String cleanCardWarning(ModelMap modelMap,
                                   @RequestParam(value = "id") int id,
                                   @RequestParam(value = "type") TableType tableType) {
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("type", tableType);
        return "cleanCardWarning";
    }

    @PostMapping("/cleanPart")
    public String cleanPart(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser,
                            @ModelAttribute CleanHistoryDto cleanHistoryDto) {
        if (currentUser == null) {
            return "log";
        } else if (!cleanHistoryDto.getPassword().equals(password)) {
            modelMap.addAttribute("massage", "Սխալ գաղտնաբառ");
            modelMap.addAttribute("id", cleanHistoryDto.getUserId());
            modelMap.addAttribute("type", cleanHistoryDto.getTableType());
            return "cleanCardWarning";
        }
        switch (cleanHistoryDto.getTableType()) {
            case USER:
                cleanUser(cleanHistoryDto.getUserId());
                break;
            case CASH:
                cleanCash(cleanHistoryDto.getUserId());
                break;
            case CREDIT:
                cleanCredit(cleanHistoryDto.getUserId());
                break;
            case MASSAGE:
                cleanMassage(cleanHistoryDto.getUserId());
                break;
        }
        modelMap.addAttribute("massage", "Քարտի նշվաշ դաշտերը հաջողությամբ մաքրվել են։");
        modelMap.addAttribute("user", userService.getUserById(cleanHistoryDto.getUserId()));
        modelMap.addAttribute("cashSum", cashService.cashSum(cleanHistoryDto.getUserId()));
        return "userProfile";
    }

    private void cleanUser(int id) {
        User userById = userService.getUserById(id);
        userById.setPercent(0);
        userById.setSum((double) 0);
        userById.setCreditPercent(0);
        userById.setCreditSum(0);
        userService.registerUser(userById);
    }

    private void cleanCash(int id) {
        cashService.deletByUser(userService.getUserById(id));
        Cash cash = new Cash();
        cash.setUser(userService.getUserById(id));
        cash.setSum((double) 0);
        cash.setPercent(0);
        cash.setDiscountSum((double) 0);
        cashService.addCash(cash);
    }

    private void cleanCredit(int id) {
        User userById = userService.getUserById(id);
        creditService.deleteAllByUser(userById);
        Credit credit = new Credit();
        credit.setUser(userById);
        credit.setSum((double) 0);
        credit.setPercent(0);
        creditService.addCredit(credit);
    }

    private void cleanMassage(int id) {
        List<Massage> allByUser = massageService.findAllByUser(userService.getUserById(id));
    }
}
