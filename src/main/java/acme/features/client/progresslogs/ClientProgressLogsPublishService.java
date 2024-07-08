
package acme.features.client.progresslogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientProgressLogsPublishService extends AbstractService<Client, ProgressLogs> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		Contract contract;
		ProgressLogs progressLog;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractByProgressLogId(progressLogId);
		progressLog = this.repository.findOneProgressLogById(progressLogId);
		status = contract != null && !progressLog.isPublished() && contract.getClient().getUserAccount().getUsername().equals(super.getRequest().getPrincipal().getUsername()) && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
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
		ProgressLogs object2;
		int id;

		id = super.getRequest().getData("id", int.class);
		object2 = this.repository.findProgressLogsById(id);
		object.setContract(object2.getContract());
		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsable");
	}

	@Override
	public void validate(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLogs existing;
			existing = this.repository.findOneProgressLogsByRecordId(object.getRecordId());
			final ProgressLogs progressLog2 = object.getRecordId().equals("") || object.getRecordId() == null ? null : this.repository.findOneProgressLogById(object.getId());
			super.state(existing == null || progressLog2.equals(existing), "recordId", "client.progressLog.form.error.duplicated");
		}

	}

	@Override
	public void perform(final ProgressLogs object) {
		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsable", "contract", "published");

		dataset.put("contractTitle", object.getContract().getCode());
		super.getResponse().addData(dataset);
	}
}
