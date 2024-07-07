
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoice.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int id;
		Sponsorship sponsorship;
		Sponsor sponsor;
		int id1;

		id = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findSponsorshipById(id);
		id1 = super.getRequest().getPrincipal().getAccountId();

		sponsor = sponsorship.getSponsor();
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(Sponsor.class) && sponsor.getUserAccount().getId() == id1;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "draftMode");
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		long nonDraftInvoiceCount = this.repository.countNonDraftInvoicesBySponsorshipId(object.getId());
		boolean canDelete = nonDraftInvoiceCount == 0;

		super.state(canDelete, "*", "sponsor.sponsorship.form.error.cantDelete");
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		Collection<Invoice> invoices;

		invoices = this.repository.findInvoiceBySponsorshipId(object.getId());

		this.repository.deleteAll(invoices);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		Sponsorship sponsorship = this.repository.findSponsorshipById(object.getId());
		dataset = super.unbind(sponsorship, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "draftMode");

		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjectsPublished();

		choicesProjects = SelectChoices.from(projects, "code", object.getProject());
		dataset.put("projects", choicesProjects);
		SelectChoices choices;

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
