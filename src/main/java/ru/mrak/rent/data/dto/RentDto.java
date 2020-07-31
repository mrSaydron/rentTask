package ru.mrak.rent.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RentDto {
    
    private Long id;
    
    /** Имя арендатора **/
    @NotBlank(message = "Необходимо заполнить имя арендатора")
    private String renterName;
    
    /** Время когда автомобил был взят в аредну **/
    @NotNull(message = "Необходимо заполнить дату взятия в аренду")
    private Timestamp tookDate;
    
    /** Время когда автомобиль был возвращен. Может быть пустым**/
    private Timestamp returnedDate;
    
    private Long carId;
    
    /** Номер автомобиля **/
    private String carNumber;
    
    /** Производитель автомобиля **/
    private String carManufacturer;
    
    /** Марка автомобиля **/
    private String carBrand;
}
