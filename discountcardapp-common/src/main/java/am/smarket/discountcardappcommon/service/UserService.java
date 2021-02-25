package am.smarket.discountcardappcommon.service;

import am.smarket.discountcardappcommon.model.User;

import java.util.List;

public interface UserService {

    void registerUser(User user);

    List<User> findAllUsers();

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByCode(String code);

    List<User> findByUserParam(String search);

    User getUserById(int id);

    void deleteUser(int id);

    List<User> findAllCredits();

    double allCreditSum();

}
