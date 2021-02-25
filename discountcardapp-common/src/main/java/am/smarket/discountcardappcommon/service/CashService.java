package am.smarket.discountcardappcommon.service;

import am.smarket.discountcardappcommon.model.Cash;
import am.smarket.discountcardappcommon.model.User;

import java.util.List;

public interface CashService {

    void addCash(Cash cash);

    List<Cash> findAllByUserId(int id);

    Double cashSum(int id);

    double allCashSum();

    void deletByUser(User user);

    List<Cash> searchByDateRange(String toDate, String fromDate);

}
