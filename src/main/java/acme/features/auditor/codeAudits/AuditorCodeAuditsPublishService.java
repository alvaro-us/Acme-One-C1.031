
package acme.features.auditor.codeAudits;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Audit.AuditRecordType;
import acme.entities.Audit.CodeAudits;
import acme.entities.Audit.EnumType;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditsPublishService extends AbstractService<Auditor, CodeAudits> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		int codeAuditId = super.getRequest().getData("id", int.class);
		CodeAudits codeAudit = this.repository.findOneCodeAuditsById(codeAuditId);
		int auditor = super.getRequest().getPrincipal().getActiveRoleId();
		boolean status = auditor == codeAudit.getAuditor().getId() && codeAudit.isPublished() == false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		CodeAudits object = this.repository.findOneCodeAuditsById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudits object) {
		assert object != null;
		super.bind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "link", "project");
	}

	@Override
	public void validate(final CodeAudits object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("mark")) {
			List<AuditRecordType> marks;

			marks = this.repository.findMarksByCodeAuditId(object.getId()).stream().toList();
			AuditRecordType nota = this.repository.averageMark(marks);

			super.state(nota == AuditRecordType.C || nota == AuditRecordType.B || nota == AuditRecordType.A || nota == AuditRecordType.APLUS, "mark", "auditor.codeaudit.form.error.mark");
		}
	}

	@Override
	public void perform(final CodeAudits object) {
		assert object != null;
		object.setPublished(!object.isPublished());
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudits object) {
		assert object != null;
		Dataset dataset;
		List<AuditRecordType> marks = this.repository.findMarksByCodeAuditId(object.getId()).stream().toList();
		Collection<Project> allProjects = this.repository.findAllProjects();
		SelectChoices choices = SelectChoices.from(EnumType.class, object.getType());
		SelectChoices choices2 = SelectChoices.from(allProjects, "code", (Project) allProjects.toArray()[0]);
		dataset = super.unbind(object, "code", "executionDate", "type", "proposedCorrectiveActions", "optionalLink", "published");
		dataset.put("mark", this.repository.averageMark(marks));
		dataset.put("type", choices);
		dataset.put("projects", choices2);

		super.getResponse().addData(dataset);

	}

}
