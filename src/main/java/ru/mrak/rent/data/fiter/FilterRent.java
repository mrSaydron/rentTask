package ru.mrak.rent.data.fiter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterRent {
    private String id;
    private String renterName;
    private Timestamp tookDateFrom;
    private Timestamp tookDateTo;
    private Timestamp returnedDateFrom;
    private Timestamp returnedDateTo;
    private String carId;
    private String carNumber;
    private String carManufacturer;
    private String carBrand;
}
