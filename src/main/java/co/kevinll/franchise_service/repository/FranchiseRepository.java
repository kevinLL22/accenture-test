package co.kevinll.franchise_service.repository;

import co.kevinll.franchise_service.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {
}
