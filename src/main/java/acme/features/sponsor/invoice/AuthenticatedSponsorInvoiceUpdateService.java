
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
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int invocieId;
		Invoice invoice;
		Sponsor sponsor;
		int id1;

		invocieId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findInvoiceById(invocieId);
		id1 = super.getRequest().getPrincipal().getAccountId();

		sponsor = invoice.getSponsorship().getSponsor();
		status = invoice != null && super.getRequest().getPrincipal().hasRole(Sponsor.class) && sponsor.getUserAccount().getId() == id1;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findInvoiceById(id);

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

		Invoice invoice1 = this.repository.findInvoiceById(object.getId());
		int sponsorshipId = object.getSponsorship().getId();
		Collection<Invoice> invoices = this.repository.findAllInvoiceOfSponsorship(sponsorshipId);

		double totalAmount = 0.;
		for (Invoice invoice : invoices)
			if (invoice != null && invoice.getId() != object.getId())
				totalAmount += invoice.getTotalAmount().getAmount();

		if (!Objects.equals(object.getCode(), invoice1.getCode())) {
			Boolean codeDuplicated = this.repository.findInvoiceByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.invoice.form.error.codeDuplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {

			String currency = object.getSponsorship().getAmount().getCurrency();

			boolean correctCurrency = Objects.equals(object.getQuantity().getCurrency(), currency);
			super.state(correctCurrency, "quantity", "sponsor.invoice.form.error.correctCurrency");

			boolean totalAmountLessOrEqualThanSponsorshipAmount = totalAmount + object.getTotalAmount().getAmount() <= object.getSponsorship().getAmount().getAmount();
			if (correctCurrency) {
				super.state(totalAmountLessOrEqualThanSponsorshipAmount, "quantity", "sponsor.invoice.form.error.totalAmountLessOrEqualThanSponsorshipAmount");
				super.state(totalAmountLessOrEqualThanSponsorshipAmount, "tax", "sponsor.invoice.form.error.totalAmountLessOrEqualThanSponsorshipAmount");
			}

		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate") && !super.getBuffer().getErrors().hasErrors("registrationTime")) {
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
		super.getResponse().addData(dataset);
	}

}
