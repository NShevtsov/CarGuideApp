package com.project.carguideapp.repository;

import com.project.carguideapp.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(value =
            "SELECT count(id)" +
                    "FROM car"
            , nativeQuery = true)
    public Long countAll();

    public Car findByRegistrationNumber(String registrationNumber);

    @Query(value =
            "SELECT * " +
                    "FROM car " +
                    "order by update_time desc " +
                    "limit 1"
            , nativeQuery = true)
    public Car findLastUpdated();

    @Query(value =
            "SELECT count(c) FROM car c WHERE c.brand=:brand "
            , nativeQuery = true)
    public Long countByBrand(@Param("brand")String brand);


}
