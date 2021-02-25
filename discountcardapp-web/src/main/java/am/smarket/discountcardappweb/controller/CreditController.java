package am.smarket.discountcardappweb.controller;

import am.smarket.discountcardappcommon.model.Credit;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.service.CashService;
import am.smarket.discountcardappcommon.service.CreditService;
import am.smarket.discountcardappcommon.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@Controller
public class CreditController {

    @Value("${password}")
    private String password;

    private final UserService userService;
    private final CreditService creditService;
    private final CashService cashService;

    public CreditController(UserService userService, CreditService creditService, CashService cashService) {
        this.userService = userService;
        this.creditService = creditService;
        this.cashService = cashService;
    }

    @GetMapping("/credit")
    public String login(ModelMap modelMap, @RequestParam(value = "id") int userId) {
        modelMap.addAttribute("user", userService.getUserById(userId));
        return "userCreditPage";
    }

    @PostMapping("/addCredit")
    public String addCredit(ModelMap modelMap, @ModelAttribute Credit credit) {
        if (credit.getSum() == null) {
            modelMap.addAttribute("massage1", "Գումարը մուտքագրված չէ");
            modelMap.addAttribute("user", credit.getUser());
            return "userCreditPage";
        }
        if (credit.getSum() > 1000) {
            modelMap.addAttribute("credit", credit);
            return "confirmCredit";
        } else if (credit.getSum() == 0) {
            modelMap.addAttribute("massage", "Չի գրանցվել, դաշտերը մուտքագրված է 0։");
            return "index";
        }
        User user = credit.getUser();
        if (user.getCreditPercent() == 0.0) {
            creditService.addCredit(credit);
            user.setCreditSum(user.getCreditSum() + credit.getSum());
            userService.registerUser(user);
            return "redirect:/";
        }
        double percent = (credit.getSum() * user.getCreditPercent()) / 100;
        credit.setPercent(percent);
        creditService.addCredit(credit);
        double c = user.getCreditSum() + credit.getSum() + percent;
        String str = new DecimalFormat("#.0#").format(c);
        str = str.replace(',', '.');
        double b = Double.valueOf(str);
        user.setCreditSum(b);
        userService.registerUser(user);
        return "redirect:/";
    }

    @PostMapping("/confirmCredit")
    public String confirmCash(@ModelAttribute Credit credit) {
        User user = credit.getUser();
        if (user.getCreditPercent() == 0.0) {
            creditService.addCredit(credit);
            user.setCreditSum(user.getCreditSum() + credit.getSum());
            userService.registerUser(user);
            return "redirect:/";
        }
        double percent = (credit.getSum() * user.getCreditPercent()) / 100;
        credit.setPercent(percent);
        creditService.addCredit(credit);
        double c = user.getCreditSum() + credit.getSum() + percent;
        String str = new DecimalFormat("#.0#").format(c);
        str = str.replace(',', '.');
        double b = Double.valueOf(str);
        user.setCreditSum(b);
        userService.registerUser(user);
        return "redirect:/";
    }

    @PostMapping("/reduceCredit")
    public String takeCash(ModelMap modelMap,
                           @RequestParam(value = "sum") Double sum,
                           @RequestParam(value = "id") int id) {
        if (sum == null) {
            modelMap.addAttribute("massage1", "Գումարը մուտքագրված չէ");
            modelMap.addAttribute("user", userService.getUserById(id));
            return "userCreditPage";
        }
        User user = userService.getUserById(id);
        if (user.getCreditSum() < sum) {
            modelMap.addAttribute("user", userService.getUserById(id));
            modelMap.addAttribute("massage", "Պարտքի չափը փոքր է մուտքագրված գումարի չափից։");
            return "userCreditPage";
        } else if (sum == 0) {
            modelMap.addAttribute("user", userService.getUserById(id));
            modelMap.addAttribute("massage", "Մուտքագրված է 0։");
            return "userCreditPage";
        }
        {
            Credit credit = new Credit();
            credit.setSum(0 - sum);
            credit.setPercent(0);
            credit.setUser(user);
            creditService.addCredit(credit);
            double c = user.getCreditSum() - sum;
            String str = new DecimalFormat("#.0#").format(c);
            str=str.replace(',','.');
            double b = Double.valueOf(str);
            user.setCreditSum(b);
            userService.registerUser(user);
            return "index";
        }
    }


    @GetMapping("/creditSum")
    public String creditSum(ModelMap modelMap,
                            @RequestParam(value = "id") int id) {
        modelMap.addAttribute("creditList", creditService.findAllByUserId(id));
        modelMap.addAttribute("user", userService.getUserById(id));
        return "userCreditList";
    }

    @GetMapping("/creditList")
    public String creditList(ModelMap modelMap) {
        modelMap.addAttribute("creditList", userService.findAllCredits());
        modelMap.addAttribute("allCreditSum", userService.allCreditSum());
        return "allCreditsList";
    }
}