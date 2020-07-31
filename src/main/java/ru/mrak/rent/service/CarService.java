package ru.mrak.rent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.config.exception.NotFoundException;
import ru.mrak.rent.config.exception.ValidationException;
import ru.mrak.rent.data.Page;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.dto.CarDto;
import ru.mrak.rent.data.dto.CarStatisticDto;
import ru.mrak.rent.data.entity.Car;
import ru.mrak.rent.data.fiter.FilterCar;
import ru.mrak.rent.repository.CarRepository;
import ru.mrak.rent.service.mapper.CarMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CarService {
    
    @Autowired private Validator validator;
    
    @Autowired private CarRepository carRepository;
    
    /**
     * Создает новую запись с автомобилем
     */
    @Transactional
    public CarDto create(CarDto carDto) {
        carDto.setId(null);
        
        validateCreate(carDto);
        Car car = CarMapper.INSTANCE.carDtoToCar(carDto);
        Car newCar = create(car);
        return CarMapper.INSTANCE.carToCarDto(newCar);
    }
    
    /**
     * Создает новую запись с автомобилем
     */
    @Transactional
    public Car create(Car car) {
        car.setActive(true);
        return carRepository.save(car);
    }
    
    /**
     * Возвращает запись с автомобилем
     */
    public CarDto get(Long id) {
        Optional<Car> byId = carRepository.findById(id);
        byId.orElseThrow(() -> new RuntimeException("Не найден автомобиль с id: " + id));
        return CarMapper.INSTANCE.carToCarDto(byId.get());
    }
    
    /**
     * Возвращает автомобил по идентификатору или по номеру
     * Если у найденного автомобиля не совпал идентификатор или номер то бросается ошибка
     */
    public Car getByIdOrNumber(Long id, String number) {
        Car car = carRepository.getByIdOrNumber(id, number);
        if (car == null) throw new NotFoundException("Не найден автомобиль с id: " + id + " или номером: " + number);
        if (id != null && !id.equals(car.getId())) throw new ValidationException("Идентификатор автомобиля не совпал с номерм");
        if (number != null && !number.equals(car.getNumber())) throw new ValidationException("Идентификатор автомобиля не совпал с номерм");
        return car;
    }
    
    /**
     * Обновляет запись об автомобиле
     */
    @Transactional
    public CarDto update(CarDto carDto) {
        validateUpdate(carDto);
        if (!carRepository.existsById(carDto.getId())) {
            throw new RuntimeException("Не найден автомобиль с id: " + carDto.getId());
        }
        Car car = CarMapper.INSTANCE.carDtoToCar(carDto);
        Car newCar = carRepository.save(car);
        return CarMapper.INSTANCE.carToCarDto(newCar);
    }
    
    /**
     * Проставляет для автомобиля признак не активности. При этом сам автомобиль не удаляется
     */
    @Transactional
    public void delete(Long id) {
        Optional<Car> carOpt = carRepository.findById(id);
        carOpt.orElseThrow(() -> new RuntimeException("Не найден автомобиль с id: " + id));
        Car car = carOpt.get();
        carRepository.save(car);
    }
    
    
    /**
     * Возвращает список автомобилей с фильтрацией, сортировкой и пагинацией
     */
    public Page<CarDto> getList(Pageable<FilterCar> paramCar) {
        if (paramCar.getFilter() == null) paramCar.setFilter(new FilterCar());
        if (paramCar.getSortBy() == null) paramCar.setSortBy("number");
        if (paramCar.getSortDir() == null) paramCar.setSortDir("asc");
        if (paramCar.getFilter().getActive() == null) paramCar.getFilter().setActive(true);
    
        Page<Car> list = carRepository.getList(paramCar);
        List<CarDto> dtoList = list.getContent().stream().map(CarMapper.INSTANCE::carToCarDto).collect(Collectors.toList());
        
        return new Page<>(dtoList, list.getPage(), list.getPageSize(), list.getCount());
    }
    
    /**
     * Возвращает статистике по автомобилям, с группировкой по переданному полю
     * Стаистика состоит из количества заказов и общего времени всех заказов
     * Немного не по заданию. В задании надо было среднее время, но думаю что так лучше
     */
    public List<CarStatisticDto> getStatistic(String groupField) {
        return carRepository.getStatistic(groupField);
    }
    
    /**
     * Проверки при создании автомобиля
     */
    private void validateCreate(CarDto car) {
        ValidationException validationException = new ValidationException();
        validationException.putAll(validateCommon(car));
        validationException.isErrors();
    }
    
    /**
     * Проверки при редактировании автомобиля
     */
    private void validateUpdate(CarDto car) {
        ValidationException validationException = new ValidationException();
        validationException.putAll(validateCommon(car));
        
        if (car.getId() == null) {
            validationException.put("Не указан идентификатор машины");
        }
        
        validationException.isErrors();
    }
    
    /**
     * Общие проверки
     */
    private ValidationException validateCommon(CarDto car) {
        ValidationException validationException = new ValidationException();
        
        Set<ConstraintViolation<CarDto>> validate = validator.validate(car);
        for (ConstraintViolation<CarDto> carConstraintViolation : validate) {
            validationException.put(carConstraintViolation.getMessage());
        }
        
        if (car.getNumber() != null) {
            Car carByNumber = carRepository.getByNumber(car.getNumber());
            if (carByNumber != null) validationException.put("Машина с таким номером уже существует");
        }
        
        return validationException;
    }
}
