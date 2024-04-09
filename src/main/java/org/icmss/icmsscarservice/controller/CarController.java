package org.icmss.icmsscarservice.controller;

import lombok.RequiredArgsConstructor;
import org.icmss.icmsscarservice.domain.request.CarCreateRequest;
import org.icmss.icmsscarservice.domain.request.CarSearchRequest;
import org.icmss.icmsscarservice.domain.request.CarUpdateRequest;
import org.icmss.icmsscarservice.domain.response.CarDetailsResponse;
import org.icmss.icmsscarservice.domain.response.CarListResponse;
import org.icmss.icmsscarservice.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @PreAuthorize("hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> createCar(@Valid @RequestBody CarCreateRequest request) {
        carService.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{carId}")
    @PreAuthorize("hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> updateCar(@PathVariable String carId, @Valid @RequestBody CarUpdateRequest request) {
        carService.updateCar(carId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{carId}")
    @PreAuthorize("hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
        carService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{carId}")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CarDetailsResponse> getCarDetails(@PathVariable String carId) {
        CarDetailsResponse response = carService.getCarDetails(carId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarListResponse>> getAllCars(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<CarListResponse> response = carService.getAllCars(page, size);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<CarListResponse>> searchCars(@Valid CarSearchRequest request) {
        List<CarListResponse> response = carService.searchCars(request);
        return ResponseEntity.ok(response);
    }
}
