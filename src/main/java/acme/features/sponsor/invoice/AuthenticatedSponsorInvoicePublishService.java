
package acme.features.sponsor.invoice;

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

		Invoice invoice;
		invoice = this.repository.findInvoiceById(object.getId());
		boolean noUpdate = invoice.getCode().equals(object.getCode()) && invoice.getRegistrationTime().compareTo(object.getRegistrationTime()) == 0 && invoice.getDueDate().compareTo(object.getDueDate()) == 0
			&& invoice.getQuantity().getAmount().equals(object.getQuantity().getAmount()) && invoice.getQuantity().getCurrency().equals(object.getQuantity().getCurrency()) && invoice.getTax().equals(object.getTax())
			&& invoice.getLink().equals(object.getLink());
		super.state(noUpdate, "*", "sponsor.invoice.form.error.hasToUpdate");

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

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");

		super.getResponse().addData(dataset);
	}

}
