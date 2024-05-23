
package acme.features.sponsor.invoice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Controller
public class AuthenticatedSponsorInvoiceController extends AbstractController<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorInvoiceListService	listService;

	@Autowired
	protected AuthenticatedSponsorInvoiceCreateService	createService;

	@Autowired
	protected AuthenticatedSponsorInvoiceUpdateService	updateService;

	@Autowired
	protected AuthenticatedSponsorInvoiceShowService	showService;

	@Autowired
	protected AuthenticatedSponsorInvoiceDeleteService	deleteService;

	@Autowired
	protected AuthenticatedSponsorInvoicePublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
