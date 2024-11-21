
package acme.features.sponsor.invoice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoicePublishService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int id;
		Invoice invoice;
		Sponsor sponsor;
		int id1;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findInvoiceById(id);
		id1 = super.getRequest().getPrincipal().getAccountId();

		sponsor = invoice.getSponsorship().getSponsor();
		status = invoice != null && invoice.isDraftMode() && super.getRequest().getPrincipal().hasRole(Sponsor.class) && sponsor.getUserAccount().getId() == id1;

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
	public void validate(final Invoice object) {
		assert object != null;

		Invoice invoice1 = this.repository.findInvoiceById(object.getId());

		if (!Objects.equals(object.getCode(), invoice1.getCode())) {
			Boolean codeDuplicated = this.repository.findInvoiceByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.invoice.form.error.codeDuplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity") && !super.getBuffer().getErrors().hasErrors("tax")) {

			String currency = object.getSponsorship().getAmount().getCurrency();

			boolean correctCurrency = Objects.equals(object.getQuantity().getCurrency(), currency);
			super.state(correctCurrency, "quantity", "sponsor.invoice.form.error.correctCurrency");
			Boolean correctQuantity = object.getQuantity().getAmount() > 0;
			super.state(correctQuantity, "quantity", "sponsor.invoice.form.error.correctQuantity");
			double maxAmount = 1000000.;

			Collection<Invoice> invoices = this.repository.findAllInvoicePublishedOfSponsorship(invoice1.getSponsorship().getId());
			for (Invoice invoice : invoices)
				if (invoice.getId() != invoice1.getId())
					maxAmount = maxAmount - invoice.getTotalAmount().getAmount();

			boolean incorrectMaxAmount = maxAmount - object.getTotalAmount().getAmount() < 0;
			super.state(!incorrectMaxAmount, "quantity", "sponsor.invoice.form.error.incorrectMaxAmountPublished");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {

			LocalDateTime localDateTime = LocalDateTime.of(2200, 12, 31, 23, 59);
			Date maxDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

			boolean dueDateMax = object.getDueDate().after(maxDate);
			super.state(!dueDateMax, "dueDate", "sponsor.invoice.form.error.dueDateMax");

			boolean registrationTime1MonthBeforeDueDate;

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
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");

	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
