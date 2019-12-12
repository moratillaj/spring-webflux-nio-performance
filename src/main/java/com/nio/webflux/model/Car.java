package com.nio.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "car")
@Entity
@NoArgsConstructor
public class Car {
    @Id
    private String id;
    private String model;
    private Integer year;
}
