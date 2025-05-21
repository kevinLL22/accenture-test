package co.kevinll.franchise_service.controller;

import co.kevinll.franchise_service.model.Branch;
import co.kevinll.franchise_service.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Branch> create(@RequestBody Branch branch) {
        return service.create(branch);
    }

    @GetMapping("{id}")
    public Mono<Branch> findOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<Branch> findAll() {
        return service.findAll();
    }

    @PutMapping("{id}")
    public Mono<Branch> update(@PathVariable Long id,
                               @RequestBody Branch branch) {
        return service.update(id, branch);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    // -------- consulta por franquicia --------
    @GetMapping("/by-franchise/{franchiseId}")
    public Flux<Branch> findByFranchise(@PathVariable Long franchiseId) {
        return service.findByFranchise(franchiseId);
    }
}

