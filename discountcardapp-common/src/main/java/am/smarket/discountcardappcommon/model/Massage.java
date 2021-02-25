package am.smarket.discountcardappcommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "massage")
public class Massage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String subject;
    @Column
    private String text;
    @Column
    private Date date;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="massage_user",
            joinColumns={@JoinColumn(name="massage_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> users = new ArrayList<>();


}
