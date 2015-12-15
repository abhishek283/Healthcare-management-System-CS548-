package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.RadiologyType;

public class RadiologyRepresentation extends RadiologyType {
private TreatmentDtoFactory treatmentDtoFactory;
	
	public RadiologyRepresentation () {
		super();
		this.treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public RadiologyRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.treatmentDates = dto.getRadiologyTreatment().getTreatmentDates();
		this.radiologistName=dto.getRadiologyTreatment().getRadiologistName();
		}
	
	
	public static LinkType getRadiologyLink(String diag, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("radiologytreatment").path("{diagnosys}");
		String radiologyURI = ub.build(diag).toString();

		LinkType link = new LinkType();
		link.setUrl(radiologyURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
}
