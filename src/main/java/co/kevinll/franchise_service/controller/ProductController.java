package co.kevinll.franchise_service.controller;

import co.kevinll.franchise_service.model.Product;
import co.kevinll.franchise_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService svc;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@RequestBody Product p) {
        return svc.create(p);
    }

    @GetMapping("{id}")
    public Mono<Product> findOne(@PathVariable Long id) {
        return svc.findById(id);
    }

    @GetMapping
    public Flux<Product> findAll() {
        return svc.findAll();
    }

    @GetMapping("/by-branch/{branchId}")
    public Flux<Product> findByBranch(@PathVariable Long branchId) {
        return svc.findByBranch(branchId);
    }

    @PutMapping("{id}")
    public Mono<Product> update(@PathVariable Long id,
                                @RequestBody Product p) {
        return svc.update(id, p);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return svc.delete(id);
    }
}

