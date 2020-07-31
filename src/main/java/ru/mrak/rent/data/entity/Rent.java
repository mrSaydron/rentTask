package ru.mrak.rent.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "rent")
@Getter
@Setter
public class Rent {
    
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_car", sequenceName = "seq_car", allocationSize = 1)
    @GeneratedValue(generator = "seq_car", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "renter_name")
    private String renterName;
    
    @Column(name = "took_date")
    private Timestamp tookDate;
    
    @Column(name = "returned_date")
    private Timestamp returnedDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;
}
