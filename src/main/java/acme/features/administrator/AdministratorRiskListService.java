
package acme.features.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.risks.Risk;

@Service
public class AdministratorRiskListService extends AbstractService<Administrator, Risk> {

	@Autowired
	protected AdministratorRiskRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Risk> objects;
		objects = this.repository.findAllRisk();
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "value", "description", "link");
		super.getResponse().addData(dataset);
	}

}
