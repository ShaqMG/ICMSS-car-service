package org.icmss.icmsscarservice.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.icmss.icmsscarservice.domain.document.Car;
import org.icmss.icmsscarservice.domain.enums.DbStatus;
import org.icmss.icmsscarservice.domain.request.CarCreateRequest;
import org.icmss.icmsscarservice.domain.request.CarSearchRequest;
import org.icmss.icmsscarservice.domain.request.CarUpdateRequest;
import org.icmss.icmsscarservice.domain.response.CarDetailsResponse;
import org.icmss.icmsscarservice.domain.response.CarListResponse;
import org.icmss.icmsscarservice.exceptions.BusinessException;
import org.icmss.icmsscarservice.mapper.CarMapper;
import org.icmss.icmsscarservice.util.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final IndexCoordinates carIndex = IndexCoordinates.of("car");

    public void createCar(CarCreateRequest request) {
        Car car = CarMapper.toEntity(request);
        car.setDealerId(UserUtil.getCurrentUser());
        car.setStatus(DbStatus.ACTIVE);
        car.setCreatedAt(LocalDateTime.now(Clock.systemUTC()));
        car.setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));

        elasticsearchOperations.save(car, carIndex);
    }

    public void updateCar(String carId, CarUpdateRequest request) {
        Car car = elasticsearchOperations.get(carId, Car.class, carIndex);
        if (car == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Car not found with id: " + carId);
        }

        CarMapper.updateEntity(request, car);
        car.setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));

        elasticsearchOperations.save(car, carIndex);
    }

    public void deleteCar(String carId) {
        Car car = elasticsearchOperations.get(carId, Car.class, carIndex);
        if (car == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Car not found with id: " + carId);
        }

        car.setStatus(DbStatus.DELETED);
        car.setUpdatedAt(LocalDateTime.now());

        elasticsearchOperations.save(car, carIndex);
    }

    public CarDetailsResponse getCarDetails(String carId) {
        Car car = elasticsearchOperations.get(carId, Car.class, carIndex);
        if (car == null || car.getStatus() != DbStatus.ACTIVE) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Car not found with id: " + carId);
        }

        return CarMapper.toDetailsResponse(car);
    }

    public List<CarListResponse> getAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("status", DbStatus.ACTIVE.name())))
                .withPageable(pageable)
                .build();

        SearchHits<Car> searchHits = elasticsearchOperations.search(query, Car.class, carIndex);
        return searchHits.stream()
                .map(hit -> CarMapper.toListResponse(hit.getContent()))
                .collect(Collectors.toList());
    }

    public List<CarListResponse> searchCars(CarSearchRequest request) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("status", DbStatus.ACTIVE.ordinal()));

        if (request.getBrand() != null && !request.getBrand().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("brand", request.getBrand()));
        }
        if (request.getModel() != null && !request.getModel().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("model", request.getModel()));
        }
        if (request.getYearFrom() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("year").gte(request.getYearFrom()));
        }
        if (request.getYearTo() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("year").lte(request.getYearTo()));
        }
        if (request.getColor() != null && !request.getColor().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("color", request.getColor()));
        }
        if (request.getPriceFrom() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(request.getPriceFrom().doubleValue()));
        }
        if (request.getPriceTo() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(request.getPriceTo().doubleValue()));
        }
        if (request.getMileageFrom() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mileage").gte(request.getMileageFrom()));
        }
        if (request.getMileageTo() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mileage").lte(request.getMileageTo()));
        }
        if (request.getLocationCountry() != null && !request.getLocationCountry().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("locationCountry", request.getLocationCountry()));
        }
        if (request.getNumberOfDoorsFrom() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("numberOfDoors").gte(request.getNumberOfDoorsFrom()));
        }
        if (request.getNumberOfDoorsTo() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("numberOfDoors").lte(request.getNumberOfDoorsTo()));
        }
        if (request.getNumberOfSeatsFrom() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("numberOfSeats").gte(request.getNumberOfSeatsFrom()));
        }
        if (request.getNumberOfSeatsTo() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("numberOfSeats").lte(request.getNumberOfSeatsTo()));
        }
        if (request.getCarType() != null && !request.getCarType().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("carType", request.getCarType()));
        }
        if (request.getGearboxType() != null && !request.getGearboxType().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("gearboxType", request.getGearboxType().stream().map(Enum::name).collect(Collectors.toList())));
        }
        if (request.getSteeringWheelPosition() != null && !request.getSteeringWheelPosition().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("steeringWheelPosition", request.getSteeringWheelPosition().stream().map(Enum::name).collect(Collectors.toList())));
        }

        queryBuilder.withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(request.getPage(), request.getSize()));

        Query query = queryBuilder.build();

        SearchHits<Car> searchHits = elasticsearchOperations.search(query, Car.class, carIndex);
        return searchHits.stream()
                .map(hit -> CarMapper.toListResponse(hit.getContent()))
                .collect(Collectors.toList());
    }

//    public List<CarListResponse> searchCars(CarSearchRequest request) {
//        NativeQueryBuilder queryBuilder = NativeQuery.builder()
//                .withPageable(PageRequest.of(request.getPage(), request.getSize()));
//
//        addItemFiltersAggregation(queryBuilder);
//        addCarFilters(request, queryBuilder);
//
//        SearchHits<Car> result = elasticsearchOperations.search(queryBuilder.build(), Car.class);
//        return result.stream()
//                .map(hit -> CarMapper.toListResponse(hit.getContent()))
//                .collect(Collectors.toList());
//    }
//
//    private void addItemFiltersAggregation(NativeQueryBuilder queryBuilder) {
//        addAggregation(queryBuilder, MODEL_FIELD, MODEL_FIELD, 10000);
//        addAggregation(queryBuilder, BRAND_FIELD, BRAND_FIELD, 10000);
//        addAggregation(queryBuilder, CAR_TYPE_FIELD, CAR_TYPE_FIELD, 10000);
//        addAggregation(queryBuilder, GEARBOX_TYPE_FIELD, GEARBOX_TYPE_FIELD, 10000);
//        addAggregation(queryBuilder, STEERING_WHEEL_POSITION_FIELD, STEERING_WHEEL_POSITION_FIELD, 10000);
//        addAggregation(queryBuilder, COLOR_FIELD, COLOR_FIELD, 10000);
//    }
//
//    private void addCarFilters(CarSearchRequest request, NativeQueryBuilder queryBuilder) {
//        List<FieldValue> itemStatuses = Stream.of(DbStatus.ACTIVE).map(is -> FieldValue.of(is.name())).toList();
//        queryBuilder.withQuery(q -> q.bool(b -> {
//            b.must(termsQuery(STATUS_FIELD, itemStatuses)._toQuery());
//            if (!CollectionUtils.isEmpty(request.getBrand())) {
//                List<FieldValue> values = convertFieldValues(request.getBrand(), FieldValue::of);
//                b.must(termsQuery(BRAND_FIELD, values)._toQuery());
//            }
//
//            if (request.getModel() != null) {
//                List<FieldValue> values = convertFieldValues(request.getModel(), FieldValue::of);
//                b.must(termsQuery(MODEL_FIELD, values)._toQuery());
//            }
//
//            if (request.getYearFrom() != null) {
//                b.must(rangeGTEQuery(YEAR_FIELD, request.getYearFrom())._toQuery());
//            }
//
//            if (request.getYearTo() != null) {
//                b.must(rangeLTEQuery(YEAR_FIELD, request.getYearTo())._toQuery());
//            }
//
//            if (request.getColor() != null) {
//                List<FieldValue> values = convertFieldValues(request.getColor(), FieldValue::of);
//                b.must(termsQuery(COLOR_FIELD, values)._toQuery());
//            }
//
//            if (request.getPriceFrom() != null) {
//                b.must(rangeGTEQuery(PRICE_FIELD, request.getPriceFrom())._toQuery());
//            }
//
//            if (request.getPriceTo() != null) {
//                b.must(rangeLTEQuery(PRICE_FIELD, request.getPriceTo())._toQuery());
//            }
//
//            if (request.getMileageFrom() != null) {
//                b.must(rangeGTEQuery(MILEAGE_FIELD, request.getMileageFrom())._toQuery());
//            }
//
//            if (request.getMileageTo() != null) {
//                b.must(rangeLTEQuery(MILEAGE_FIELD, request.getMileageTo())._toQuery());
//            }
//
//            if (request.getLocationCountry() != null) {
//                List<FieldValue> values = convertFieldValues(request.getLocationCountry(), FieldValue::of);
//                b.must(termsQuery(LOCATION_COUNTRY_FIELD, values)._toQuery());
//            }
//
//            if (request.getNumberOfDoorsFrom() != null) {
//                b.must(rangeGTEQuery(NUMBER_OF_DOORS_FIELD, request.getNumberOfDoorsFrom())._toQuery());
//            }
//
//            if (request.getNumberOfDoorsTo() != null) {
//                b.must(rangeLTEQuery(NUMBER_OF_DOORS_FIELD, request.getNumberOfDoorsTo())._toQuery());
//            }
//
//            if (request.getNumberOfSeatsFrom() != null) {
//                b.must(rangeGTEQuery(NUMBER_OF_SEATS_FIELD, request.getNumberOfSeatsFrom())._toQuery());
//            }
//
//            if (request.getNumberOfSeatsTo() != null) {
//                b.must(rangeLTEQuery(NUMBER_OF_SEATS_FIELD, request.getNumberOfSeatsTo())._toQuery());
//            }
//
//            if (request.getCarType() != null) {
//                List<FieldValue> values = convertFieldValues(request.getCarType(), c -> FieldValue.of(c.name()));
//                b.must(termsQuery(CAR_TYPE_FIELD, values)._toQuery());
//            }
//
//            if (request.getGearboxType() != null) {
//                List<FieldValue> values = convertFieldValues(request.getGearboxType(), c -> FieldValue.of(c.name()));
//                b.must(termsQuery(GEARBOX_TYPE_FIELD, values)._toQuery());
//            }
//
//            if (request.getSteeringWheelPosition() != null) {
//                List<FieldValue> values = convertFieldValues(request.getSteeringWheelPosition(), c -> FieldValue.of(c.name()));
//                b.must(termsQuery(STEERING_WHEEL_POSITION_FIELD, values)._toQuery());
//            }
//
//            return b;
//        }));
//    }
}
