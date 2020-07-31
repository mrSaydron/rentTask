package ru.mrak.rent.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mrak.rent.data.Page;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.dto.CarDto;
import ru.mrak.rent.data.dto.CarStatisticDto;
import ru.mrak.rent.data.fiter.FilterCar;
import ru.mrak.rent.service.CarService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {
    
    @Autowired private CarService carService;
    
    @ApiOperation(value = "Создает новую запись об автомобиле")
    @RequestMapping(method = RequestMethod.POST)
    public CarDto create(@Valid @RequestBody CarDto car) {
        return carService.create(car);
    }
    
    @ApiOperation(value = "Возвращает автомобиль по его идентификатору")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CarDto read(@PathVariable Long id) {
        return carService.get(id);
    }
    
    @ApiOperation(value = "Редактирует автомобиль")
    @RequestMapping(method = RequestMethod.PUT)
    public CarDto update(@RequestBody CarDto car) {
        return carService.update(car);
    }
    
    @ApiOperation(value = "Помечает автомобиль не активным")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        carService.delete(id);
    }
    
    @ApiOperation(value = "Возвращает список автомобилей с пагинацией, сортировкой и фильтрацией")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Page<CarDto> getList(@RequestBody Pageable<FilterCar> paramCar) {
        return carService.getList(paramCar);
    }
    
    @ApiOperation(value = "Возвращает статистику по аренде автомобилей. Допустимые groupField: number, manufacturer, brand")
    @RequestMapping(value = "/statistic/{groupField}", method = RequestMethod.GET)
    public List<CarStatisticDto> getStatistic(@PathVariable String groupField) {
        return carService.getStatistic(groupField);
    }
    
}
