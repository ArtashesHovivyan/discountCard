package am.smarket.discountcardappcommon.repository;

        import am.smarket.discountcardappcommon.model.Massage;
        import am.smarket.discountcardappcommon.model.User;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.List;

public interface MassageRepository extends JpaRepository<Massage, Integer> {

    List<Massage> findAllByUsers(User user);

}
