
package acme.features.auditor.codeAudits;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Audit.AuditRecordType;
import acme.entities.Audit.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditsListPublishedService extends AbstractService<Auditor, CodeAudits> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<CodeAudits> objects;
		int auditorId;
		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findCodeAuditByAuditorId(auditorId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final CodeAudits object) {
		assert object != null;
		Dataset dataset;
		List<AuditRecordType> marks;
		marks = this.repository.findMarksByCodeAuditId(object.getId()).stream().toList();
		dataset = super.unbind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "optionalLink", "published");
		dataset.put("mark", this.repository.averageMark(marks));
		dataset.put("project", object.getProject().getCode());

		super.getResponse().addData(dataset);
	}
}
