package co.kevinll.franchise_service.service;

import co.kevinll.franchise_service.model.Product;
import co.kevinll.franchise_service.repository.BranchRepository;
import co.kevinll.franchise_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final BranchRepository branchRepo;   // para validar la FK

    public Mono<Product> create(Product p) {
        return validateBranch(p.getBranchId())
                .then(productRepo.save(p));
    }

    public Mono<Product> findById(Long id) {
        return productRepo.findById(id);
    }

    public Flux<Product> findAll() {
        return productRepo.findAll();
    }

    public Flux<Product> findByBranch(Long branchId) {
        return productRepo.findByBranchId(branchId);
    }

    public Mono<Product> update(Long id, Product p) {
        return validateBranch(p.getBranchId())
                .then(productRepo.save(new Product(id, p.getBranchId(), p.getName(), p.getStock())));
    }

    public Mono<Void> delete(Long id) {
        return productRepo.deleteById(id);
    }

    /* --- helpers --- */
    private Mono<Void> validateBranch(Long branchId) {
        return branchRepo.existsById(branchId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new IllegalArgumentException("Branch not found: " + branchId)));
    }
}

