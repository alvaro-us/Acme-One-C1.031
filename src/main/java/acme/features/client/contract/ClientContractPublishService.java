
package acme.features.client.contract;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;
import acme.entities.configuration.CurrencyService;
// import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.roles.client.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected CurrencyService			currencyService;

	//@Autowired
	//protected AuxiliarService			auxiliarService;

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.code");
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

		final Collection<Contract> contracts1 = this.repository.findContractsFromProject(object.getProject().getId());
		super.state(!contracts1.isEmpty(), "*", "manager.project.form.error.noContracts");

	}

	@Override
	public void perform(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		dataset = super.unbind(object, "code", "instationMoment", "providerName", "customerName", "goals", "budget", "project", "client", "published");
		dataset.put("projectTitle", object.getProject().getCode());
		super.getResponse().addData(dataset);
	}
}
