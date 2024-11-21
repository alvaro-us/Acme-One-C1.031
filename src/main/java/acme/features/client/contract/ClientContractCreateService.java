
package acme.features.client.contract;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.Configuration;
import acme.entities.configuration.CurrencyService;
// import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected CurrencyService			currencyService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		object = new Contract();
		final Client client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setInstationMoment(MomentHelper.getCurrentMoment());
		object.setClient(client);
		object.setPublished(false);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		super.bind(object, "code", "providerName", "instationMoment", "customerName", "goals", "budget", "project");
	}

	@Override
	public void validate(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.code");
		}

		if (object.getProject() == null) {
			super.state(false, "project", "client.contract.form.error.project-null");
			return;
		}

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			boolean totalAmountBelowProjectCost;
			Collection<Contract> contracts = this.repository.findContractsFromProject(object.getId());
			double sum = 0.;
			String currency = object.getProject().getCost().getCurrency();
			for (Contract contract : contracts)

				sum += this.currencyService.changeCurrencyTo(contract.getBudget(), currency).getAmount();

			totalAmountBelowProjectCost = sum + this.currencyService.changeCurrencyTo(object.getBudget(), currency).getAmount() <= object.getProject().getCost().getAmount();

			Configuration config;
			config = this.repository.findConfiguration();
			super.state(totalAmountBelowProjectCost, "budget", "client.contract.form.error.totalAmountBelow");
			super.state(Arrays.asList(config.getAcceptedCurrency().trim().split(",")).contains(object.getBudget().getCurrency()), "budget", "client.contract.error.budget.currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0., "budget", "client.contract.form.error.negative-price");

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
		super.getResponse().addData(dataset);
	}
}
