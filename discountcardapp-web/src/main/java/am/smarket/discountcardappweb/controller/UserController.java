package am.smarket.discountcardappweb.controller;

import am.smarket.discountcardappcommon.model.Cash;
import am.smarket.discountcardappcommon.model.Credit;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.service.CashService;
import am.smarket.discountcardappcommon.service.CreditService;
import am.smarket.discountcardappcommon.service.UserService;
import am.smarket.discountcardappweb.security.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Value("${password}")
    private String password;

    private final UserService userService;
    private final CashService cashService;
    private final CreditService creditService;

    public UserController(UserService userService, CashService cashService, CreditService creditService) {
        this.userService = userService;
        this.cashService = cashService;
        this.creditService = creditService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(ModelMap modelMap,
                               @ModelAttribute User user) {
        if (user.getName().equals("") || user.getSurname().equals("") || user.getPhone().equals("") || user.getCode().equals("")){
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "* - ով նշված դաշտերը պարտադիր պետք է լրացնել");
            return "register";
        }
        if (user.getPercent() < 0 || user.getPercent() > 100 || user.getCreditPercent() < 0 || user.getCreditPercent() > 100) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "Տոկոսի չափ տողերից մեկը լրացված է սխալ");
            return "register";
        }
        if (userService.findByPhone(user.getPhone()) != null) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "Տվյալ հեռախոսահամարով բաժանորդ արդեն գրանցված է");
            return "register";
        } else if (userService.findByCode(user.getCode()) != null) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "Տվյալ համարով քարդ արդեն գրանցված է");
            return "register";
        } else {
            user.setSum(0);
            user.setCreditSum(0);
            userService.registerUser(user);
            creatCash(user);
            creatCredit(user);
            modelMap.addAttribute("massage", "Քարտը գրանցված է");
            return "index";
        }
    }

    @PostMapping("/searchCard")
    public String searchCard(ModelMap modelMap,
                             @RequestParam(value = "param") String param) {
        if (userService.findByUserParam(param).size() == 0) {
            modelMap.addAttribute("massage", "Սխալ արդյունք։ Տվյալ քարտը գտնված չէ");
            return "index";
        } else if (userService.findByUserParam(param).size() > 1) {
            userService.findByUserParam(param);
            modelMap.addAttribute("users", userService.findByUserParam(param));
            return "findUser";
        }
        modelMap.addAttribute("user", userService.findByUserParam(param).get(0));
        return "userPage";
    }

    @GetMapping("/allUsers")
    public String allUsers(ModelMap modelMap) {
        modelMap.addAttribute("allUsers", userService.findAllUsers());
        return "allUsers";
    }

    @GetMapping("/userProfile")
    public String userProfile(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser,
                              @RequestParam(value = "id") int id) {
        modelMap.addAttribute("currentUser", currentUser);
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("cashSum", cashService.cashSum(id));
        return "userProfile";
    }

    @GetMapping("/deleteUserById")
    public String deleteUserById(ModelMap modelMap,
                                 @RequestParam(value = "id") int id) {
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("cashSum", cashService.cashSum(id));
        return "delWarning";
    }
    @PostMapping("/deleteUser")
    public String takeCash(ModelMap modelMap,
                           @RequestParam(value = "id") int id,
                           @RequestParam(value = "password") String pass) {
        if (pass.equals(password)){
            userService.deleteUser(id);
            modelMap.addAttribute("massage", "Քարտը հաջողությամբ ջնջվել է");
            return "index";
        }
        modelMap.addAttribute("massage", "Սխալ գաղտնաբառ, Քարտը չի ջնջվել։");
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("cashSum", cashService.cashSum(id));
        return "delWarning";
    }

    @GetMapping("/changeUser")
    public String changeUser(ModelMap modelMap,
                                 @RequestParam(value = "userId") int id) {
        modelMap.addAttribute("user", userService.getUserById(id));
        return "changeUser";
    }

    @PostMapping("/changeUser")
    public String changeUser(ModelMap modelMap,
                             @ModelAttribute User user,
                             @RequestParam(value = "password") String pass) {
        if (!pass.equals(password)){
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "Գաղտնաբառը լրացված է սխալ․ քարտի տվյալները փոփոխված չեն");
            return "changeUser";
        }else if (user.getName().equals("") || user.getSurname().equals("") || user.getPhone().equals("")|| user.getCode().equals("")) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("massage", "Դասշտերը լրացված է թերի");
            return "changeUser";
        }
        user.setSum(userService.getUserById(user.getId()).getSum());
        user.setCreditSum(userService.getUserById(user.getId()).getCreditSum());
        user.setDate(userService.getUserById(user.getId()).getDate());
        userService.registerUser(user);
        return "redirect:/userProfile?id=" + user.getId();
    }

    private Cash creatCash(User user){
        Cash cash = new Cash();
        cash.setSum((double) 0);
        cash.setPercent(0);
        cash.setDiscountSum((double) 0);
        cash.setUser(user);
        cashService.addCash(cash);
        return cash;
    }
    private Credit creatCredit(User user){
        Credit credit = new Credit();
        credit.setSum((double) 0);
        credit.setPercent(0);
        credit.setUser(user);
        creditService.addCredit(credit);
        return credit;
    }
}