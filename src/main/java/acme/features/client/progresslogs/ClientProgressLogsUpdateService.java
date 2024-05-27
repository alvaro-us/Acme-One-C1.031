
package acme.features.client.progresslogs;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientProgressLogsUpdateService extends AbstractService<Client, ProgressLogs> {

	@Autowired
	protected ClientProgressLogsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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

		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "client.progressLogs.form.error.published");

		if (!Objects.equals(object.getRecordId(), pl1.getRecordId())) {
			Boolean codeDuplicated = this.repository.findProgressLogsByRecordId(object.getRecordId()) == null;
			super.state(codeDuplicated, "recordId", "client.progressLogs.form.error.codeDuplicated");

		}
		if (!super.getBuffer().getErrors().hasErrors("registrationMoment")) {
			Date maxDate = new Date(4102441199000L); // 2099/12/31 23:59
			super.state(MomentHelper.isBeforeOrEqual(object.getRegistrationMoment(), maxDate), "registrationMoment", "client.progressLogs.form.error.moment");
		}
		if (!super.getBuffer().getErrors().hasErrors("registrationMoment"))
			super.state(MomentHelper.isAfter(object.getRegistrationMoment(), object.getContract().getInstationMoment()), "registrationMoment", "client.progressLogs.form.error.moment2");

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
