
package acme.features.client.progresslogs;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientProgressLogsUpdateService extends AbstractService<Client, ProgressLogs> {

	@Autowired
	protected ClientProgressLogsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		Contract contract;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractByProgressLogId(progressLogId);
		status = contract != null && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLogs object;
		object = new ProgressLogs();
		object.setPublished(false);
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

		ProgressLogs pl1 = this.repository.findProgressLogsById(object.getId());

		if (!Objects.equals(object.getRecordId(), pl1.getRecordId())) {
			Boolean codeDuplicated = this.repository.findProgressLogsByRecordId(object.getRecordId()) == null;
			super.state(codeDuplicated, "recordId", "client.progressLogs.form.error.codeDuplicated");

		}
		if (!super.getBuffer().getErrors().hasErrors("completeness")) {
			Double existing;
			existing = this.repository.findPublishedProgressLogWithMaxCompletenessPublished(object.getContract().getId());
			if (existing != null)
				super.state(object.getCompleteness() > existing, "completeness", "client.progress-log.form.error.completeness-too-low");
		}

	}

	@Override
	public void perform(final ProgressLogs object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
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
