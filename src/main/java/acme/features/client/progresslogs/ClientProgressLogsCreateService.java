
package acme.features.client.progresslogs;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Service
public class ClientProgressLogsCreateService extends AbstractService<Client, ProgressLogs> {

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
		int contractId = super.getRequest().getData("masterId", int.class);
		Contract contract = this.repository.findContractById(contractId);

		object.setRegistrationMoment(MomentHelper.getCurrentMoment());
		object.setContract(contract);
		object.setPublished(false);
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

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLogs existing;
			existing = this.repository.findProgressLogsByRecordId(object.getRecordId());
			super.state(existing == null, "recordId", "client.progressLogs.form.error.codeDuplicate");
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
		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsable", "published");

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
