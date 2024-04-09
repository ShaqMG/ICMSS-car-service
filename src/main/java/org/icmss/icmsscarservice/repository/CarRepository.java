package org.icmss.icmsscarservice.repository;

import org.icmss.icmsscarservice.domain.document.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ElasticsearchRepository<Car, Long> {
}
