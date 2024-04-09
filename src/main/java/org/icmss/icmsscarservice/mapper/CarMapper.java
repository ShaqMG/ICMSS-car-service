package org.icmss.icmsscarservice.mapper;

import org.icmss.icmsscarservice.domain.document.Car;
import org.icmss.icmsscarservice.domain.request.CarCreateRequest;
import org.icmss.icmsscarservice.domain.request.CarUpdateRequest;
import org.icmss.icmsscarservice.domain.response.CarDetailsResponse;
import org.icmss.icmsscarservice.domain.response.CarListResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static Car toEntity(CarCreateRequest request) {
        Car car = new Car();
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setPrice(request.getPrice());
        car.setMileage(request.getMileage());
        car.setVin(request.getVin());
        car.setDescription(request.getDescription());
        car.setLocationCountry(request.getLocationCountry());
        car.setNumberOfDoors(request.getNumberOfDoors());
        car.setNumberOfSeats(request.getNumberOfSeats());
        car.setCarType(request.getCarType());
        car.setGearboxType(request.getGearboxType());
        car.setSteeringWheelPosition(request.getSteeringWheelPosition());
        car.setImagesUrls(request.getImageUrls());
        return car;
    }

    public static void updateEntity(CarUpdateRequest request, Car car) {
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setPrice(request.getPrice());
        car.setMileage(request.getMileage());
        car.setVin(request.getVin());
        car.setDescription(request.getDescription());
        car.setLocationCountry(request.getLocationCountry());
        car.setNumberOfDoors(request.getNumberOfDoors());
        car.setNumberOfSeats(request.getNumberOfSeats());
        car.setCarType(request.getCarType());
        car.setGearboxType(request.getGearboxType());
        car.setSteeringWheelPosition(request.getSteeringWheelPosition());
        car.setImagesUrls(request.getImageUrls());
    }

    public static CarDetailsResponse toDetailsResponse(Car car) {
        CarDetailsResponse response = new CarDetailsResponse();
        response.setId(car.getId());
        response.setDealerId(car.getDealerId());
        response.setBrand(car.getBrand());
        response.setModel(car.getModel());
        response.setYear(car.getYear());
        response.setColor(car.getColor());
        response.setPrice(car.getPrice());
        response.setMileage(car.getMileage());
        response.setVin(car.getVin());
        response.setDescription(car.getDescription());
        response.setLocationCountry(car.getLocationCountry());
        response.setNumberOfDoors(car.getNumberOfDoors());
        response.setNumberOfSeats(car.getNumberOfSeats());
        response.setCarType(car.getCarType());
        response.setGearboxType(car.getGearboxType());
        response.setSteeringWheelPosition(car.getSteeringWheelPosition());
        response.setImageUrls(car.getImagesUrls());

        return response;
    }

    public static CarListResponse toListResponse(Car car) {
        CarListResponse response = new CarListResponse();
        response.setId(car.getId());
        response.setBrand(car.getBrand());
        response.setModel(car.getModel());
        response.setYear(car.getYear());
        response.setPrice(car.getPrice());
        response.setCarType(car.getCarType());
        response.setGearboxType(car.getGearboxType());
        response.setSteeringWheelPosition(car.getSteeringWheelPosition());

        List<String> imagesUrls = car.getImagesUrls();
        if (imagesUrls != null && !imagesUrls.isEmpty()) {
            response.setImageUrl(imagesUrls.get(0));
        }

        return response;
    }
}