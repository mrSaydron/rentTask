package ru.mrak.rent.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.data.Page;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.dto.CarStatisticDto;
import ru.mrak.rent.data.entity.Car;
import ru.mrak.rent.data.fiter.FilterCar;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class CarRepository {
    
    @Autowired private CarDao carDao;
    
    @Autowired private EntityManager entityManager;
    
    @Transactional
    public Car save(Car car) {
        return carDao.save(car);
    }
    
    @Transactional
    public void delete(Car car) {
        carDao.delete(car);
    }
    
    public Car getOne(Long id) {
        return carDao.getOne(id);
    }
    
    public boolean existsById(Long id) {
        return carDao.existsById(id);
    }
    
    public Optional<Car> findById(Long id) {
        return carDao.findById(id);
    }
    
    public Car getByNumber(String number) {
        return carDao.getByNumber(number);
    }
    
    /**
     * Возвращает список автомобилей с пагинацией, фильтрацией и сортировкой по полям car.
     */
    public Page<Car> getList(Pageable<FilterCar> paramCar) {
        FilterCar filter = paramCar.getFilter();
    
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> query = criteriaBuilder.createQuery(Car.class);
        Root<Car> from = query.from(Car.class);
    
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<Car> countRoot = count.from(Car.class);
        count.select(criteriaBuilder.count(countRoot));
    
        Predicate queryPredicate = criteriaBuilder.isNotNull(from.get("id"));
        Predicate countPredicate = criteriaBuilder.isNotNull(from.get("id"));
        if (filter.getId() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(from.get("id").as(String.class), "%" + filter.getId() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(countRoot.get("id").as(String.class), "%" + filter.getId() + "%"));
        }
        if (filter.getNumber() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(from.get("number")), "%" + filter.getNumber().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countRoot.get("number")), "%" + filter.getNumber().toUpperCase() + "%"));
        }
        if (filter.getManufacturer() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(from.get("manufacturer")), "%" + filter.getManufacturer().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countRoot.get("manufacturer")), "%" + filter.getManufacturer().toUpperCase() + "%"));
        }
        if (filter.getBrand() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(from.get("brand")), "%" + filter.getBrand().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countRoot.get("brand")), "%" + filter.getBrand().toUpperCase() + "%"));
        }
        if (filter.getActive() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.equal(from.get("active"), filter.getActive()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("active"), filter.getActive()));
        }
        query.where(queryPredicate);
        count.where(countPredicate);
    
        Order order;
        if ("desc".equals(paramCar.getSortDir())) {
            order = criteriaBuilder.desc(from.get(paramCar.getSortBy()));
        } else {
            order = criteriaBuilder.asc(from.get(paramCar.getSortBy()));
        }
        query.orderBy(order);
    
        List<Car> resultList = entityManager.createQuery(query)
                .setFirstResult(paramCar.getOffset())
                .setMaxResults(paramCar.getLimit())
                .getResultList();
    
        Long resultCount = entityManager.createQuery(count)
                .getSingleResult();
    
        return new Page<>(resultList, paramCar.getPage(), paramCar.getPageSize(), resultCount);
    }
    
    /**
     * Возвращает автомобиль по идентификатору или по номеру
     */
    public Car getByIdOrNumber(Long id, String number) {
        return carDao.getFirstByIdOrNumber(id, number);
    }
    
    private static final String statisticQueryFormat =
            "select car.%1$s as groupFieldValue, count(*) as countRent, sum(extract(epoch from rent.returned_date)) - sum(extract(epoch from rent.took_date)) as rentSeconds\n" +
            "from car\n" +
            "join rent on rent.car_id = car.id\n" +
            "where rent.returned_date notnull\n" +
            "group by car.%1$s";
    
    /**
     * Возвращает статистике по автомобилям, с группировкой по переданному полю
     * Стаистика состоит из количества заказов и общего времени всех заказов
     * Немного не по заданию. В задании надо было среднее время, но думаю что так лучше
     */
    public List<CarStatisticDto> getStatistic(String groupField) {
        return (List<CarStatisticDto>) entityManager.createNativeQuery(String.format(statisticQueryFormat, groupField))
                .getResultList()
                .stream()
                .map(result -> {
                    String groupFieldValue = ((Object[]) result)[0].toString();
                    Double rentSeconds = (Double) ((Object[]) result)[2];
                    Integer countRent = ((BigInteger) ((Object[]) result)[1]).intValue();
                    return new CarStatisticDto(groupFieldValue, rentSeconds, countRent);
                }).collect(Collectors.toList());
    }
}
