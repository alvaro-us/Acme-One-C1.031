
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.CurrencyService;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected CurrencyService			currencyService;

	// AbstractService interface -------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getClient().getUserAccount().getId() == userAccountId && !object.isPublished());
	}

	@Override
	public void load() {
		Contract object;
		object = new Contract();
		object.setPublished(false);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");

		Contract object2;
		int id;

		id = super.getRequest().getData("id", int.class);
		object2 = this.repository.findContractById(id);
		object.setProject(object2.getProject());
		object.setClient(object2.getClient());
		super.bind(object, "code", "providerName", "instationMoment", "customerName", "goals", "budget");
	}

	@Override
	public void validate(final Contract object) {

		if (object == null)
			throw new IllegalArgumentException("No object found");

		boolean totalAmountBelowProjectCost;
		String currency = object.getProject().getCost().getCurrency();
		Collection<Contract> contracts = this.repository.findContractsFromProject(object.getId());
		double sum = 0.;
		for (Contract contract : contracts)
			if (contract.getId() != object.getId())
				sum += this.currencyService.changeCurrencyTo(contract.getBudget(), currency).getAmount();

		totalAmountBelowProjectCost = sum + this.currencyService.changeCurrencyTo(object.getBudget(), currency).getAmount() <= object.getProject().getCost().getAmount();
		super.state(totalAmountBelowProjectCost, "budget", "client.contract.form.error.totalAmountBelow");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0., "budget", "client.contract.form.error.negative-price");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			final Contract contract2 = object.getCode().equals("") || object.getCode() == null ? null : this.repository.findContractById(object.getId());
			super.state(existing == null || contract2.equals(existing), "code", "client.contract.form.error.code");
		}
	}

	@Override
	public void perform(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		final SelectChoices choicesP = new SelectChoices();
		Collection<Project> projects;
		projects = this.repository.findPublishedProjects();
		for (final Project p : projects)
			choicesP.add(Integer.toString(p.getId()), p.getCode() + " - " + p.getTitle(), false);
		dataset = super.unbind(object, "code", "instationMoment", "providerName", "customerName", "goals", "budget", "project", "client", "published");
		dataset.put("projects", choicesP);

		dataset.put("projectTitle", object.getProject().getCode());
		super.getResponse().addData(dataset);
	}
}
