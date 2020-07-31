package ru.mrak.rent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.RentApplication;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.entity.Car;
import ru.mrak.rent.data.fiter.FilterCar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = RentApplication.class)
class CarControllerTest {
    
    @Autowired private MockMvc restCarMockMvc;
    
    @Test
    @Transactional
    void create() throws Exception {
        Car car = Car.builder()
                .number("a999aa59")
                .manufacturer("volkswagen")
                .brand("polo")
                .build();
    
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(car);
    
        restCarMockMvc.perform(post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }
    
    @Test
    void read() throws Exception {
        restCarMockMvc.perform(get("/car/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    void update() throws Exception {
        Car car = Car.builder()
                .id(1L)
                .number("a999aa59")
                .manufacturer("volkswagen")
                .brand("polo")
                .active(true)
                .build();
    
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(car);
    
        restCarMockMvc.perform(put("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    void delete_test() throws Exception {
        restCarMockMvc.perform(delete("/car/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void getList() throws Exception {
        FilterCar filterCar = FilterCar.builder()
                .id("3")
                .manufacturer("ced")
                .brand("class")
                .number("102")
                .build();
    
        Pageable<FilterCar> pageable = new Pageable<>(filterCar, 0, 10, null, null);
    
        ObjectMapper objectMapper = new ObjectMapper();
        String pageableString = objectMapper.writeValueAsString(pageable);
        restCarMockMvc.perform(post("/car/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pageableString))
                .andExpect(status().isOk());
    }
    
    @Test
    void statistic() throws Exception {
        restCarMockMvc.perform(get("/car/statistic/{field}", "number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}