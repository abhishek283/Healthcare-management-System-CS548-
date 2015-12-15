package edu.stevens.cs548.clinic.service.dto.util;

import java.util.Calendar;
import java.util.Date;

import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;



public class PatientDtoFactory {
	
	ObjectFactory factory;
	
	public PatientDtoFactory() {
		factory = new ObjectFactory();
	}
	
	
	public PatientDto createPatientDto () {
		return factory.createPatientDto();
	}
	
	public PatientDto createPatientDto (Patient p) {
		PatientDto d = factory.createPatientDto();
		/*
		 * TODO: Initialize the fields of the DTO.
		 */
		
		
		d.setId(p.getId());
		d.setDob(p.getBirthDate());
		d.setName(p.getName());
		d.setPatientId(p.getPatientId());
		d.setAge(computeAge(p.getBirthDate()));
		return d;
	}
	public static int computeAge(Date dateOfBirth) {
		Calendar dob = Calendar.getInstance();  
		dob.setTime(dateOfBirth);  
		Calendar today = Calendar.getInstance();  
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}
		return age;
	}
	

}
