
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.CurrencyService;
import acme.entities.invoice.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorSponsorshipRepository	repository;

	@Autowired
	protected CurrencyService							service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int sponsorshipId;
		Sponsorship sponsorship;
		int id;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		id = super.getRequest().getPrincipal().getAccountId();

		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(Sponsor.class) && sponsorship.getSponsor().getUserAccount().getId() == id;

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

		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link");

	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		int sponsorshipId = super.getRequest().getData("id", int.class);
		Sponsorship sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		Collection<Invoice> invoices = this.repository.findInvoiceBySponsorshipId(sponsorshipId);
		double totalAmount = 0.;

		boolean isAcceptedCurrency = this.service.isAcceptedCurrency(object.getAmount().getCurrency());

		for (Invoice invoice : invoices)
			totalAmount += invoice.getTotalAmount().getAmount();

		if (!Objects.equals(object.getCode(), sponsorship.getCode())) {
			Boolean codeDuplicated = this.repository.findSponsorshipByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.sponsorship.form.error.codeDuplicated");

		}

		Boolean amountLessInvoicesAmount = object.getAmount().getAmount() >= totalAmount;
		Boolean amountPositive = object.getAmount().getAmount() >= 0;
		Boolean momentBeforeDurationStart = MomentHelper.isAfter(object.getDurationStart(), MomentHelper.deltaFromMoment(object.getMoment(), 0l, ChronoUnit.DAYS));
		Boolean durationStart1MothBeforeDurationEnd = MomentHelper.isAfter(object.getDurationEnd(), MomentHelper.deltaFromMoment(object.getDurationStart(), 1l, ChronoUnit.MONTHS));
		super.state(momentBeforeDurationStart, "durationStart", "sponsor.sponsorship.form.error.momentBeforeDurationStart");
		super.state(durationStart1MothBeforeDurationEnd, "durationEnd", "sponsor.sponsorship.form.error.durationStart1MothBeforeDurationEnd");

		if (isAcceptedCurrency) {
			super.state(amountPositive, "amount", "sponsor.sponsorship.form.error.amountPositive");
			super.state(amountLessInvoicesAmount, "amount", "sponsor.sponsorship.form.error.amountLessInvoicesAmount");
		}
		super.state(isAcceptedCurrency, "amount", "sponsor.sponsorship.form.error.isAcceptedCurrency");

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;
		SelectChoices choices;
		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjects();

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());

		Dataset dataset;

		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project", "draftMode");

		dataset.put("types", choices);
		dataset.put("projects", choicesProjects);
		super.getResponse().addData(dataset);
	}

}
