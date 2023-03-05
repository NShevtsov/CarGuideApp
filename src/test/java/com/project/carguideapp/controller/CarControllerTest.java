package com.project.carguideapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.carguideapp.dto.CarRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc
@SpringBootTest
class CarControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should return DTO of saved entity")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void saveThenReturnSavedDTO() throws Exception {
        //given
        CarRequestDTO requestBody = CarRequestDTO.builder()
                .brand("Volvo")
                .model("525i")
                .color("Red")
                .productionYear(1333)
                .build();
        OffsetDateTime dateTime = OffsetDateTime.now();
        //when then
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(12)))
                .andExpect(jsonPath("$.brand", equalTo("Volvo")))
                .andExpect(jsonPath("$.model", equalTo("525i")))
                .andExpect(jsonPath("$.color", equalTo("Red")))
                .andExpect(jsonPath("$.productionYear", equalTo(1333)))
                .andExpect(jsonPath("$.creationTime", notNullValue()))
                .andExpect(jsonPath("$.updateTime", notNullValue()));
    }

    @Test
    @DisplayName("Should return list of DTOs with the size 11")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllThenReturnListOfDTO() throws Exception {
        //when then
        mockMvc.perform(get("/api/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[2].id", equalTo(3)))
                .andExpect(jsonPath("$[3].id", equalTo(4)))
                .andExpect(jsonPath("$[4].id", equalTo(5)))
                .andExpect(jsonPath("$[5].id", equalTo(6)))
                .andExpect(jsonPath("$[6].id", equalTo(7)))
                .andExpect(jsonPath("$[7].id", equalTo(8)))
                .andExpect(jsonPath("$[8].id", equalTo(9)))
                .andExpect(jsonPath("$[9].id", equalTo(10)))
                .andExpect(jsonPath("$[10].id", equalTo(11)));
    }

    @Test
    @DisplayName("Should return DTO of found entity")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByValidIdThenReturnCarResponseDTO() throws Exception {
        //when then
        mockMvc.perform(get("/api/cars/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.brand", equalToIgnoringCase("kia")))
                .andExpect(jsonPath("$.model", equalToIgnoringCase("rio")))
                .andExpect(jsonPath("$.color", equalToIgnoringCase("White")))
                .andExpect(jsonPath("$.productionYear", equalTo(1996)))
                .andExpect(jsonPath("$.creationTime", equalTo(OffsetDateTime.of(
                        2011,10,12,
                        12,6,55,0, ZoneOffset.ofHours(7)).toString())))
                .andExpect(jsonPath("$.updateTime", notNullValue()));
    }

    @Test
    @DisplayName("Should throw not found exception")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByInvalidIdThenThrow() throws Exception {
        //given
        Long nonExistentId = 44L;
        //when then
        mockMvc.perform(get("/api/cars/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Car not found for this id :: " + nonExistentId)));
    }

    @Test
    @DisplayName("Should delete entity and return deleted DTO")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteWhenIdIsValidThenReturnDeletedGameResponseDTO() throws Exception {
        //when then
        mockMvc.perform(delete("/api/cars/{id}", 4L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.brand", equalTo("renault")))
                .andExpect(jsonPath("$.model", equalTo("logan")))
                .andExpect(jsonPath("$.color", equalTo("blue")))
                .andExpect(jsonPath("$.productionYear", equalTo(2002)))
                .andExpect(jsonPath("$.creationTime", notNullValue()))
                .andExpect(jsonPath("$.updateTime", notNullValue()));
    }

    @Test
    @DisplayName("Should throw not found exception")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteWhenIdInvalidThenThrow() throws Exception {
        //given
        Long nonExistentId = 42L;
        //when then
        mockMvc.perform(delete("/api/cars/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Car not found for this id :: " + nonExistentId)));
    }

    @Test
    @DisplayName("Should update entity and return updated DTO")
    @Sql(value = {"/sql/set_up_db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateExistingEntityThenReturnUpdatedCarDTO() throws Exception {
        //given
        CarRequestDTO requestBody = CarRequestDTO.builder()
                .brand("Volvo")
                .model("CX33")
                .color("Red")
                .productionYear(1333)
                .build();
        //when then
        mockMvc.perform(put("/api/cars/{carId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.brand", equalTo("Volvo")))
                .andExpect(jsonPath("$.color", equalTo("Red")))
                .andExpect(jsonPath("$.model", equalTo("CX33")))
                .andExpect(jsonPath("$.productionYear", equalTo(1333)))
                .andExpect(jsonPath("$.creationTime", notNullValue()))
                .andExpect(jsonPath("$.updateTime",notNullValue()));
    }
}