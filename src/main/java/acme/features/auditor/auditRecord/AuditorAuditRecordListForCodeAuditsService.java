
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Audit.AuditRecord;
import acme.entities.Audit.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListForCodeAuditsService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Auditor, AuditRecord> ---------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("codeAuditId", int.class);
		CodeAudits codeAudit = this.repository.findCodeAuditbyId(id);
		Auditor auditor = codeAudit == null ? null : codeAudit.getAuditor();
		boolean status = codeAudit != null && super.getRequest().getPrincipal().hasRole(auditor);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		Collection<AuditRecord> objects = this.repository.findAuditRecordsByCodeAuditId(codeAuditId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		Dataset dataset = super.unbind(object, "code", "furtherInformation", "mark", "published", "auditPeriodStart", "auditPeriodEnd");
		super.getResponse().addData(dataset);
	}
}
