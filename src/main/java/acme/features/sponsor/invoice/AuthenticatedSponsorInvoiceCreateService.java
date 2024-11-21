
package acme.features.sponsor.invoice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoiceCreateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int id1;
		Sponsor sponsor;

		id1 = super.getRequest().getPrincipal().getAccountId();
		sponsor = this.repository.findSponsorByUserId(id1);

		int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		Sponsorship sponsorship = this.repository.findSponsorshipById(sponsorshipId);

		boolean canCreate = this.repository.findAllSponsorshipOfSponsorPublished(sponsor.getId()).contains(sponsorship);

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class) && canCreate;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;

		object = new Invoice();

		int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		Sponsorship sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		object.setSponsorship(sponsorship);
		object.setDraftMode(true);
		object.setRegistrationTime(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");

	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;

		// Code

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean codeDuplicated = this.repository.findInvoiceByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.invoice.form.error.codeDuplicated");
		}

		// Quantity

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			String currency = object.getSponsorship().getAmount().getCurrency();
			boolean correctCurrency = Objects.equals(object.getQuantity().getCurrency(), currency);
			super.state(correctCurrency, "quantity", "sponsor.invoice.form.error.correctCurrency");
			double maxAmount = 1000000.;
			boolean incorrectMaxAmount = object.getQuantity().getAmount() > maxAmount;
			super.state(!incorrectMaxAmount, "quantity", "sponsor.invoice.form.error.incorrectMaxAmount");
			Boolean correctQuantity = object.getQuantity().getAmount() > 0;
			super.state(correctQuantity, "quantity", "sponsor.invoice.form.error.correctQuantity");

		}

		// Date

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			boolean registrationTime1MonthBeforeDueDate;

			LocalDateTime localDateTime = LocalDateTime.of(2200, 12, 31, 23, 59);
			Date maxDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

			boolean dueDateMax = object.getDueDate().after(maxDate);
			super.state(!dueDateMax, "dueDate", "sponsor.invoice.form.error.dueDateMax");

			LocalDateTime registrationTimeDate = object.getRegistrationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime dueDate = object.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			long monthsDifference = ChronoUnit.MONTHS.between(registrationTimeDate, dueDate);
			registrationTime1MonthBeforeDueDate = monthsDifference >= 1;

			super.state(registrationTime1MonthBeforeDueDate, "dueDate", "sponsor.invoice.form.error.registrationTime1MonthBeforeDueDate");
		}

		//URL
		if (!super.getBuffer().getErrors().hasErrors("link") && !object.getLink().isEmpty()) {
			boolean linkbetween7and255 = object.getLink().length() >= 7 && object.getLink().length() <= 255;
			super.state(linkbetween7and255, "link", "sponsor.invoice.form.error.linkbetween7and255");
		}

	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");

		dataset.put("sponsorshipId", super.getRequest().getData("sponsorshipId", int.class));
		dataset.put("draftMode", object.getSponsorship().isDraftMode());
		super.getResponse().addData(dataset);
	}

}
