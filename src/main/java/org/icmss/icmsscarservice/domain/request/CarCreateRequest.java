package org.icmss.icmsscarservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmsscarservice.domain.enums.CarType;
import org.icmss.icmsscarservice.domain.enums.GearboxType;
import org.icmss.icmsscarservice.domain.enums.SteeringWheelPosition;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarCreateRequest {
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    private Integer year;
    @NotBlank
    private String color;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Long mileage;
    @NotBlank
    private String vin;
    private String description;
    @NotBlank
    private String locationCountry;
    @NotNull
    private Integer numberOfDoors;
    @NotNull
    private Integer numberOfSeats;
    @NotNull
    private CarType carType;
    @NotNull
    private GearboxType gearboxType;
    @NotNull
    private SteeringWheelPosition steeringWheelPosition;
    private List<String> imageUrls;
}