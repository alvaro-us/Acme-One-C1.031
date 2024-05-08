
package acme.features.client.contract;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.CurrencyService;
// import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Service
public class ClientContractShowService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected CurrencyService			currencyService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getClient().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		SelectChoices choicesP;
		Collection<Project> projects;
		projects = this.repository.findAllProjects();
		choicesP = SelectChoices.from(projects, "code", object.getProject());
		Money budgetBase = this.currencyService.changeCurrencyToBase(object.getBudget());

		dataset = super.unbind(object, "id", "code", "instationMoment", "providerName", "customerName", "goals", "budget", "published", "project", "client");
		final List<ProgressLogs> progressLogs = (List<ProgressLogs>) this.repository.findProgressLogsByContract(object.getId());
		dataset.put("hasProgressLogs", !progressLogs.isEmpty());
		dataset.put("projectTitle", object.getProject().getCode());
		dataset.put("projects", choicesP);
		dataset.put("budgetBase", budgetBase);
		super.getResponse().addData(dataset);
	}
}
