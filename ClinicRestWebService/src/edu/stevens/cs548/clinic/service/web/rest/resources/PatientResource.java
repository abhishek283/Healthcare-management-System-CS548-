package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

@Path("/patient")
@RequestScoped
public class PatientResource {
   
    /**
     * Default constructor. 
     */
    public PatientResource() {
        // TODO Auto-generated constructor stub
    }

    @Context
    private UriInfo context;
    
    @Inject
    private IPatientServiceLocal patientService;
    
    private PatientDtoFactory patientDtoFactory=new PatientDtoFactory();
    
    final static Logger logger = Logger.getLogger(PatientResource.class.getCanonicalName());

    @GET
    @Path("site")
    @Produces("text/plain")
    public String getSiteInfo() {
    	return patientService.siteInfo();
    }
    /**
     * Retrieves representation of an instance of Patient
     * @return an instance of PatientType
     */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public PatientRepresentation getPatient(@PathParam("id") String id) {
    	try{
    		long key=Long.parseLong(id);
    		PatientDto patientDto=patientService.getPatientByDbId(key);
    		PatientRepresentation patientRep=new PatientRepresentation(patientDto,context);
    		return patientRep;
    		
    	}
    	catch(PatientServiceExn e){
    		throw new WebApplicationException("Not Found",403);
    	}     
    }

    @GET
    @Path("byPatientId")
    @Produces("application/xml")
    public PatientRepresentation getPatientByPatientId(@QueryParam("id") String id) {
    	try{
    		long pid=Long.parseLong(id);
    		PatientDto patientDto=patientService.getPatientByPatId(pid);
    		PatientRepresentation patientRep=new PatientRepresentation(patientDto,context);
    		return patientRep;
    		
    	}
    	catch(PatientServiceExn e){
    		throw new WebApplicationException("Not Found",403);
    	}     
    }
   
    @GET
    @Path("PatientNameDob")
    @Produces("application/xml")
    public PatientRepresentation[] getPatientByNameDob(@QueryParam("name") String name,
    												@QueryParam("dob") String dob) {
    	Date birthdate=DatatypeConverter.parseDate(dob).getTime();
		PatientDto[] patientDto=patientService.getPatientByNameDob(name, birthdate);
		PatientRepresentation[] patientReps=new PatientRepresentation[patientDto.length];
		for(int i=0;i<patientReps.length;i++){
		patientReps[i]=new PatientRepresentation(patientDto[i],context);
		}
		return patientReps;     
    }
    
    @POST
    @Consumes("application/xml")
    public Response addPatient(PatientRepresentation patientRep){
       	try {
    	PatientDto dto = patientDtoFactory.createPatientDto();
		dto.setPatientId(patientRep.getPatientId());
		dto.setName(patientRep.getName());
		dto.setDob(patientRep.getDob());
		dto.setAge(patientRep.getAge());
		long id = patientService.addPatient(dto);
		UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
		URI url = ub.build(Long.toString(id));
		return Response.created(url).build();
		} 
       	catch (PatientServiceExn e) {
			throw new WebApplicationException("Not Found",403);
		}
    }
    
    @GET
    @Path("patientTreatment")
    @Produces("application/xml")
    public TreatmentRepresentation getPatientTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
    	try {
    		TreatmentDto treatment = patientService.getTreatment(Long.parseLong(id), Long.parseLong(tid)); 
    		TreatmentRepresentation treatmentRep = new TreatmentRepresentation(treatment, context);
    		return treatmentRep;
    	} catch (PatientServiceExn e) {
    		throw new WebApplicationException("Not Found",403);
    	}
    }
}
