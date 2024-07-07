
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoiceDeleteService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int invoiceId;
		Invoice invoice;
		Sponsor sponsor;
		int id1;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findInvoiceById(invoiceId);
		id1 = super.getRequest().getPrincipal().getAccountId();
		boolean correctInvoice = invoice != null && invoice.isDraftMode();
		sponsor = invoice.getSponsorship().getSponsor();
		status = correctInvoice && super.getRequest().getPrincipal().hasRole(Sponsor.class) && sponsor.getUserAccount().getId() == id1;

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

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "sponsorship");
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;

	}
	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "sponsorship");

		super.getResponse().addData(dataset);
	}

}
