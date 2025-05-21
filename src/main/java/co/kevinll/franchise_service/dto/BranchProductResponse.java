package co.kevinll.franchise_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchProductResponse {

    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Integer stock;

}
