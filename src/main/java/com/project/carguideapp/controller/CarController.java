package com.project.carguideapp.controller;

import com.project.carguideapp.dto.CarRequestDTO;
import com.project.carguideapp.dto.CarResponseDTO;
import com.project.carguideapp.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> findAll(){
        return new ResponseEntity<>(carService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarResponseDTO> findCarById(@PathVariable Long carId){
        return new ResponseEntity<>(carService.findById(carId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CarResponseDTO> save(@RequestBody CarRequestDTO carRequestDTO){
        return new ResponseEntity<>(carService.save(carRequestDTO),HttpStatus.CREATED);
    }

    @PutMapping("{carId}")
    public ResponseEntity<CarResponseDTO> update(@PathVariable Long carId,@RequestBody CarRequestDTO carRequestDTO){
        return new ResponseEntity<>(carService.update(carId,carRequestDTO),HttpStatus.OK);
    }

    @DeleteMapping("{carId}")
    public ResponseEntity<CarResponseDTO> delete(@PathVariable Long carId){
        return new ResponseEntity<>(carService.deleteCarById(carId),HttpStatus.OK);
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countAll(){
        return new ResponseEntity<>(carService.countAll(),HttpStatus.OK);
    }
    @GetMapping("count/{brand}")
    public ResponseEntity<Long> countByBrand(@PathVariable String brand){
        return new ResponseEntity<>(carService.countByBrand(brand),HttpStatus.OK);
    }
    @GetMapping("/lastLine")
    public ResponseEntity<CarResponseDTO> findLastUpdated(){
        return new ResponseEntity<>(carService.findLastUpdated(),HttpStatus.OK);
    }
}
