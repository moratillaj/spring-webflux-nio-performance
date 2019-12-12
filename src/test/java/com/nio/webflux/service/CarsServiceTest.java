package com.nio.webflux.service;

import com.nio.webflux.exception.CarExistException;
import com.nio.webflux.exception.CarNotExistException;
import com.nio.webflux.model.Car;
import com.nio.webflux.repository.CarsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarsServiceTest {

    @InjectMocks
    private CarsService carsService;

    @Mock
    private CarsRepository carsRepository;

    @Mock
    private Car car1;
    @Mock
    private Car car2;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;

    @Test
    public void shouldFindAll() {
        //Given
        when(carsRepository.findAll()).thenReturn(asList(car1, car2));

        // When && Then
        assertThat(carsService.findAll()).contains(car1, car2);
        verify(carsRepository).findAll();
    }

    @Test
    public void shouldFindByModel() {
        //Given
        String model = "fiesta";
        when(carsRepository.findByModel("fiesta")).thenReturn(asList(car1, car2));

        //When && Then
        assertThat(carsService.findByModel(model)).contains(car1, car2);
        verify(carsRepository).findByModel(model);
    }

    @Test
    public void shouldNotFindByModelAnyCar() {
        //Given
        String model = "unavailableModel";
        when(carsRepository.findByModel(model)).thenReturn(emptyList());

        //When && Then
        assertThat(carsService.findByModel(model)).isEmpty();
        verify(carsRepository).findByModel(model);
    }


    @Test
    public void shouldFindByYear() {
        //Given
        Integer year = 2019;
        when(carsRepository.findByYear(year)).thenReturn(asList(car1, car2));

        //When && Then
        assertThat(carsService.findByYear(year)).contains(car1, car2);
        verify(carsRepository).findByYear(year);
    }

    @Test
    public void shouldNotFindByYearAnyCar() {
        //Given
        Integer year = 2019;
        when(carsRepository.findByYear(year)).thenReturn(emptyList());

        //When && Then
        assertThat(carsService.findByYear(year)).isEmpty();
        verify(carsRepository).findByYear(year);
    }

    @Test
    public void shouldFindByModelAndYear() {
        //Given
        String model = "x1";
        Integer year = 2020;
        when(carsRepository.findByModelAndYear(model, year)).thenReturn(asList(car1, car2));

        //When
        List<Car> found = carsService.findByModelAndYear(model, year);

        //Then
        assertThat(found).contains(car1, car2);
        verify(carsRepository).findByModelAndYear(model, year);
    }

    @Test
    public void shouldNotFindByModelAndYearAnyCar() {
        //Given
        String model = "model";
        Integer year = 2000;
        when(carsRepository.findByModelAndYear(model, year)).thenReturn(emptyList());

        //When && Then
        assertThat(carsService.findByModelAndYear(model, year)).isEmpty();
        verify(carsRepository).findByModelAndYear(model, year);
    }

    @Test
    public void shouldFindById() {
        //Given
        String id = "1234ABC";
        when(carsRepository.findById(id)).thenReturn(of(car1));

        //When && Then
        assertThat(carsService.findById(id)).contains(car1);
        verify(carsRepository).findById(id);
    }

    @Test
    public void shouldNotFindByIdWhenIdNotExist() {
        //Given
        String id = "nonExistentId";
        when(carsRepository.findById(id)).thenReturn(empty());

        //When && Then
        assertThat(carsService.findById(id)).isEmpty();
        verify(carsRepository).findById(id);
    }

    @Test
    public void shouldCreate() {
        //Given
        String carId = "1234ABC";
        when(car1.getId()).thenReturn(carId);
        when(carsRepository.findById(carId)).thenReturn(empty());
        when(carsRepository.save(car1)).thenReturn(car1);


        //When
        Car created = carsService.create(car1);

        //Then
        assertThat(created).isEqualTo(car1);
        InOrder inOrder = inOrder(carsRepository, car1);
        inOrder.verify(car1).getId();
        inOrder.verify(carsRepository).findById(carId);
        inOrder.verify(carsRepository).save(car1);
    }

    @Test
    public void shouldCreateThrowExceptionWhenAlreadyExist() {
        //Given
        String carId = "alreadExistentId";
        when(car1.getId()).thenReturn(carId);
        when(carsRepository.findById(carId)).thenReturn(of(car1));

        //When && Then
        assertThatThrownBy(() -> carsService.create(car1))
                .isInstanceOf(CarExistException.class)
                .hasMessage("Car with id alreadExistentId already exists");
        InOrder inOrder = Mockito.inOrder(carsRepository, car1);
        inOrder.verify(car1).getId();
        inOrder.verify(carsRepository).findById(carId);
        inOrder.verify(carsRepository, never()).save(any(Car.class));
    }

    @Test
    public void shouldUpdate() {
        //Given
        String carId = "1234ABC";
        String newModel = "polo";
        String currentModel = "focus";
        int currentYear = 2019;
        Car currentCar = Car.builder().id(carId).model(currentModel).year(currentYear).build();
        Car carToUpdate = Car.builder().id(carId).model(newModel).year(2020).build();
        when(carsRepository.findById(carId)).thenReturn(of(currentCar));
        when(carsRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //When
        Car updated = carsService.update(carToUpdate);

        //Then
        assertThat(updated.getId()).isEqualTo(carId);
        assertThat(updated.getModel()).isEqualTo(newModel);
        assertThat(updated.getYear()).isEqualTo(currentYear);

        InOrder inOrder = inOrder(carsRepository);
        inOrder.verify(carsRepository).findById(carId);
        inOrder.verify(carsRepository).save(carArgumentCaptor.capture());

        Car saved = carArgumentCaptor.getValue();
        assertThat(saved.getId()).isEqualTo(carToUpdate.getId());
        assertThat(saved.getModel()).isEqualTo(newModel);
        assertThat(saved.getYear()).isEqualTo(currentYear);
    }

    @Test
    public void shouldUpdateThrowExceptionWhenCarNotExist() {
        //Given
        String carId = "1234ABC";
        Car carToUpdate = Car.builder().id(carId).model("newModel").year(2000).build();
        when(carsRepository.findById(carId)).thenReturn(empty());

        //When && Then
        assertThatThrownBy(() -> carsService.update(carToUpdate))
                .isInstanceOf(CarNotExistException.class)
                .hasMessage("Car with id " + carId + " not exist");
        verify(carsRepository).findById(carId);
    }

    @Test
    public void shouldDelete() {
        //Given
        String id = "7777MMM";
        Car car = Car.builder().id(id).model("model").year(2001).build();

        //When
        carsService.delete(car);

        //Then
        verify(carsRepository).deleteById(id);
    }

}