package co.kevinll.franchise_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("franchise")
public record Franchise (
        @Id Long id,
        String name
){
}
