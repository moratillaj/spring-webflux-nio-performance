package com.nio.webflux.controller;

import com.nio.webflux.exception.CarExistException;
import com.nio.webflux.exception.CarNotExistException;
import com.nio.webflux.model.Car;
import com.nio.webflux.service.CarsService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CarsControllerTest {

    @InjectMocks
    private CarsController carsController;

    @Mock
    private CarsService carsService;

    @Mock
    private Car car1;

    @Mock
    private Car car2;

    @Test
    public void shouldFindAll() {
        //Given
        when(carsService.findAll()).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(null, null))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
        verify(carsService).findAll();
    }

    @Test
    public void shouldFindByModel() {
        //Given
        String modelToSearch = "modelToSearch";
        when(carsService.findByModel(modelToSearch)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(modelToSearch, null))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();

        verify(carsService).findByModel(modelToSearch);
    }

    @Test
    public void shouldFindByYear() {
        //Given
        Integer yearToSearch = 2000;
        when(carsService.findByYear(yearToSearch)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(null, yearToSearch))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
        verify(carsService).findByYear(yearToSearch);
    }

    @Test
    public void shouldFindByModelAndYear() {
        //Given
        String modelToSearch = "modelToSearch";
        Integer yearToSearch = 2000;
        when(carsService.findByModelAndYear(modelToSearch, yearToSearch)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(modelToSearch, yearToSearch))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
    }

    @Test
    public void shouldFindById() {
        //Given
        String carId = "1234ABC";
        when(carsService.findById(carId)).thenReturn(of(car1));

        //When && Then
        StepVerifier.create(carsController.findById(carId))
                .expectNext(car1)
                .verifyComplete();
    }

    @Test
    public void shouldNotFindByIdWhenIdNotExist() {
        //Given
        when(carsService.findById("notExistentId")).thenReturn(empty());

        //When && Then
        StepVerifier.create(carsController.findById("notExistentId"))
                .verifyComplete();
    }

    @Test
    public void shouldCreate() {
        //Given
        when(carsService.create(car1)).thenReturn(car1);

        //When && Then
        StepVerifier.create(carsController.createCar(car1))
                .expectNext(car1)
                .verifyComplete();

        verify(carsService).create(car1);
    }

    @Test
    public void shouldCreateThrowExceptionWhenCarExist() {
        //Given
        when(carsService.create(car1)).thenThrow(new CarExistException("Car already exist"));

        //When && Then
        assertThatThrownBy(() -> carsController.createCar(car1))
        .isInstanceOf(CarExistException.class)
        .hasMessage("Car already exist");

        verify(carsService).create(car1);
    }

    @Test
    public void shouldUpdate() {
        //Given
        when(carsService.update(car1)).thenReturn(car1);

        //When && Then
        StepVerifier.create(carsController.updateCar(car1))
                .expectNext(car1)
                .verifyComplete();

        verify(carsService).update(car1);
    }

    @Test
    public void shouldUpdateThrowExceptionWhenCarNotFound() {
        //Given
        when(carsService.update(car1)).thenThrow(new CarNotExistException("Car doesn't exist"));

        //When && Then
        assertThatThrownBy(() -> carsController.updateCar(car1))
                .isInstanceOf(CarNotExistException.class)
                .hasMessage("Car doesn't exist");

        verify(carsService).update(car1);
    }

}