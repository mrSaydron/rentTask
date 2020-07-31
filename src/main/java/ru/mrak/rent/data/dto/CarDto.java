package ru.mrak.rent.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarDto {
    
    private Long id;
    
    /** Номер автомобиля **/
    @NotBlank(message = "Необходимо заполнить номер автомобиля")
    private String number;
    
    /** Производитель автомобиля **/
    @NotNull(message = "Необходимо заполнить производителя автомобиля")
    private String manufacturer;
    
    /** Марка автомобиля **/
    @NotNull(message = "Необходимо заполнить марку автомобиля")
    private String brand;
    
    /** Флаг актуальности автомобиля. При false автомобиль нельзя использовать для аренды**/
    private Boolean active;
}
