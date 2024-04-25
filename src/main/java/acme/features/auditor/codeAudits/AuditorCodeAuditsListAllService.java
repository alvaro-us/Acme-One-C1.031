
package acme.features.auditor.codeAudits;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.CodeAudits;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditsListAllService extends AbstractService<Auditor, CodeAudits> {

	@Autowired
	private AuditorCodeAuditsRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<CodeAudits> objects;
		Principal principal = super.getRequest().getPrincipal();
		Integer userAccountId = principal.getAccountId();
		UserAccount userAccount = this.repository.findOneUserAccountById(userAccountId);
		Auditor auditor = new Auditor();
		auditor.setUserAccount(userAccount);
		objects = this.repository.findAllCodeAuditsByAuditor(auditor);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final CodeAudits object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "executionDate");

		super.getResponse().addData(dataset);
	}
}
