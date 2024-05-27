
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.services.AbstractService;
// import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientContractDeleteService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository repository;

	//@Autowired
	//protected AuxiliarService			auxiliarService;

	// AbstractService interface ----------------------------------------------


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
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;
		super.bind(object, "id", "code", "instationMoment", "providerName", "customerName", "goals", "budget");
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		final Collection<ProgressLogs> progressLogs = this.repository.findProgressLogsByContract(object.getId());
		for (final ProgressLogs pl : progressLogs)
			this.repository.delete(pl);
		this.repository.delete(object);
	}
	/*
	 * @Override
	 * public void unbind(final Contract object) {
	 * assert object != null;
	 * Dataset dataset;
	 * dataset = super.unbind(object, "code", "instationMoment", "providerName", "customerName", "goals", "budget");
	 * //dataset.put("money", this.auxiliarService.changeCurrency(object.getBudget()));
	 * super.getResponse().addData(dataset);
	 * }
	 */
}
