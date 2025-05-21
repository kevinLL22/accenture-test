package co.kevinll.franchise_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    private Long id;

    private String name;

    @Column("franchise_id")
    private Long franchiseId;
}

