package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {
	
	@GetMapping("/filtering")
	public MappingJacksonValue filtering() {
		SomeBean someBean = new SomeBean("value1", "value2", "value3");
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1","field3");
		return customizedFilterDefinition(mappingJacksonValue, filter);
	}
	
	@GetMapping("/filtering-list")
	public MappingJacksonValue filteringList() {
		SomeBean someBean1 = new SomeBean("value1", "value2", "value3");
		SomeBean someBean2 = new SomeBean("value4", "value5", "value6");		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(Arrays.asList(someBean1,someBean2));
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2","field3");
		return customizedFilterDefinition(mappingJacksonValue, filter);			
	}

	private MappingJacksonValue customizedFilterDefinition(MappingJacksonValue mappingJacksonValue,
			SimpleBeanPropertyFilter filter) {
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
	return mappingJacksonValue;
	}
	
	
}
