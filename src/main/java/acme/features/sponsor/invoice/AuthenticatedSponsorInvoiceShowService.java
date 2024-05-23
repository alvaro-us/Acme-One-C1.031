
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.CurrencyService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedSponsorInvoiceRepository	repository;

	@Autowired
	private CurrencyService							service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		Invoice object;
		Sponsor sponsor;
		int id;
		int id1;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findInvoiceById(id);
		id1 = super.getRequest().getPrincipal().getAccountId();

		sponsor = this.repository.findSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(Sponsor.class) && object.getSponsorship().getSponsor().equals(sponsor) && object.getSponsorship().getSponsor().getUserAccount().getId() == id1;
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
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "sponsorship", "draftMode");

		Money quantityBase = this.service.changeCurrencyToBase(object.getQuantity());
		Money quantityBaseTax = this.service.changeCurrencyToBase(object.getTotalAmount());
		dataset.put("quantityBase", quantityBase);
		dataset.put("quantityBaseTax", quantityBaseTax);

		super.getResponse().addData(dataset);

	}

}
