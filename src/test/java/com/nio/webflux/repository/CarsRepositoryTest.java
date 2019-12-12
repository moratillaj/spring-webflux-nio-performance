package com.nio.webflux.repository;

import com.nio.webflux.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CarsRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CarsRepository carsRepository;

    @Test
    public void shouldFindAll() {
        //Given
        Car car = Car.builder().id("1234ABC").model("fiesta").year(2019).build();
        testEntityManager.persist(car);

        //When
        List<Car> cars = carsRepository.findAll();

        //Then
        assertThat(cars).hasSize(1);
        assertThat(cars.get(0))
                .extracting("id", "model", "year")
                .contains("1234ABC", "fiesta", 2019);
    }

    @Test
    public void shouldFindByYear() {
        //Given
        int year = 1999;
        Car car = Car.builder().id("7890HIJ").year(year).model("dacia").build();
        testEntityManager.persist(car);

        //When
        List<Car> found = carsRepository.findByYear(year);

        //Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0))
                .extracting("id", "model", "year")
                .contains("7890HIJ", year, "dacia");
    }

    @Test
    public void shouldNotFindByYear() {
        //Given
        int yearToSearch = 2000;
        Car car = Car.builder().id("1234ABC").year(1999).model("model1").build();
        testEntityManager.persist(car);

        //When && Then
        assertThat(carsRepository.findByYear(yearToSearch)).isEmpty();
    }

    @Test
    public void shouldFindByModel() {
        //Given
        String model = "polo";
        Car car = Car.builder().id("1234ABC").model(model).year(2021).build();
        testEntityManager.persist(car);

        //When
        List<Car> found = carsRepository.findByModel(model);

        //Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0))
                .extracting("id", "model", "year")
                .contains("1234ABC", model, 2021);
    }

    @Test
    public void shouldNotFindByModel() {
        //Given
        String modelToSearch = "modelToSearch";
        Car car = Car.builder().id("1234ABC").model("ford").year(2000).build();
        testEntityManager.persist(car);

        //When && Then
        assertThat(carsRepository.findByModel(modelToSearch)).isEmpty();
    }

    @Test
    public void shouldFindByModelAndYear() {
        //Given
        Car car1 = Car.builder().id("1111ABC").model("focus").year(2000).build();
        Car car2 = Car.builder().id("2222ABC").model("focus").year(2001).build();
        Car car3 = Car.builder().id("3333ABC").model("fiesta").year(2001).build();
        Stream.of(car1, car2, car3).forEach(testEntityManager::persist);

        //When
        List<Car> found = carsRepository.findByModelAndYear("focus", 2000);

        //Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0))
                .extracting("id", "model", "year")
                .contains("1111ABC", "focus", 2000);
    }

    @Test
    public void shouldFindById() {
        //Given
        String id = "9999ZZZ";
        Car car = Car.builder().id(id).model("oneModel").year(2000).build();
        testEntityManager.persist(car);

        //When
        Optional<Car> found = carsRepository.findById(id);

        //Then
        assertThat(found).isPresent();
        assertThat(found.get())
                .extracting("id", "model", "year")
                .contains(id, "oneModel", 2000);
    }

    @Test
    public void shouldNotFindById() {
        //Given
        String idToSearch = "idToSearch";
        Car car = Car.builder().id("1234ABC").model("model").year(2000).build();
        testEntityManager.persist(car);

        //When && Then
        assertThat(carsRepository.findById(idToSearch)).isNotPresent();
    }

    @Test
    public void shouldCreate() {
        //Given
        Car car = Car.builder().id("1234ABC").model("fiesta").year(2018).build();

        //When
        Car created = carsRepository.save(car);

        //Then
        assertThat(created)
                .extracting("id", "model", "year")
                .contains("1234ABC", "fiesta", 2018);
        Car found = testEntityManager.find(Car.class, "1234ABC");
        assertThat(found).extracting("id", "model", "year")
                .contains("1234ABC", "fiesta", 2018);
    }

    @Test
    public void shouldUpdate() {
        //Given
        Car car = Car.builder().id("1234ABC").model("scort").year(2019).build();
        testEntityManager.persist(car);
        Car toUpdate = car.toBuilder().id("1234ABC").model("mondeo").year(2020).build();

        //When
        Car updated = carsRepository.save(toUpdate);

        //Then
        assertThat(updated)
                .extracting("id", "model", "year")
                .contains("1234ABC", "mondeo", 2020);
        Car found = testEntityManager.find(Car.class, "1234ABC");
        assertThat(found).extracting("id", "model", "year")
                .contains("1234ABC", "mondeo", 2020);
        assertThat(carsRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldDelete() {
        //Given
        String id = "1234ABC";
        Car car = Car.builder().id(id).year(2020).model("A5").build();
        testEntityManager.persist(car);

        //When
        carsRepository.deleteById(id);

        //Then
        assertThat(carsRepository.findById(id)).isNotPresent();
    }
}