package ru.mrak.rent.data.fiter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterCar {
    private String id;
    private String number;
    private String manufacturer;
    private String brand;
    private Boolean active;
}
