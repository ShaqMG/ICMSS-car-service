package org.icmss.icmsscarservice.domain.document;

import lombok.*;
import org.icmss.icmsscarservice.domain.enums.CarType;
import org.icmss.icmsscarservice.domain.enums.DbStatus;
import org.icmss.icmsscarservice.domain.enums.GearboxType;
import org.icmss.icmsscarservice.domain.enums.SteeringWheelPosition;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(indexName = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    private String id = UUID.randomUUID().toString();

    @Field(type = FieldType.Keyword)
    private String dealerId;

    @Field(type = FieldType.Text)
    private String brand;

    @Field(type = FieldType.Text)
    private String model;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Keyword)
    private String color;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Long)
    private Long mileage;

    @Field(type = FieldType.Keyword)
    private String vin;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String locationCountry;

    @Field(type = FieldType.Integer)
    private Integer numberOfDoors;

    @Field(type = FieldType.Integer)
    private Integer numberOfSeats;

    @Field(type = FieldType.Keyword)
    private CarType carType;

    @Field(type = FieldType.Keyword)
    private GearboxType gearboxType;

    @Field(type = FieldType.Keyword)
    private SteeringWheelPosition steeringWheelPosition;

    @Field(type = FieldType.Keyword)
    private DbStatus status;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<String> imagesUrls;
}
