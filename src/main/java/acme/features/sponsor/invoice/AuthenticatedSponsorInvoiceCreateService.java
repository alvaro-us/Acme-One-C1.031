
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
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

		boolean canCreate = this.repository.findAllSponsorshipOfSponsor(sponsor.getId()).contains(sponsorship);

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

		int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		Collection<Invoice> invoices = this.repository.findAllInvoiceOfSponsorship(sponsorshipId);
		double totalAmount = 0.;

		for (Invoice invoice : invoices)
			if (invoice != null)
				totalAmount += invoice.getTotalAmount().getAmount();

		String currency = object.getSponsorship().getAmount().getCurrency();

		// Code

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean codeDuplicated = this.repository.findInvoiceByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.invoice.form.error.codeDuplicated");
		}

		// Quantity

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {

			boolean correctCurrency = Objects.equals(object.getQuantity().getCurrency(), currency);
			super.state(correctCurrency, "quantity", "sponsor.invoice.form.error.correctCurrency");
			if (!super.getBuffer().getErrors().hasErrors("tax")) {
				boolean totalAmountLessOrEqualThanSponsorshipAmount = totalAmount + object.getTotalAmount().getAmount() <= object.getSponsorship().getAmount().getAmount();
				if (correctCurrency) {
					super.state(totalAmountLessOrEqualThanSponsorshipAmount, "quantity", "sponsor.invoice.form.error.totalAmountLessOrEqualThanSponsorshipAmount");
					super.state(totalAmountLessOrEqualThanSponsorshipAmount, "tax", "sponsor.invoice.form.error.totalAmountLessOrEqualThanSponsorshipAmount");
				}
			}
		}

		// Date

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			boolean registrationTime1MothBeforeDueDate = MomentHelper.isAfter(object.getDueDate(), MomentHelper.deltaFromMoment(object.getRegistrationTime(), 1l, ChronoUnit.MONTHS));
			super.state(registrationTime1MothBeforeDueDate, "dueDate", "sponsor.invoice.form.error.registrationTime1MothBeforeDueDate");
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
