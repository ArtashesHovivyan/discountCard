package am.smarket.discountcardappcommon.service.impl;

import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.repository.UserRepository;
import am.smarket.discountcardappcommon.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User findByCode(String code) {
        return userRepository.findByCode(code);
    }

    @Override
    public List<User> findByUserParam(String search) {
        return userRepository.findByUserParam(search);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllCredits() {
        return userRepository.findAllSredits();
    }

    @Override
    public double allCreditSum() {
        return userRepository.allCreditSum();
    }

}
