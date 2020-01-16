package com.andrey.reactwebapp.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.andrey.reactwebapp.domain.Beer;

@Repository
public interface BeerRepository extends ReactiveMongoRepository<Beer, Long>{
	Beer findByName(String name);
}
