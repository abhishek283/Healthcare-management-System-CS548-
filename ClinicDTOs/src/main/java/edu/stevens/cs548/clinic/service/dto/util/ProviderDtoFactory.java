package edu.stevens.cs548.clinic.service.dto.util;

import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;

public class ProviderDtoFactory {
ObjectFactory factory;
	
	public ProviderDtoFactory() {
		factory = new ObjectFactory();
	}
	
	
	public ProviderDto createProviderDto () {
		return factory.createProviderDto();
	}
	
	public ProviderDto createProviderDto (Provider p) {
		ProviderDto d = factory.createProviderDto();
		/*
		 * TODO: Initialize the fields of the DTO.
		 */
		//d.setProviderId(p.getProviderId());
		d.setNpi(p.getNPI());
		d.setName(p.getName());
		d.setSpecialization(p.getSpecialization());
		//d.setTreatments(p.getTrmtIds());
		return d;
	}
}
