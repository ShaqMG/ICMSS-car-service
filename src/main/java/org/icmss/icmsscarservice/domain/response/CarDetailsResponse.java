package org.icmss.icmsscarservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmsscarservice.domain.enums.CarType;
import org.icmss.icmsscarservice.domain.enums.GearboxType;
import org.icmss.icmsscarservice.domain.enums.SteeringWheelPosition;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetailsResponse {
    private String id;
    private String dealerId;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private BigDecimal price;
    private Long mileage;
    private String vin;
    private String description;
    private String locationCountry;
    private Integer numberOfDoors;
    private Integer numberOfSeats;
    private CarType carType;
    private GearboxType gearboxType;
    private SteeringWheelPosition steeringWheelPosition;
    private List<String> imageUrls;
}
