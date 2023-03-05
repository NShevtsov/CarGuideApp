package com.project.carguideapp.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CarResponseDTO {

    private Long id;

    private String brand;
    private String model;
    private String color;
    private int productionYear;
    private OffsetDateTime creationTime;
    private OffsetDateTime updateTime;
    private String registrationNumber;

}
