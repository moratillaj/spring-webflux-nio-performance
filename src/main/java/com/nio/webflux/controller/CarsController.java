package com.nio.webflux.controller;

import com.nio.webflux.model.Car;
import com.nio.webflux.service.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

@RestController
@RequestMapping(value = "/cars")
public class CarsController {

    @Autowired
    private CarsService carsService;

    @GetMapping
    public Flux<Car> find(@RequestParam(required = false) String model, @RequestParam(required = false) Integer year) {
        if (model == null && year == null) {
            return fromIterable(carsService.findAll());
        } else if (model != null && year != null) {
            return fromIterable(carsService.findByModelAndYear(model, year));
        } else if (year != null) {
            return fromIterable(carsService.findByYear(year));
        }

        return fromIterable(carsService.findByModel(model));
    }

    @GetMapping("/{id}")
    public Mono<Car> findById(@PathVariable String id) {
        return carsService.findById(id)
                .map(Mono::just)
                .orElse(empty());
    }

    @PostMapping
    public Mono<Car> createCar(@RequestBody Car car) {
        return just(carsService.create(car));
    }

    @PutMapping
    public Mono<Car> updateCar(@RequestBody Car car) {
        return just(carsService.update(car));
    }


}
