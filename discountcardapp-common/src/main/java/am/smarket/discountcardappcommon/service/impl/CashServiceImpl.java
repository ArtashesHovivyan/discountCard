package am.smarket.discountcardappcommon.service.impl;

import am.smarket.discountcardappcommon.model.Cash;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.repository.CashReopsitory;
import am.smarket.discountcardappcommon.service.CashService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CashServiceImpl implements CashService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final CashReopsitory cashReopsitory;

    public CashServiceImpl(CashReopsitory cashReopsitory) {
        this.cashReopsitory = cashReopsitory;
    }

    @Override
    public void addCash(Cash cash) {
        cashReopsitory.save(cash);
    }

    @Override
    public List<Cash> findAllByUserId(int id) {
        return cashReopsitory.findAllByUserId(id, Sort.by("date").descending());
    }

    @Override
    public Double cashSum(int id) {
        return cashReopsitory.cashSum(id);
    }

    @Override
    public double allCashSum() {
        return cashReopsitory.allCashSum();
    }

    @Override
    public void deletByUser(User user) {
        cashReopsitory.deleteAllByUser(user);
    }

    @Override
    public List<Cash> searchByDateRange(String toDate, String fromDate) {
        String dateRange = dateRange(toDate, fromDate);
        String[] tmp = dateRange.split(",");
        List<Cash> result = cashReopsitory.searchByDateRange(tmp[0], tmp[1]);
        return result;
    }

    public String dateRange(String startDate, String endDate) {
        String dateRange = startDate + "," + endDate;

        DateTime time = new DateTime().withTimeAtStartOfDay();
        Date start = time.toDate();
        Date end = new Date();
        String a = dateFormat.format(start);
        String b = dateFormat.format(end);
        if (dateRange.equals(",")) {
            dateRange = a + "," + b;
        } else if (startDate.equals("")) {
            dateRange = a + "," + endDate;
        } else if (endDate.equals("")) {
            dateRange = startDate + "," + b;
        }
        return dateRange;
    }
}
