package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {

	private EntityManager em;
	private ITreatmentDAO treatmentDAO;
	
	public ProviderDAO(EntityManager em) {
		// TODO Auto-generated constructor stub
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@Override
	public long addProvider(Provider prov) throws ProviderExn {
		// TODO Auto-generated method stub
		String npi = prov.getNPI();
		Query query = em.createNamedQuery("CountProviderByNPI").setParameter("npi", npi);
		Long numExisting = (Long) query.getSingleResult();
		if (numExisting < 1) {
			// TODO add to database (and sync with database to generate primary key)
			// Don't forget to initialize the patient aggregate with a treatment DAO
			em.persist(prov);
			prov.setTreatmentDAO(this.treatmentDAO);
			return prov.getProviderId();			
		} else {
			throw new ProviderExn("Insertion: Provider with npi (" + npi + ") already exists.");
		}
	}

	@Override
	public Provider getProviderByNPI(String NPI) throws ProviderExn {
		// TODO Auto-generated method stub
		TypedQuery<Provider> query=em.createNamedQuery("SearchProviderByNPI",Provider.class).setParameter("npi", NPI);
		List<Provider> providers=query.getResultList();
		if(providers.size()>1){
			throw new ProviderExn("Duplicate records found for Provider NPI :"+NPI);
		}
		else if(providers.size()<1){
			throw new ProviderExn("NO record found for NPI :"+NPI);
		}
		else{
			Provider p = providers.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public Provider getProvider(long Providerid) throws ProviderExn {
		// TODO Auto-generated method stub
		Provider p=em.find(Provider.class, Providerid);
		if(p==null){
			throw new ProviderExn("Provider not found,primary key "+Providerid);
		}
		else{
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
		
	}

	@Override
	public void deleteProviders() {
		// TODO Auto-generated method stub
		Query update = em.createNamedQuery("RemoveAllProviders");
		update.executeUpdate();

	}
	
	@Override
	public void deleteProvider(Provider provider) throws ProviderExn {
		// TODO Auto-generated method stub
		em.remove(provider);
		
	}

}
