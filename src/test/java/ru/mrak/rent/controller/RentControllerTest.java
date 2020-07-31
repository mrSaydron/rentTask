package ru.mrak.rent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.RentApplication;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.dto.RentDto;
import ru.mrak.rent.data.fiter.FilterRent;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = RentApplication.class)
class RentControllerTest {
    
    @Autowired private MockMvc restCarMockMvc;
    
    @Test
    @Transactional
    void create() throws Exception {
        RentDto rentDto = RentDto.builder()
                .renterName("Александр")
                .tookDate(Timestamp.valueOf("2020-06-12 18:52:57"))
                .carNumber("b234bb59")
                .carManufacturer("ВАЗ")
                .carBrand("Гранта")
                .build();
    
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(rentDto);
    
        ResultActions resultActions = restCarMockMvc.perform(post("/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }
    
    @Test
    void read() throws Exception {
        restCarMockMvc.perform(get("/rent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    void update() throws Exception {
        RentDto rentDto = RentDto.builder()
                .id(1L)
                .renterName("Кирилл")
                .tookDate(Timestamp.valueOf("2020-08-30 18:52:57"))
                .carId(3L)
                .build();
    
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(rentDto);
    
        restCarMockMvc.perform(put("/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    void delete_test() throws Exception {
        restCarMockMvc.perform(delete("/rent/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void getHistory() throws Exception {
        FilterRent filter = FilterRent.builder()
                .renterName("Андрей")
                .carNumber("a100")
                .build();
    
        Pageable<FilterRent> pageable = new Pageable<>(filter,
                0,10, null, null);
    
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(pageable);
    
        restCarMockMvc.perform(post("/rent/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }
}