package am.smarket.discountcardappcommon.dto;

import am.smarket.discountcardappcommon.model.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CleanHistoryDto {
    private String password;
    private TableType tableType;
    private int userId;

}
