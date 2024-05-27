
package acme.features.client.progresslogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.services.AbstractService;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientProgressLogsDeleteService extends AbstractService<Client, ProgressLogs> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		ProgressLogs object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProgressLogsById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getContract().getClient().getUserAccount().getId() == userAccountId && !object.isPublished());
	}

	@Override
	public void load() {
		ProgressLogs object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProgressLogsById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsable");
	}

	@Override
	public void validate(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "client.progressLogs.form.error.published");
	}

	@Override
	public void perform(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		this.repository.delete(object);
	}
	/*
	 * @Override
	 * public void unbind(final ProgressLogs object) {
	 * if (object == null)
	 * throw new IllegalArgumentException("No object found");
	 * Dataset dataset;
	 * dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsable", "published", "contract");
	 * super.getResponse().addData(dataset);
	 * }
	 */
}
