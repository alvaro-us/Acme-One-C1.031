
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Audit.AuditRecord;
import acme.entities.Audit.AuditRecordType;
import acme.entities.Audit.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Auditor, AuditRecord> ---------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		AuditRecord auditRecord = this.repository.findOneAuditRecordById(id);
		boolean status = auditRecord != null && super.getRequest().getPrincipal().hasRole(auditRecord.getCodeAudits().getAuditor());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		AuditRecord object = this.repository.findOneAuditRecordById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;
		int codeAuditId = super.getRequest().getData("codeAudit", int.class);
		CodeAudits codeAudit = this.repository.findCodeAuditbyId(codeAuditId);
		object.setCodeAudits(codeAudit);
		super.bind(object, "code", "furtherInformation", "mark", "auditPeriodStart", "auditPeriodEnd");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord auditRecordSameCode = this.repository.findAuditRecordByCode(object.getCode());
			if (auditRecordSameCode != null)
				super.state(auditRecordSameCode.getId() == object.getId(), "code", "validation.auditrecord.error.duplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("auditPeriodEnd")) {
			Date auditPeriodStart;
			Date auditPeriodEnd;
			auditPeriodStart = object.getAuditPeriodStart();
			auditPeriodEnd = object.getAuditPeriodEnd();
			super.state(MomentHelper.isLongEnough(auditPeriodStart, auditPeriodEnd, 1, ChronoUnit.HOURS) && auditPeriodEnd.after(auditPeriodStart), "auditEndTime", "validation.auditrecord.moment.minimum-one-hour");
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		Dataset dataset;
		Collection<CodeAudits> allCodeAudits = this.repository.findAllCodeAudits();
		SelectChoices codeAudits = SelectChoices.from(allCodeAudits, "code", object.getCodeAudits());
		SelectChoices choices = SelectChoices.from(AuditRecordType.class, object.getMark());
		dataset = super.unbind(object, "code", "published", "furtherInformation", "mark", "auditPeriodStart", "auditPeriodEnd");
		dataset.put("codeAudits", codeAudits.getSelected().getKey());
		dataset.put("codeaudits", codeAudits);
		dataset.put("marks", choices);
		super.getResponse().addData(dataset);
	}
}
