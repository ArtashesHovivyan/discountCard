package am.smarket.discountcardappcommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Secu {

    private String name;

    private String surname;

    private String login;

    private String password;

}
