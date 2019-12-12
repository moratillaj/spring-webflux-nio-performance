package com.nio.webflux.controller;


import com.nio.webflux.model.Car;
import com.nio.webflux.service.CarsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@RunWith(SpringRunner.class)
@WebFluxTest(CarsController.class)
public class CarsControllerRestIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CarsService carsService;

    @Test
    public void shouldFindByModelAndYear() {
        //Given
        Car car1 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        Car car2 = Car.builder().id("2222ABC").model("fiesta").year(2000).build();
        when(carsService.findByModelAndYear("fiesta", 2000)).thenReturn(asList(car1, car2));

        //When && Then
        webTestClient.get()
                .uri("/cars?model=fiesta&year=2000")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Car.class)
                .contains(car1, car2);

    }

    @Test
    public void shouldFindByModel() {
        //Given
        Car car1 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        Car car2 = Car.builder().id("2222ABC").model("fiesta").year(2000).build();
        when(carsService.findByModel("fiesta")).thenReturn(asList(car1, car2));

        //When && Then
        webTestClient.get()
                .uri("/cars?model=fiesta")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Car.class)
                .contains(car1, car2);

    }

    @Test
    public void shouldFindByYear() {
        //Given
        Car car1 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        Car car2 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        when(carsService.findByYear(2000)).thenReturn(asList(car1, car2));

        //When && Then
        webTestClient.get()
                .uri("/cars?year=2000")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Car.class)
                .contains(car1, car2);

    }

    @Test
    public void shouldFindAll() {
        //Given
        Car car1 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        Car car2 = Car.builder().id("1111ABC").model("fiesta").year(2000).build();
        when(carsService.findAll()).thenReturn(asList(car1, car2));

        //When && Then
        webTestClient.get()
                .uri("/cars")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Car.class)
                .contains(car1, car2);

    }

    @Test
    public void shouldFindById() {
        //Given
        String carId = "1111ABC";
        Car car = Car.builder().id(carId).year(2000).model("theModel").build();
        when(carsService.findById(carId)).thenReturn(of(car));

        //When && Then
        webTestClient.get()
                .uri("/cars/" + carId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Car.class)
                .isEqualTo(car);
    }

    @Test
    public void shouldNotFindById() {
        //Given
        String carId = "idNotFound";
        when(carsService.findById(carId)).thenReturn(empty());

        //When && Then
        webTestClient.get()
                .uri("/cars/" + carId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void shouldUpdate() {
        //Given
        Car car = Car.builder().id("1234ABC").year(2000).model("newModel").build();
        when(carsService.update(car)).thenReturn(car);

        //When && Then
        webTestClient.put()
                .uri("/cars")
                .body(fromObject(car))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Car.class)
                .isEqualTo(car);
    }

    @Test
    public void shouldCreate() {
        //Given
        Car car = Car.builder().id("1111ABC").year(2000).model("model").build();
        when(carsService.create(car)).thenReturn(car);

        //When && Then
        webTestClient.post()
                .uri("/cars")
                .body(fromObject(car))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Car.class)
                .isEqualTo(car);
    }
}
