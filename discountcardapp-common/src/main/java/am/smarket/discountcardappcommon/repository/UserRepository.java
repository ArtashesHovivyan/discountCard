package am.smarket.discountcardappcommon.repository;

import am.smarket.discountcardappcommon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByCode(String code);

    @Query(value = "SELECT u FROM User u WHERE u.email =:search OR u.phone =:search OR u.name =:search OR u.surname =:search OR u.code =:search")
    List<User> findByUserParam(@Param("search") String search);


    @Query(value = "SELECT u FROM User u WHERE u.creditSum > 0")
    List<User> findAllSredits();

    @Query(value = "SELECT SUM(`credit_sum`) FROM user",nativeQuery = true)
    double allCreditSum ();
}
