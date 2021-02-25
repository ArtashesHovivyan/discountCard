package am.smarket.discountcardappweb.security;

import am.smarket.discountcardappcommon.model.Secu;
import lombok.Data;
import org.springframework.security.core.authority.AuthorityUtils;


@Data
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private Secu user;

    public CurrentUser(Secu user) {
        super(user.getLogin(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getLogin()));
        this.user = user;
    }

}
