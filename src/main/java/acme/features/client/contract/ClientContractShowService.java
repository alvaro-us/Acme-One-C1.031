
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Service
public class ClientContractShowService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Contract contract;
		Client client;
		Date currentMoment;

		masterId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(masterId);
		client = contract == null ? null : contract.getClient();
		currentMoment = MomentHelper.getCurrentMoment();
		status = super.getRequest().getPrincipal().hasRole(client) || contract != null && contract.isPublished() && MomentHelper.isAfter(contract.getInstationMoment(), currentMoment);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;
		SelectChoices choicesP;
		Collection<Project> projects = this.repository.findAllProjects();

		Dataset dataset;
		choicesP = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instationMoment", "providerName", "customerName", "goals", "budget", "published");
		dataset.put("projects", choicesP);
		super.getResponse().addData(dataset);
	}

}
