package co.kevinll.franchise_service.repository;

import co.kevinll.franchise_service.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Flux<Product> findByBranchId(Long branchId);
}

