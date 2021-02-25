package am.smarket.discountcardappcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllCashSumDto {
    private double AllSumByDate;
    private double AllPercentByDate;
    private double AllDiscountSumByDate;
}
