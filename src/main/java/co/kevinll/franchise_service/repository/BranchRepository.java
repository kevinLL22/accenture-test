package co.kevinll.franchise_service.repository;

import co.kevinll.franchise_service.model.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long> {

    Flux<Branch> findByFranchiseId(Long franchiseId);
}

