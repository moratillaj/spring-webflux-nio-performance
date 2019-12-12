package com.nio.webflux.controller;


import com.nio.webflux.exception.CarExistException;
import com.nio.webflux.exception.CarNotExistException;
import com.nio.webflux.model.Car;
import com.nio.webflux.service.CarsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsController.class)
public class CarsControllerIntegrationTest {

    @Autowired
    private CarsController carsController;

    @MockBean
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
    public void shouldFindModel() {
        //Given
        String model = "theModel";
        when(carsService.findByModel(model)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(model, null))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
        verify(carsService).findByModel(model);
    }

    @Test
    public void shouldFindYear() {
        //Given
        int year = 2000;
        when(carsService.findByYear(year)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(null, year))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
        verify(carsService).findByYear(year);
    }

    @Test
    public void shouldFindByModelAndYear() {
        //Given
        String model = "theModel";
        int year = 2000;
        when(carsService.findByModelAndYear(model, year)).thenReturn(asList(car1, car2));

        //When && Then
        StepVerifier.create(carsController.find(model, year))
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
        verify(carsService).findByModelAndYear(model, year);
    }

    @Test
    public void shouldFindById() {
        //Given
        String carId = "theId";
        when(carsService.findById(carId)).thenReturn(of(car1));

        //When && Then
        StepVerifier.create(carsController.findById(carId))
                .expectNext(car1)
                .verifyComplete();
        verify(carsService).findById(carId);
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

