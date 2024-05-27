
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

		Sponsorship sponsorship;
		sponsorship = this.repository.findSponsorshipById(object.getId());

		Collection<Invoice> invoices = this.repository.findInvoiceBySponsorshipId(object.getId());
		boolean arePublished;
		Double totalAmount = 0.;
		for (Invoice invoice : invoices)
			totalAmount += invoice.getTotalAmount().getAmount();

		long draftInvoiceCount = this.repository.countDraftInvoicesBySponsorshipId(object.getId());

		arePublished = draftInvoiceCount == 0;

		boolean noUpdate = sponsorship.getCode().equals(object.getCode()) && sponsorship.getDurationStart().compareTo(object.getDurationStart()) == 0 && sponsorship.getDurationEnd().compareTo(object.getDurationEnd()) == 0
			&& sponsorship.getAmount().getAmount().equals(object.getAmount().getAmount()) && sponsorship.getAmount().getCurrency().equals(object.getAmount().getCurrency()) && sponsorship.getType().equals(object.getType())
			&& sponsorship.getEmail().equals(object.getEmail()) && sponsorship.getLink().equals(object.getLink()) && sponsorship.getProject().getCode().equals(object.getProject().getCode());

		super.state(arePublished, "*", "sponsor.sponsorship.form.error.publishedInvoices");
		super.state(!invoices.isEmpty(), "*", "sponsor.sponsorship.form.error.noInvoices");
		super.state(noUpdate, "*", "sponsor.sponsorship.form.error.hasToUpdate");

		if (!invoices.isEmpty()) {

			Money sponsorshipMoney = object.getAmount();
			boolean correct = Objects.equals(totalAmount, sponsorshipMoney.getAmount());
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
