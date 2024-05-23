
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoice.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

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
	public void validate(final Sponsorship object) {
		assert object != null;

		Collection<Invoice> invoices = this.repository.findInvoiceBySponsorshipId(object.getId());
		double invoicesDouble = 0.;
		boolean arePublished = true;

		for (Invoice invoice : invoices) {
			invoicesDouble += invoice.getTotalAmount().getAmount();
			if (invoice.isDraftMode())
				arePublished = false;
		}

		super.state(arePublished, "*", "sponsor.sponsorship.form.error.publishedInvoices");
		super.state(!invoices.isEmpty(), "*", "sponsor.sponsorship.form.error.noInvoices");

		if (!invoices.isEmpty()) {

			Money sponsorshipMoney = object.getAmount();
			boolean correct = Objects.equals(invoicesDouble, sponsorshipMoney.getAmount());
			super.state(correct, "amount", "sponsor.sponsorship.form.error.correctAmount");
		}

	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "draftMode", "project");

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjects();

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project", "draftMode");

		dataset.put("types", choices);
		dataset.put("projects", choicesProjects);
		super.getResponse().addData(dataset);
	}

}
