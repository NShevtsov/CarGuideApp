package com.project.carguideapp.service;

import com.project.carguideapp.dto.CarRequestDTO;
import com.project.carguideapp.dto.CarResponseDTO;
import com.project.carguideapp.entity.Car;
import com.project.carguideapp.exception.NotFoundException;
import com.project.carguideapp.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CarService testCarService;


    @BeforeEach
    void setUp() {
        testCarService = new CarService(carRepository, modelMapper);
    }

    @Test
    void findAll() {
        Car car = Mockito.mock(Car.class);
        CarResponseDTO carResponseDto = mock(CarResponseDTO.class);
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carRepository.findAll()).thenReturn(carList);

        testCarService.findAll();

        verify(carRepository).findAll();
    }

    @Test
    void shouldFindById() {
        //given
        Long existingId = 1L;
        Optional<Car> existingCarEntity = Optional.of(mock(Car.class));
        CarResponseDTO carResponseDto = mock(CarResponseDTO.class);

        when(carRepository.findById(existingId)).thenReturn(existingCarEntity);
        //when
        testCarService.findById(existingId);
        //then
        verify(carRepository).findById(existingId);
    }


    @Test
    void shouldNotFindByIdAndThrowException() {

        //given
        Long nonExistentId = 0L;
        Optional<Car> nonExistentGameEntity = Optional.empty();

        when(carRepository.findById(nonExistentId)).thenReturn(nonExistentGameEntity);

        //then
        assertThrows(
                NotFoundException.class,
                () -> testCarService.findById(nonExistentId));
        verify(carRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void shouldSave() {
        //Given
        Car car = Car.builder()
                .id(null)
                .brand("BMW")
                .color("blue")
                .model("525")
                .productionYear(1984)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();
        Car returnedCar = Car.builder()
                .id(1L)
                .brand("BMW")
                .color("blue")
                .model("525")
                .productionYear(1984)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();
        CarResponseDTO carResponseDto = modelMapper.map(returnedCar, CarResponseDTO.class);
        CarRequestDTO carRequestDto = modelMapper.map(car, CarRequestDTO.class);

        when(carRepository.save(any(Car.class))).thenReturn(returnedCar);
        CarResponseDTO saved = testCarService.save(carRequestDto);
        //Then
        assertThat(saved).usingRecursiveComparison()
                .ignoringFieldsOfTypes(OffsetDateTime.class)
                .isEqualTo(carResponseDto);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void shouldEitherDeleteByIdOrThrowException() {
        //given
        Long nonExistentId = 0L;
        Optional<Car> nonExistentCarEntity = Optional.empty();

        Long existingId = 1L;
        Optional<Car> existingCarEntity = Optional.of(mock(Car.class));

        when(carRepository.findById(nonExistentId)).thenReturn(nonExistentCarEntity);
        when(carRepository.findById(existingId)).thenReturn(existingCarEntity);
        doNothing().when(carRepository).deleteById(existingId);
        //when
        assertThrows(
                NotFoundException.class,
                () -> testCarService.deleteCarById(nonExistentId));

        testCarService.deleteCarById(existingId);
        //then
        verify(carRepository, times(1)).findById(existingId);
        verify(carRepository, times(1)).findById(nonExistentId);
        verify(carRepository, times(1)).deleteById(existingId);
        verify(carRepository, times(0)).deleteById(nonExistentId);
    }

    @Test
    void shouldUpdate() {
        //given


        Long id = 1L;
        Car foundCar = new Car();
        String brand = "BMW";
        String color = "blue";
        String model = "525";
        int productionYear = 1984;
        CarRequestDTO carRequestDTO = CarRequestDTO.builder()
                .brand(brand)
                .color(color)
                .model(model)
                .productionYear(productionYear)
                .build();
        OffsetDateTime creationTime = OffsetDateTime.of(2022, 7, 14,
                12, 45, 41, 1222, ZoneOffset.UTC);

        Car returnedCar = Car.builder()
                .id(id)
                .brand(brand)
                .color(color)
                .model(model)
                .productionYear(productionYear)
                .creationTime(creationTime)
                .updateTime(creationTime)
                .build();
        CarResponseDTO expectedDTO = modelMapper.map(returnedCar, CarResponseDTO.class);
        Car carForSave =
                Car.builder()
                        .id(id)
                        .brand(brand)
                        .color(color)
                        .model(model)
                        .productionYear(productionYear)
                        .creationTime(null)
                        .updateTime(any())
                        .build();

        when(carRepository.save(carForSave))
                .thenReturn(returnedCar);
        when(carRepository.findById(id)).thenReturn(Optional.of(new Car()));


        //when
        CarResponseDTO actualCar = testCarService.update(id, carRequestDTO);
        //then
//        assertEquals(expectedDTO,actualCar);
        assertThat(expectedDTO).usingRecursiveComparison().ignoringFieldsOfTypes(OffsetDateTime.class)
                .isEqualTo(actualCar);
        verify(carRepository, times(1)).findById(id);
        verify(carRepository, times(1)).save(carForSave);

    }
}