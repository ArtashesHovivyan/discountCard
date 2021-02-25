package am.smarket.discountcardappapi.endpoint;

import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest/users")
public class UserEndpoint {


    private UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> users() {
        List<User> allUsers = userService.findAllUsers();
        return allUsers;
    }
}
