package ru.mrak.rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mrak.rent.data.entity.Rent;

import java.sql.Timestamp;
import java.util.List;

public interface RentDao extends JpaRepository<Rent, Long> {
    
    @Query("select r from Rent r where r.id <> :id and (r.car.id = :carId or r.car.number = :carNumber) and (r.returnedDate is null or r.returnedDate > :tookDate)")
    List<Rent> getRentListByCarAndTookDate(@Param("id") Long id,
                                           @Param("carId") Long carId,
                                           @Param("carNumber") String carNumber,
                                           @Param("tookDate") Timestamp tookDate);
    
    @Query("select r from Rent r where r.id <> :id and (r.car.id = :carId or r.car.number = :carNumber) " +
            "and ((r.returnedDate is null and r.tookDate < :tookDate) " +
            "or (r.tookDate > :tookDate and r.tookDate < :returnedDate)" +
            "or (r.returnedDate > :tookDate and r.returnedDate < :returnedDate)" +
            "or (r.tookDate < :tookDate and r.returnedDate > :returnadDate))")
    List<Rent> getRentListByCarAndDate(@Param("id") Long id,
                                       @Param("carId") Long carId,
                                       @Param("carNumber") String carNumber,
                                       @Param("tookDate") Timestamp tookDate,
                                       @Param("returnedDate") Timestamp returnedDate);
}
