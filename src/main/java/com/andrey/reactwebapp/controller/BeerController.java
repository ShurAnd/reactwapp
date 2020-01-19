package com.andrey.reactwebapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.andrey.reactwebapp.domain.Beer;
import com.andrey.reactwebapp.persistence.BeerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/beer")
public class BeerController{

	private BeerRepository beerRepo;
	
	@Autowired
	public BeerController(BeerRepository beerRepo) {
		this.beerRepo = beerRepo;
	}
	
	@GetMapping
	public Flux<Beer> getAllBeer(){
		Flux<Beer> beers = beerRepo.findAll();
		
		return beers;
	}
	
	@GetMapping("/{id}")
	public Mono<Beer> getBeerById(@PathVariable("id") Long id){
		
		return beerRepo.findById(id);
	}
	
	@PutMapping("/{id}")
	public Mono<Beer> putBeer(@RequestBody Beer beer, @PathVariable("id") Long id){
		beerRepo.save(beer);
		
		return Mono.just(beer);
	}
	
	@PatchMapping("/{id}")
	public Mono<Beer> patchBeer(@RequestBody Beer beer, @PathVariable("id") Long id){
		beerRepo.findById(id).map((oldBeer) -> {
			if (beer.getName() != null) oldBeer.setName(beer.getName());
			if (beer.getPrice() != null) oldBeer.setPrice(beer.getPrice());
			if (beer.getType() != null) oldBeer.setType(beer.getType());
			
			return beerRepo.save(oldBeer);
		});
		
		return beerRepo.findById(id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Mono<Beer>> deleteBeer(@PathVariable("id") Long id){
		beerRepo.deleteById(id);
		
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@PostMapping
	public ResponseEntity<Mono<Beer>> putNewBeer(@RequestBody Beer beer, UriComponentsBuilder ucb){
		HttpHeaders headers = new HttpHeaders();
		Mono<Beer> newBeer = beerRepo.save(beer);
		headers.setLocation(URI.create(ucb.path("/beers").path("/"+newBeer.block().getId()).toString()));
		
		return new ResponseEntity<>(newBeer, headers, HttpStatus.CREATED);
		
	}
}
