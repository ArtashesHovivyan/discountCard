package am.smarket.discountcardappweb.security;

import am.smarket.discountcardappcommon.model.Secu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    @Value("${login}")
    private String login;
    @Value("${password}")
    private String password;
    @Value("${name}")
    private String name;
    @Value("${surname}")
    private String surname;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(login)) {
            Secu secu = new Secu(name, surname, login, passwordEncoder.encode(password));
            return new CurrentUser(secu);
        }
        throw new UsernameNotFoundException("User with " + username + " not exists");
    }
}
