package ru.mrak.rent.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.mrak.rent.data.dto.CarDto;
import ru.mrak.rent.data.entity.Car;

@Mapper
public interface CarMapper {
    
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );
    
    CarDto carToCarDto(Car car);
    
    Car carDtoToCar(CarDto carDto);
    
}
