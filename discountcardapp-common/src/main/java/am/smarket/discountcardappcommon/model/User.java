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
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String code;
    @Column
    private double percent;
    @Column(name = "credit_percent")
    private double creditPercent;
    @Column
    private double sum;
    @Column(name = "credit_sum")
    private double creditSum;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date;

}
