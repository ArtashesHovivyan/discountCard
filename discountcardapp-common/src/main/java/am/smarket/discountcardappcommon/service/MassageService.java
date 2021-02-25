package am.smarket.discountcardappcommon.service;

import am.smarket.discountcardappcommon.model.Massage;
import am.smarket.discountcardappcommon.model.User;

import java.util.List;

public interface MassageService {

    void save(Massage massage);

    List<Massage> findAll();

    Massage getOne(int id);

    List<Massage> findAllByUser(User user);

    void deletEmail(int id);
}
