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
@Table(name = "cash")
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private Double sum;
    @Column
    private double percent;
    @Column
    private double discountSum;
    @ManyToOne
    private User user;
    @Column
    private Date date;

}
