
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorInvoiceListService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedSponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorshipId;
		final int id;
		Sponsorship sponsorship;
		id = super.getRequest().getPrincipal().getAccountId();
		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		status = sponsorship != null && sponsorship.getSponsor().getUserAccount().getId() == id;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Invoice> objects;
		final int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		objects = this.repository.findAllInvoiceOfSponsorship(sponsorshipId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");
		dataset.put("published", object.isDraftMode() ? "❌" : "✅");

		super.getResponse().addData(dataset);

	}
	@Override
	public void unbind(final Collection<Invoice> objects) {
		assert objects != null;

		int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		Sponsorship sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		final Boolean draftMode = sponsorship.isDraftMode();

		super.getResponse().addGlobal("draftMode", draftMode);
		super.getResponse().addGlobal("sponsorshipId", sponsorshipId);
	}

}
