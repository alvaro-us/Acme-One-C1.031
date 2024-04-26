
package acme.features.auditor.codeAudits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Audit.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditsDeleteService extends AbstractService<Auditor, CodeAudits> {
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
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditsById(id);

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

		boolean status;
		int id, numberCodeAudits;

		id = super.getRequest().getData("id", int.class);
		numberCodeAudits = this.repository.findNumberCodeAuditsByAuditorId(id);

		status = numberCodeAudits == 0;

		super.state(status, "*", "authenticated.auditor.delete.code-audits");
	}

	@Override
	public void perform(final CodeAudits object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final CodeAudits object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "mark", "link");

		super.getResponse().addData(dataset);
	}
}
