package com.project.carguideapp.service;

import com.project.carguideapp.dto.CarRequestDTO;
import com.project.carguideapp.dto.CarResponseDTO;
import com.project.carguideapp.entity.Car;
import com.project.carguideapp.exception.NotFoundException;
import com.project.carguideapp.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CarService {
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CarService(CarRepository carRepository, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
    }

    public List<CarResponseDTO> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(it -> modelMapper.map(it,CarResponseDTO.class))
                .collect(Collectors.toList());
    }

    public CarResponseDTO findById(Long id){
        Car car = carRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found for this id :: "+id));
        return modelMapper.map(car,CarResponseDTO.class);
    }

    @Transactional
    public CarResponseDTO save(CarRequestDTO carRequestDTO) {
        if(carRepository.findByRegistrationNumber(carRequestDTO.getRegistrationNumber())!=null){
            throw new RuntimeException("AlreadyExist");
        }
        Car car = modelMapper.map(carRequestDTO,Car.class);
        car.setCreationTime(OffsetDateTime.now());
        car.setUpdateTime(OffsetDateTime.now());
        Car savedCar = carRepository.save(car);
        return modelMapper.map(savedCar,CarResponseDTO.class);

    }

    @Transactional
    public CarResponseDTO deleteCarById(Long id) {
        Car car = carRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found for this id :: " + id));
        carRepository.deleteById(id);
        return modelMapper.map(car,CarResponseDTO.class);
    }

    @Transactional
    public CarResponseDTO update(Long id, CarRequestDTO carRequestDTO) {
        Car car = carRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found for this id :: " + id));
        Car carForSave=modelMapper.map(carRequestDTO,Car.class);

                carForSave.setId(id);
        carForSave.setCreationTime(car.getCreationTime());
        carForSave.setUpdateTime(OffsetDateTime.now());
        Car savedCar = carRepository.save(carForSave);
        return modelMapper.map(savedCar,CarResponseDTO.class);
    }

    public Long countAll(){
        return carRepository.countAll();
    }

    public Long countByBrand(String brand){
        return carRepository.countByBrand(brand);
    }
    public CarResponseDTO findLastUpdated(){
        Car car = carRepository.findLastUpdated();
        return modelMapper.map(car,CarResponseDTO.class);
    }
}
