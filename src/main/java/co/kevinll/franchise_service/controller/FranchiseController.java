package co.kevinll.franchise_service.controller;

import co.kevinll.franchise_service.model.Franchise;
import co.kevinll.franchise_service.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> create(@RequestBody Franchise f) {
        return service.create(f);
    }

    @GetMapping("{id}")
    public Mono<Franchise> findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping
    public Flux<Franchise> findAll() {
        return service.findAll();
    }

    @PutMapping("{id}")
    public Mono<Franchise> update(@PathVariable Long id,
                                  @RequestBody Franchise f) {
        return service.update(id, f);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
