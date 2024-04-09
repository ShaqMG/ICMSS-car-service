package org.icmss.icmsscarservice.domain.request;

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
public class CarSearchRequest {
    private List<String> brand;
    private List<String> model;
    private Integer yearFrom;
    private Integer yearTo;
    private List<String> color;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Long mileageFrom;
    private Long mileageTo;
    private List<String> locationCountry;
    private Integer numberOfDoorsFrom;
    private Integer numberOfDoorsTo;
    private Integer numberOfSeatsFrom;
    private Integer numberOfSeatsTo;
    private List<CarType> carType;
    private List<GearboxType> gearboxType;
    private List<SteeringWheelPosition> steeringWheelPosition;
    private int page;
    private int size;
}
