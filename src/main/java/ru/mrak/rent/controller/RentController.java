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
import ru.mrak.rent.data.dto.RentDto;
import ru.mrak.rent.data.fiter.FilterRent;
import ru.mrak.rent.service.RentService;

@RestController
@RequestMapping(value = "/rent")
public class RentController {
    
    @Autowired private RentService rentService;
    
    @ApiOperation(value = "Создает новую запись об аренде. " +
            "Если связанный с арендой автомобиль не найден, то будет создан новый автомобиль." +
            "Уже созданный автомобиль не редактируется")
    @RequestMapping(method = RequestMethod.POST)
    public RentDto create(@RequestBody RentDto rent) {
        return rentService.create(rent);
    }
    
    @ApiOperation(value = "Возвращает запись об аренде по ее идентификатору")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RentDto read(@PathVariable Long id) {
        return rentService.get(id);
    }
    
    @ApiOperation(value = "Редактирует запись об аренде. " +
            "Если связанный с арендой автомобиль не найден, то будет создан новый автомобиль." +
            "Уже созданный автомобиль не редактируется")
    @RequestMapping(method = RequestMethod.PUT)
    public RentDto update(@RequestBody RentDto rent) {
        return rentService.update(rent);
    }
    
    @ApiOperation(value = "Удаляет запись об аренде")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        rentService.delete(id);
    }
    
    @ApiOperation(value = "Возвращает историю аренды с пагинацией, фильтрацией по аренде и автомобилям, " +
            "сортировкой по аренде")
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public Page<RentDto> getHistory(@RequestBody Pageable<FilterRent> pageable) {
        return rentService.getList(pageable);
    }
}
