
package acme.features.manager.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.CurrencyService;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerProjectShowService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerProjectRepository	repository;

	@Autowired
	protected CurrencyService						service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		Project object;
		Manager manager;
		int id;
		int id1;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProjectById(id);
		id1 = super.getRequest().getPrincipal().getAccountId();

		manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(Manager.class) && object.getManager().equals(manager) && object.getManager().getUserAccount().getId() == id1;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");
		Money amountBase = this.service.changeCurrencyToBase(object.getCost());
		dataset.put("amountBase", amountBase);

		super.getResponse().addData(dataset);
	}

}
