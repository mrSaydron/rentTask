package ru.mrak.rent.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.mrak.rent.data.dto.RentDto;
import ru.mrak.rent.data.entity.Rent;

@Mapper
public interface RentMapper {
    
    RentMapper INSTANCE = Mappers.getMapper( RentMapper.class );
    
    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "car.number", target = "carNumber")
    @Mapping(source = "car.manufacturer", target = "carManufacturer")
    @Mapping(source = "car.brand", target = "carBrand")
    RentDto rentToRentDto(Rent rent);
    
    @Mapping(source = "carId", target = "car.id")
    @Mapping(source = "carNumber", target = "car.number")
    @Mapping(source = "carManufacturer", target = "car.manufacturer")
    @Mapping(source = "carBrand", target = "car.brand")
    Rent rentDtoToRent(RentDto rentDto);
    
}
