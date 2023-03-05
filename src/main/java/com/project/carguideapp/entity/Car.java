package com.project.carguideapp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Table(name = "car")
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String color;
    private String registrationNumber;
    private String model;
    private int productionYear;
    private OffsetDateTime creationTime;
    private OffsetDateTime updateTime;


}
