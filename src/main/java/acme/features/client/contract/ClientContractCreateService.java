
package acme.features.client.contract;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		Client client;
		List<Project> projects = this.repository.findAllProjects().stream().toList();

		client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Contract();
		object.setPublished(false);
		object.setClient(client);
		object.setProject(projects.get(0));

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "instationMoment", "providerName", "customerName", "goals", "budget");

	}

	@Override
	public void validate(final Contract object) {
		Collection<Contract> listAllContracts = this.repository.findAllContracts();
		Collection<Contract> contractsFiltered = listAllContracts.stream().filter(x -> x.getProject().getId() == object.getProject().getId()).toList();
		double totalAmount = contractsFiltered.stream().map(x -> x.getBudget().getAmount()).collect(Collectors.summingDouble(x -> x));
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;

			existing = this.repository.findOneContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.duplicated");
		}

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		this.repository.save(object);
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
