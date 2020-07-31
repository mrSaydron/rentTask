package ru.mrak.rent.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarStatisticDto {
    
    /** Значение поля по которому рассматривается статистика **/
    private String groupFieldValue;
    
    /** Общее время нахождения автомобиля в аренде**/
    private Double rentSeconds;
    
    /** Сколько раз автомобиль был возвращен из аренды**/
    private Integer countRent;
}
