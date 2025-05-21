package co.kevinll.franchise_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private Long id;

    @Column("branch_id")
    private Long branchId;

    private String name;

    private Integer stock;
}

