package org.icmss.icmsscarservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmsscarservice.domain.enums.CarType;
import org.icmss.icmsscarservice.domain.enums.GearboxType;
import org.icmss.icmsscarservice.domain.enums.SteeringWheelPosition;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarListResponse {
    private String id;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private CarType carType;
    private GearboxType gearboxType;
    private SteeringWheelPosition steeringWheelPosition;
    private String imageUrl;
}
