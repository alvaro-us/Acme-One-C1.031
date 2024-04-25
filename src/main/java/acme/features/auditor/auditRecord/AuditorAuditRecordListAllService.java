
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.AuditRecord;
import acme.entities.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListAllService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	private AuditorAuditRecordRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditRecord> objects;
		Integer id = super.getRequest().getData("id", int.class);
		CodeAudits ca = this.repository.findCodeAuditbyId(id);
		objects = this.repository.findAllAuditRecordByCodeAudits(ca);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "type");

		super.getResponse().addData(dataset);
	}
}
