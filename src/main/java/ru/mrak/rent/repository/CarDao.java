package ru.mrak.rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mrak.rent.data.entity.Car;

public interface CarDao extends JpaRepository<Car, Long> {
    
    Car getByNumber(String number);
    
    Car getFirstByIdOrNumber(Long id, String number);
}
