package ru.mrak.rent.data.entity;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "car")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_car", sequenceName = "seq_car", allocationSize = 1)
    @GeneratedValue(generator = "seq_car", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "number")
    private String number;
    
    @Column(name = "manufacturer")
    private String manufacturer;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "active")
    private Boolean active = true;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Rent> rentList;
    
}
