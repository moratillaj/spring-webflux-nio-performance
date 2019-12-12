package com.nio.webflux.service;

import com.nio.webflux.exception.CarExistException;
import com.nio.webflux.exception.CarNotExistException;
import com.nio.webflux.model.Car;
import com.nio.webflux.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@Service
public class CarsService {

    @Autowired
    private CarsRepository carsRepository;

    public List<Car> findAll() {
        return carsRepository.findAll();
    }

    public List<Car> findByModel(final String model) {
        return carsRepository.findByModel(model);
    }

    public List<Car> findByYear(final Integer year) {
        return carsRepository.findByYear(year);
    }

    public List<Car> findByModelAndYear(String model, Integer year) {
        return carsRepository.findByModelAndYear(model, year);
    }

    public Optional<Car> findById(final String id) {
        return carsRepository.findById(id);
    }

    public Car create(final Car car) {
        return of(car)
                .map(Car::getId)
                .map(carsRepository::findById)
                .filter(this::isNotPresent)
                .map(empty -> carsRepository.save(car))
                .orElseThrow(() -> new CarExistException("Car with id " + car.getId() + " already exists"));

    }

    private boolean isNotPresent(Optional<Car> car) {
        return !car.isPresent();
    }

    public Car update(final Car car) {
        return of(car)
                .map(Car::getId)
                .flatMap(carsRepository::findById)
                .map(currentCar -> mergeCarInfo(currentCar, car))
                .map(carsRepository::save)
                .orElseThrow(() -> new CarNotExistException("Car with id " + car.getId() + " not exist"));
    }

    private Car mergeCarInfo(Car currentCar, Car car) {
        return currentCar.toBuilder().model(car.getModel()).build();
    }

    public void delete(final Car car) {
        of(car)
                .map(Car::getId)
                .ifPresent(carsRepository::deleteById);
    }
}
