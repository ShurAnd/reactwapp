package com.andrey.reactwebapp.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Beer {

	private Long id;
	private String name;
	private Type type;
	private BigDecimal price;
	
	public static enum Type{
		LIGHT, DARK, BROWN, AMBER
	}
}
