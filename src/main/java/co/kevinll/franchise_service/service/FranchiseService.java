package co.kevinll.franchise_service.service;

import co.kevinll.franchise_service.model.Franchise;
import co.kevinll.franchise_service.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepository repo;

    public Mono<Franchise> create(Franchise f) {
        return repo.save(f);
    }

    public Mono<Franchise> findOne(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
    }

    public Flux<Franchise> findAll() {
        return repo.findAll();
    }

    public Mono<Franchise> update(Long id, Franchise f) {
        return findOne(id)
                .flatMap(existing ->
                        repo.save(new Franchise(id, f.getName()))
                );
    }

    public Mono<Void> delete(Long id) {
        return repo.deleteById(id);
    }

}
