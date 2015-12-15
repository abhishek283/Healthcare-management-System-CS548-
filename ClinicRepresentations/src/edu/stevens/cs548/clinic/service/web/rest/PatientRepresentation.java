package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.PatientType;
@XmlRootElement
public class PatientRepresentation extends PatientType {
	
	private PatientDtoFactory patientDtoFactory;
	
	public List<LinkType> getLinkTreatments(){
		return this.getTreatments();
	}
	
	public PatientRepresentation () {
		super();
		this.patientDtoFactory = new PatientDtoFactory();
	}
	
	public PatientRepresentation(PatientDto dto, UriInfo uriInfo){
		this();
		this.id = getPatientLink(dto.getId(), uriInfo);
		this.patientId =  dto.getPatientId();
		this.name = dto.getName();
		this.dob = dto.getDob();
		this.age = PatientFactory.computeAge(dto.getDob());
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> links = this.getTreatments();
		for (long t : dto.getTreatments()) {
			links.add(TreatmentRepresentation.getTreatmentLink(t, uriInfo));
		}
	}

	public static LinkType getPatientLink(long id, UriInfo uriInfo) {
		// TODO Auto-generated method stub
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("patient").path("{id}");
		String patientURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(patientURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	public PatientDto getPatientDto() {
		PatientDto p = patientDtoFactory.createPatientDto();
		p.setId(Representation.getId(this.id));
		p.setPatientId(this.patientId);
		p.setName(this.name);
		p.setDob(this.dob);
		return p;
	}
}
