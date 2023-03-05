package com.project.carguideapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {

    @NotBlank
    private String brand;
    @NotBlank
    private String color;
    @NotBlank
    private String model;
    @NotNull
    private int productionYear;
    @NotBlank
    @Pattern(regexp = "/^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$/ui")
    private String registrationNumber;


}
