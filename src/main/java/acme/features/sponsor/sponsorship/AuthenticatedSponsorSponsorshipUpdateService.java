
package acme.features.sponsor.sponsorship;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
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

		boolean correctSponsorship = sponsorship != null && sponsorship.isDraftMode();
		boolean correctRol = super.getRequest().getPrincipal().hasRole(Sponsor.class);
		boolean correctUserId = sponsorship.getSponsor().getUserAccount().getId() == id;
		status = correctSponsorship && correctRol && correctUserId;

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

		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "draftMode", "project");

	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		int sponsorshipId = super.getRequest().getData("id", int.class);
		Sponsorship sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		Collection<Invoice> invoices = this.repository.findInvoiceBySponsorshipId(sponsorshipId);
		List<Invoice> invoiceList = new ArrayList<>(invoices);

		// Code
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			String code = object.getCode();
			if (!Objects.equals(code, sponsorship.getCode())) {
				boolean codeDuplicated = this.repository.findSponsorshipByCode(object.getCode()) == null;
				super.state(codeDuplicated, "code", "sponsor.sponsorship.form.error.codeDuplicated");

			}
		}

		// Date

		if (!super.getBuffer().getErrors().hasErrors("durationStart")) {
			Boolean momentBeforeDurationStart;

			LocalDateTime momentDate = object.getMoment().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime durationStartDate = object.getDurationStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			LocalDateTime localDateTime = LocalDateTime.of(2200, 12, 31, 23, 59);
			Date maxDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

			momentBeforeDurationStart = momentDate.isBefore(durationStartDate);

			super.state(momentBeforeDurationStart, "durationStart", "sponsor.sponsorship.form.error.momentBeforeDurationStart");

			if (!super.getBuffer().getErrors().hasErrors("durationEnd")) {
				boolean durationStart1MonthBeforeDurationEnd;
				LocalDateTime durationEndDate = object.getDurationEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

				long monthsDifferenceEnd = ChronoUnit.MONTHS.between(durationStartDate, durationEndDate);
				durationStart1MonthBeforeDurationEnd = monthsDifferenceEnd >= 1;

				super.state(durationStart1MonthBeforeDurationEnd, "durationEnd", "sponsor.sponsorship.form.error.durationStart1MonthBeforeDurationEnd");

				boolean durationEndMax = object.getDurationEnd().after(maxDate);
				super.state(!durationEndMax, "durationEnd", "sponsor.sponsorship.form.error.durationEndMax");

			}

			boolean durationStartMax = object.getDurationStart().after(maxDate);

			super.state(!durationStartMax, "durationStart", "sponsor.sponsorship.form.error.durationStartMax");

		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {

			boolean cantChangeCurrency = true;
			double maxAmount = 1000000.;
			String currency = object.getAmount().getCurrency();
			for (Invoice invoice : invoiceList)
				if (!invoice.isDraftMode()) {
					String invoicesCurrency = invoice.getQuantity().getCurrency();
					cantChangeCurrency = Objects.equals(invoicesCurrency, currency);
				}

			boolean isAcceptedCurrency = this.service.isAcceptedCurrency(currency);
			boolean amountPositive = object.getAmount().getAmount() > 0;

			boolean incorrectMaxAmount = object.getAmount().getAmount() > maxAmount;

			super.state(amountPositive, "amount", "sponsor.sponsorship.form.error.amountPositive");
			super.state(!incorrectMaxAmount, "amount", "sponsor.sponsorship.form.error.incorrectMaxAmount");
			super.state(cantChangeCurrency, "amount", "sponsor.sponsorship.form.error.cantChangeCurrency");

			super.state(isAcceptedCurrency, "amount", "sponsor.sponsorship.form.error.isAcceptedCurrency");

		}
		//URL
		if (!super.getBuffer().getErrors().hasErrors("link") && !object.getLink().isEmpty()) {
			boolean linkbetween7and255 = object.getLink().length() >= 7 && object.getLink().length() <= 255;
			super.state(linkbetween7and255, "link", "sponsor.sponsorship.form.error.linkbetween7and255");
		}

		//EMAIL
		if (!super.getBuffer().getErrors().hasErrors("email") && !object.getEmail().isEmpty()) {
			boolean emailbetween6and254 = object.getEmail().length() >= 6 && object.getEmail().length() <= 254;
			super.state(emailbetween6and254, "email", "sponsor.sponsorship.form.error.emailbetween6and254");
		}

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjectsPublished();

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project", "draftMode");

		dataset.put("types", choices);
		dataset.put("projects", choicesProjects);
		super.getResponse().addData(dataset);
	}

}
