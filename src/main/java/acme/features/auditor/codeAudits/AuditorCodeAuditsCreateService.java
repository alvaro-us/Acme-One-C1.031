
package acme.features.auditor.codeAudits;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Audit.CodeAudits;
import acme.entities.Audit.EnumType;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditsCreateService extends AbstractService<Auditor, CodeAudits> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudits object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new CodeAudits();
		object.setCode("");
		object.setExecutionDate(moment);
		object.setType(EnumType.DYNAMIC);
		object.setProposedCorrectiveActions("");
		object.setMark(0.);
		object.setLink("");

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudits object) {
		assert object != null;

		super.bind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "mark", "link");
	}

	@Override
	public void validate(final CodeAudits object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
	}

	@Override
	public void perform(final CodeAudits object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudits object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "mark", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
