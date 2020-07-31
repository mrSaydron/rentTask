package ru.mrak.rent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.config.exception.NotFoundException;
import ru.mrak.rent.config.exception.ValidationException;
import ru.mrak.rent.data.Page;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.dto.RentDto;
import ru.mrak.rent.data.entity.Car;
import ru.mrak.rent.data.entity.Rent;
import ru.mrak.rent.data.fiter.FilterRent;
import ru.mrak.rent.repository.RentRepository;
import ru.mrak.rent.service.mapper.RentMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RentService {
    
    @Autowired private CarService carService;
    
    @Autowired private RentRepository rentRepository;
    
    /**
     * Создаю запись аренды. Если для данной записи не найден автомобить то пытаюсь создать его
     * Если автомобиль найден, то его поля не редактируются
     */
    @Transactional
    public RentDto create(RentDto rentDto) {
        validateCreate(rentDto);
        Rent rent = RentMapper.INSTANCE.rentDtoToRent(rentDto);
    
        Car car;
        try {
            car = carService.getByIdOrNumber(rent.getCar().getId(), rent.getCar().getNumber());
        } catch (NotFoundException e) {
            car = carService.create(rent.getCar());
        }
        if (!car.getActive()) {
            throw new ValidationException("Нельзя для аренды использовать неактивную машину");
        }
        rent.setCar(car);
        Rent newRent = rentRepository.save(rent);
        
        return RentMapper.INSTANCE.rentToRentDto(newRent);
    }
    
    /**
     * возвращает аренду по идентификатору
     */
    public RentDto get(Long rentId) {
        Optional<Rent> rentOptional = rentRepository.findById(rentId);
        rentOptional.orElseThrow(() -> new NotFoundException("Не найдена аренда с id: " + rentId));
        return RentMapper.INSTANCE.rentToRentDto(rentOptional.get());
    }
    
    /**
     * Обновляет запись аренды. Если автомобил не найден, то будет создан новый по переданным данным
     * Если автомобиль найден, то его поля не редактируются
     */
    @Transactional
    public RentDto update(RentDto rentDto) {
        validationUpdate(rentDto);
        
        if (!rentRepository.existsById(rentDto.getId())) {
            throw new RuntimeException("Не найдена аренда с id: " + rentDto.getId());
        }
        Rent rent = RentMapper.INSTANCE.rentDtoToRent(rentDto);
    
        Car car = carService.getByIdOrNumber(rent.getCar().getId(), rent.getCar().getNumber());
        if (car == null) {
            car = carService.create(rent.getCar());
        }
        rent.setCar(car);
        Rent newRent = rentRepository.save(rent);
        
        return RentMapper.INSTANCE.rentToRentDto(newRent);
    }
    
    /**
     * Удаляет запись аренды
     */
    @Transactional
    public void delete(Long rentId) {
        Rent rent = rentRepository.getOne(rentId);
        rentRepository.delete(rent);
    }
    
    public Page<RentDto> getList(Pageable<FilterRent> filterRentPageable) {
        if (filterRentPageable.getFilter() == null) filterRentPageable.setFilter(new FilterRent());
        if (filterRentPageable.getSortBy() == null) filterRentPageable.setSortBy("id");
        if (filterRentPageable.getSortDir() == null) filterRentPageable.setSortDir("asc");
        
        Page<Rent> rentPage = rentRepository.getList(filterRentPageable);
        List<RentDto> rentDtoList = rentPage.getContent().stream().map(RentMapper.INSTANCE::rentToRentDto).collect(Collectors.toList());
        
        return new Page<>(rentDtoList, rentPage.getPage(), rentPage.getPageSize(), rentPage.getCount());
    }
    
    /**
     * Проверки при создании
     */
    private void validateCreate(RentDto rentDto) {
        ValidationException validationException = new ValidationException();
        validationException.putAll(validationCommon(rentDto));
        validationException.isErrors();
    }
    
    /**
     * Проверки при обновдении
     */
    private void validationUpdate(RentDto rentDto) {
        ValidationException validationException = new ValidationException();
        
        validationException.putAll(validationCommon(rentDto));
        if (rentDto.getId() == null) validationException.put("Не указан идентификатор аренды");
        
        validationException.isErrors();
    }
    
    /**
     * общие проверки
     */
    private ValidationException validationCommon(RentDto rentDto) {
        ValidationException validationException = new ValidationException();
    
        if (rentDto.getReturnedDate() != null && rentDto.getReturnedDate().compareTo(rentDto.getTookDate()) <= 0) {
            validationException.put("Дата возврата не может быть раньше даты выдачи");
        }
    
        List<Rent> rentList = rentRepository.getRentListByCarAndDate(
                rentDto.getId(),
                rentDto.getCarId(),
                rentDto.getCarNumber(),
                rentDto.getTookDate(),
                rentDto.getReturnedDate());
        if (!rentList.isEmpty()) {
            validationException.put("Данное время уже занято");
        }
        
        return validationException;
    }
}
