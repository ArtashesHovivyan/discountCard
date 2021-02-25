package am.smarket.discountcardappcommon.repository;

import am.smarket.discountcardappcommon.model.Credit;
import am.smarket.discountcardappcommon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CreditReopsitory extends JpaRepository<Credit, Integer> {

    List<Credit> findAllByUserId(int id);

    @Query(value = "SELECT SUM(`sum`) FROM credit WHERE user_id =:user and sum <= 0", nativeQuery = true)
    Double minusSum(@Param(value = "user") User user);

    @Query(value = "SELECT SUM(`sum`) FROM credit WHERE user_id =:user and sum >= 0", nativeQuery = true)
    double plusSum(@Param(value = "user") User user);

    @Transactional
    void deleteAllByUser(User user);
}
