package am.smarket.discountcardappcommon.service;

import am.smarket.discountcardappcommon.model.Credit;
import am.smarket.discountcardappcommon.model.User;

import java.util.List;

public interface CreditService {

    void addCredit(Credit credit);

    List<Credit> findAllByUserId(int id);

    Double minusSum(User user);

    double plusSum(User user);

    void deleteAllByUser(User user);
}
