package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.DrugType;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;

public class DrugTreatmentRepresentation extends DrugType {
	private TreatmentDtoFactory treatmentDtoFactory;
	
	public DrugTreatmentRepresentation () {
		super();
		this.treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public DrugTreatmentRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.drugName = dto.getDrugTreatment().getDrugName();
		this.dosage =  dto.getDrugTreatment().getDosage();
		}
	
	
	public static LinkType getDrugLink(String diag, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("drugtreatment").path("{diagnosys}");
		String drugURI = ub.build(diag).toString();
		LinkType link = new LinkType();
		link.setUrl(drugURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
}
