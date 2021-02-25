package am.smarket.discountcardappweb.controller;

import am.smarket.discountcardappcommon.dto.AllCashSumDto;
import am.smarket.discountcardappcommon.model.Cash;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.service.CashService;
import am.smarket.discountcardappcommon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class CashController {

//    NumberFormat nf = NumberFormat.getInstance();
//    private NumberFormat formatter = new DecimalFormat("#0.0");
    private final UserService userService;
    private final CashService cashService;

    public CashController(UserService userService, CashService cashService) {
        this.userService = userService;
        this.cashService = cashService;
    }

    @PostMapping("/addCash")
    public String addCash(ModelMap modelMap, @ModelAttribute Cash cash,
                          @RequestParam(value = "sumPlus") Double sumPlus) {
        if (cash.getSum() == null && sumPlus == null){
            modelMap.addAttribute("massage", "Չի գրանցվել, դաշտերը մուտքագրված չէ");
            modelMap.addAttribute("user", cash.getUser());
            return "userPage";
        }
        if (cash.getSum() == null){cash.setSum((double) 0);}
        if (sumPlus == null){sumPlus = Double.valueOf(0);}
        if (cash.getSum() > 3000) {
            modelMap.addAttribute("cash", cash);
            modelMap.addAttribute("sumPlus", sumPlus);
            return "confirm";
        } else if (cash.getSum() == 0 && sumPlus == 0) {
            modelMap.addAttribute("massage", "Չի գրանցվել, դաշտերը մուտքագրված է  '0'։");
            modelMap.addAttribute("user", cash.getUser());
            return "userPage";
        }
        User user = cash.getUser();
        double percent = (cash.getSum() * user.getPercent()) / 100;
        cash.setPercent(percent + sumPlus);
        cash.setDiscountSum(0);
        cashService.addCash(cash);
        double c = user.getSum() + percent + sumPlus;
        String str = new DecimalFormat("#.0#").format(c);
        str=str.replace(',','.');
//        nf.setMaximumFractionDigits(1);
//        String a = nf.format(c);
        double b = Double.valueOf(str);
        user.setSum(b);
        userService.registerUser(user);
        return "redirect:/";
    }

    @PostMapping("/confirmCash")
    public String confirmCash(@ModelAttribute Cash cash,
                              @RequestParam(value = "sumPlus1") Double sumPlus) {
        User user = cash.getUser();
        double percent = (cash.getSum() * user.getPercent()) / 100;
        cash.setPercent(percent + sumPlus);
        cash.setDiscountSum(0);
        cashService.addCash(cash);
        double c = user.getSum() + percent + sumPlus;
        String str = new DecimalFormat("#.0#").format(c);
        str=str.replace(',','.');
        double b = Double.valueOf(str);
        user.setSum(b);
        userService.registerUser(user);
        return "redirect:/";
    }

    @GetMapping("/takeCash")
    public String takeCash(ModelMap modelMap,
                           @RequestParam(value = "id") int id) {
        modelMap.addAttribute("user", userService.getUserById(id));
        return "cashPage";
    }


    @PostMapping("/takeCash")
    public String takeCash(ModelMap modelMap,
                           @RequestParam(value = "sum") Double sum,
                           @RequestParam(value = "id") int id) {
        if (sum == null){
            modelMap.addAttribute("massage", "Գումարը մուտքագրված չէ");
            modelMap.addAttribute("user", userService.getUserById(id));
            return "cashPage";
        }
        User user = userService.getUserById(id);
        if (user.getSum() < sum) {
            modelMap.addAttribute("massage", "Քարտում առկա գումարը բավարար չէ։");
            return "index";
        } else if (sum == 0) {
            modelMap.addAttribute("massage", "Մուտքագրված է 0։");
            return "index";
        }
        {
            Cash cash = new Cash();
            cash.setSum((double) 0);
            cash.setPercent(0);
            cash.setDiscountSum(sum);
            cash.setUser(user);
            cashService.addCash(cash);
            double c = user.getSum() - sum;
            String str = new DecimalFormat("#.0#").format(c);
            str=str.replace(',','.');
            double b = Double.valueOf(str);
            user.setSum(b);
            userService.registerUser(user);
            return "index";
        }
    }

    @GetMapping("/userPercentSum")
    public String userPercentSum(ModelMap modelMap,
                                 @RequestParam(value = "id") int id) {
        modelMap.addAttribute("allpercent", cashService.findAllByUserId(id));
        modelMap.addAttribute("user", userService.getUserById(id));
        return "userPercentSum";
    }

    @GetMapping("/allSum")
    public String allSum(ModelMap modelMap,
                         @RequestParam(value = "id") int id) {
        modelMap.addAttribute("allpercent", cashService.findAllByUserId(id));
        modelMap.addAttribute("sum", cashService.cashSum(id));
        modelMap.addAttribute("user", userService.getUserById(id));
        return "userSumPage";
    }


    //    Վաճառքի ցանկը տվյալ օրվա համար
    @GetMapping("/cashRange")
    public String todayOrders(ModelMap modelMap) {
        List<Cash> cashList = cashService.searchByDateRange("", "");
        modelMap.addAttribute("allCash", cashList);
        modelMap.addAttribute("allCashSumDto", allCashSum(cashList));
        modelMap.addAttribute("dateRange", new Date().toString());
        modelMap.addAttribute("sum", cashService.allCashSum());
        return "cashRange";
    }

    //    Վաճառքի ցանկը ըստ անսաթվի միջակայքի
    @PostMapping("/cashRange")
    public String orderPageByDateRange(ModelMap modelMap,
                                       @RequestParam(value = "start") String startDate,
                                       @RequestParam(value = "end") String endDate) {
        List<Cash> cashList = cashService.searchByDateRange(startDate, endDate);
        modelMap.addAttribute("allCash", cashList);
        modelMap.addAttribute("allCashSumDto", allCashSum(cashList));
        modelMap.addAttribute("sum", cashService.allCashSum());
        if (endDate.equals("")) {
            endDate = new Date().toString();
            String dateRange = startDate + " : " + endDate;
            modelMap.addAttribute("dateRange", dateRange);
            return "cashRange";
        }
        String dateRange = startDate + " - " + endDate;
        modelMap.addAttribute("dateRange", dateRange);
        return "cashRange";
    }

    public AllCashSumDto allCashSum(List<Cash> cashList) {
        double allSumByDate = 0;
        double allPercentByDate = 0;
        double allDiscountSumByDate = 0;
        for (Cash cash : cashList) {
            allSumByDate = allSumByDate + cash.getSum();
            allPercentByDate = allPercentByDate + cash.getPercent();
            allDiscountSumByDate = allDiscountSumByDate + cash.getDiscountSum();
        }
        AllCashSumDto allCashSumDto = new AllCashSumDto(allSumByDate, allPercentByDate, allDiscountSumByDate);
        return allCashSumDto;
    }
}