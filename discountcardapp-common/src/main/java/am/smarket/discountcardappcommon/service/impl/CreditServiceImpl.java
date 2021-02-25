package am.smarket.discountcardappcommon.service.impl;

import am.smarket.discountcardappcommon.model.Credit;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.repository.CreditReopsitory;
import am.smarket.discountcardappcommon.service.CreditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    private final CreditReopsitory creditReopsitory;

    public CreditServiceImpl(CreditReopsitory creditReopsitory) {
        this.creditReopsitory = creditReopsitory;
    }

    @Override
    public void addCredit(Credit credit) {
        creditReopsitory.save(credit);
    }

    @Override
    public List<Credit> findAllByUserId(int id) {
        return creditReopsitory.findAllByUserId(id);
    }

    @Override
    public Double minusSum(User user) {
        return creditReopsitory.minusSum(user);
    }

    @Override
    public double plusSum(User user) {
        return creditReopsitory.plusSum(user);
    }

    @Override
    public void deleteAllByUser(User user) {
        creditReopsitory.deleteAllByUser(user);
    }


}
