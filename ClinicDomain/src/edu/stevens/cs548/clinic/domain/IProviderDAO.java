package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;

public interface IProviderDAO {
	public static class ProviderExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderExn (String msg) {
			super(msg);
		}
	}

	public long addProvider (Provider prov) throws ProviderExn;
	
	public Provider getProviderByNPI (String NPI) throws ProviderExn;
	
	public Provider getProvider (long id) throws ProviderExn;
	
	public void deleteProviders();

	void deleteProvider(Provider provider) throws ProviderExn;
}
