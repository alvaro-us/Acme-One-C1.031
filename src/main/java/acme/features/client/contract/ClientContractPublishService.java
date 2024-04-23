
package acme.features.client.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.roles.client.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int clientRequestId;
		Client client;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(contractId);
		client = contract == null ? null : contract.getClient();
		clientRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (client != null)
			status = !contract.isPublished() && super.getRequest().getPrincipal().hasRole(client) && contract.getId() == clientRequestId;
		else
			status = false;

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
	public void bind(final Contract object) {
		assert object != null;
		super.bind(object, "code", "providerName", "instantiationMoment", "customerName", "goals", "budget");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		object.setPublished(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "client", "published");

		super.getResponse().addData(dataset);
	}
}
