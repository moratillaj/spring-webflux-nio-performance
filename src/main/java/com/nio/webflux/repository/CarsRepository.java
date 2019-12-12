package com.nio.webflux.repository;

import com.nio.webflux.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarsRepository extends JpaRepository<Car, String> {
    List<Car> findByYear(Integer year);

    List<Car> findByModel(String model);

    List<Car> findByModelAndYear(String model, Integer year);
}
