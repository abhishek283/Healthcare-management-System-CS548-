package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.SurgeryType;

public class SurgeryRepresentation extends SurgeryType {
	private TreatmentDtoFactory treatmentDtoFactory;

	public SurgeryRepresentation () {
		super();
		this.treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public SurgeryRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.dateOfSurgery = dto.getSurgeryTreatment().getDateOfSurgery();
		this.surgeonName=dto.getSurgeryTreatment().getSurgeonName();
		}
	
	
	public static LinkType getSurgeryLink(String diag, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("surgerytreatment").path("{diagnosys}");
		String surgeryURI = ub.build(diag).toString();

		LinkType link = new LinkType();
		link.setUrl(surgeryURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}

}
