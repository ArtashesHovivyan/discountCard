package am.smarket.discountcardappcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForEmailDto {

    private int id;
    private String name;
    private String surname;
    private String email;
    private double allSum;
    private double percent;
    private double percentSum;
    private double creditPercent;
    private double creditSum;
}
