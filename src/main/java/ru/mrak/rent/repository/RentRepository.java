package ru.mrak.rent.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mrak.rent.data.Page;
import ru.mrak.rent.data.Pageable;
import ru.mrak.rent.data.entity.Car;
import ru.mrak.rent.data.entity.Rent;
import ru.mrak.rent.data.fiter.FilterRent;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class RentRepository {
    
    @Autowired private RentDao rentDao;
    
    @Autowired private EntityManager entityManager;
    
    public Rent getOne(Long aLong) {
        return rentDao.getOne(aLong);
    }
    
    @Transactional
    public <S extends Rent> S save(S entity) {
        return rentDao.save(entity);
    }
    
    public Optional<Rent> findById(Long aLong) {
        return rentDao.findById(aLong);
    }
    
    public boolean existsById(Long aLong) {
        return rentDao.existsById(aLong);
    }
    
    @Transactional
    public void delete(Rent entity) {
        rentDao.delete(entity);
    }
    
    /**
     * Возвращает историю заказов с пагинацией, фильтрацией по заказам (rent) и
     * автомобилям (car), сортировкой по заказам
     */
    public Page<Rent> getList(Pageable<FilterRent> filterRentPageable) {
        FilterRent filter = filterRentPageable.getFilter();
    
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> query = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> fromRent = query.from(Rent.class);
        Join<Rent, Car> joinCar = fromRent.join("car");
    
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<Rent> countRent = count.from(Rent.class);
        Join<Rent, Car> countJoinCar = countRent.join("car");
        count.select(criteriaBuilder.count(countRent));
    
        Predicate queryPredicate = criteriaBuilder.isNotNull(fromRent.get("id"));
        Predicate countPredicate = criteriaBuilder.isNotNull(countRent.get("id"));
        if (filter.getId() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(fromRent.get("id").as(String.class), "%" + filter.getId() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(countRent.get("id").as(String.class), "%" + filter.getId() + "%"));
        }
        if (filter.getRenterName() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(fromRent.get("renterName"), "%" + filter.getRenterName() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(countRent.get("renterName"), "%" + filter.getRenterName() + "%"));
        }
        if (filter.getTookDateFrom() != null && filter.getTookDateTo() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.between(fromRent.get("tookDate"), filter.getTookDateFrom(), filter.getTookDateTo()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.between(countRent.get("tookDate"), filter.getTookDateFrom(), filter.getTookDateTo()));
        } else if (filter.getTookDateFrom() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.greaterThanOrEqualTo(fromRent.get("tookDate"), filter.getTookDateFrom()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.greaterThanOrEqualTo(countRent.get("tookDate"), filter.getTookDateFrom()));
        } else if (filter.getTookDateTo() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.lessThanOrEqualTo(fromRent.get("tookDate"), filter.getTookDateTo()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.lessThanOrEqualTo(countRent.get("tookDate"), filter.getTookDateTo()));
        }
        if (filter.getReturnedDateFrom() != null && filter.getReturnedDateTo() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.between(fromRent.get("returnedDate"), filter.getReturnedDateFrom(), filter.getReturnedDateTo()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.between(countRent.get("returnedDate"), filter.getReturnedDateFrom(), filter.getReturnedDateTo()));
        } else if (filter.getReturnedDateFrom() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.greaterThanOrEqualTo(fromRent.get("returnedDate"), filter.getReturnedDateFrom()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.greaterThanOrEqualTo(countRent.get("returnedDate"), filter.getReturnedDateFrom()));
        } else if (filter.getReturnedDateTo() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.lessThanOrEqualTo(fromRent.get("returnedDate"), filter.getReturnedDateTo()));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.lessThanOrEqualTo(countRent.get("returnedDate"), filter.getReturnedDateTo()));
        }
        
        if (filter.getCarNumber() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(joinCar.get("number")), "%" + filter.getCarNumber().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countJoinCar.get("number")), "%" + filter.getCarNumber().toUpperCase() + "%"));
        }
        if (filter.getCarManufacturer() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(joinCar.get("manufacturer")), "%" + filter.getCarManufacturer().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countJoinCar.get("manufacturer")), "%" + filter.getCarManufacturer().toUpperCase() + "%"));
        }
        if (filter.getCarBrand() != null) {
            queryPredicate = criteriaBuilder.and(queryPredicate, criteriaBuilder.like(criteriaBuilder.upper(joinCar.get("brand")), "%" + filter.getCarBrand().toUpperCase() + "%"));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.like(criteriaBuilder.upper(countJoinCar.get("brand")), "%" + filter.getCarBrand().toUpperCase() + "%"));
        }
        query.where(queryPredicate);
        count.where(countPredicate);
    
        Order order;
        if ("desc".equals(filterRentPageable.getSortDir())) {
            order = criteriaBuilder.desc(fromRent.get(filterRentPageable.getSortBy()));
        } else {
            order = criteriaBuilder.asc(fromRent.get(filterRentPageable.getSortBy()));
        }
        query.orderBy(order);
    
        List<Rent> resultList = entityManager.createQuery(query)
                .setFirstResult(filterRentPageable.getOffset())
                .setMaxResults(filterRentPageable.getLimit())
                .getResultList();
    
        Long resultCount = entityManager.createQuery(count)
                .getSingleResult();
    
        return new Page<>(resultList, filterRentPageable.getPage(), filterRentPageable.getPageSize(), resultCount);
    }
    
    /**
     * Возвращает заказы для указанной машины, которые могут "конфликтовать" с переданными датами
     * начала и конца аренды
     * Конец оренды может не передаваться, тогда считается что аренда открытая
     */
    public List<Rent> getRentListByCarAndDate(Long id,
                                              Long carId,
                                              String carNumber,
                                              Timestamp tookDate,
                                              Timestamp returnedDate) {
        if (id == null) id = -1L;
        List<Rent> result;
        if (returnedDate == null) {
            result = rentDao.getRentListByCarAndTookDate(id, carId, carNumber, tookDate);
        } else {
            result = rentDao.getRentListByCarAndDate(id, carId, carNumber, tookDate, returnedDate);
        }
        return result;
    }
}
