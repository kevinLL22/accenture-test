package co.kevinll.franchise_service.service;

import co.kevinll.franchise_service.model.Branch;
import co.kevinll.franchise_service.repository.BranchRepository;
import co.kevinll.franchise_service.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepo;
    private final FranchiseRepository franchiseRepo;   // para validar FK

    public Mono<Branch> create(Branch b) {
        return validateFranchise(b.getFranchiseId())
                .then(branchRepo.save(b));
    }

    public Mono<Branch> findById(Long id) {
        return branchRepo.findById(id);
    }

    public Flux<Branch> findAll() {
        return branchRepo.findAll();
    }

    public Flux<Branch> findByFranchise(Long franchiseId) {
        return branchRepo.findByFranchiseId(franchiseId);
    }

    public Mono<Branch> update(Long id, Branch b) {
        return validateFranchise(b.getFranchiseId())
                .then(branchRepo.save(new Branch(id, b.getName(), b.getFranchiseId())));
    }

    public Mono<Void> delete(Long id) {
        return branchRepo.deleteById(id);
    }

    /* --- helpers --- */
    private Mono<Void> validateFranchise(Long franchiseId) {
        return franchiseRepo.existsById(franchiseId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new IllegalArgumentException("Franchise not found: " + franchiseId)));
    }
}

