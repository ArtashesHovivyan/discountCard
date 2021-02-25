package am.smarket.discountcardappcommon.repository;

import am.smarket.discountcardappcommon.model.Cash;
import am.smarket.discountcardappcommon.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CashReopsitory extends JpaRepository <Cash, Integer> {

    List<Cash> findAllByUserId(int id, Sort sort);

    @Query(value = "SELECT SUM(`sum`) FROM cash WHERE user_id=:id",nativeQuery = true)
    Double cashSum (@Param("id") int id);

    @Query(value = "SELECT SUM(`percent`) FROM cash WHERE user_id=:id",nativeQuery = true)
    double percentSum (@Param("id") int id);

    @Query(value = "SELECT * FROM cash WHERE date BETWEEN :toDate AND :fromDate + interval 1 day", nativeQuery = true)
    List<Cash> searchByDateRange(
            @Param("toDate") String toDateTime,
            @Param("fromDate") String fromDateTime);

    @Query(value = "SELECT SUM(`sum`) FROM cash",nativeQuery = true)
    double allCashSum ();

    @Transactional
    void deleteAllByUser(User user);
}
