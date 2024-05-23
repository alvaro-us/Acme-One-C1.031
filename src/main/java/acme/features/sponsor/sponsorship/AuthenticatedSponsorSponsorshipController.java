
package acme.features.sponsor.sponsorship;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Controller
public class AuthenticatedSponsorSponsorshipController extends AbstractController<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorSponsorshipListMineService	listMineService;

	@Autowired
	protected AuthenticatedSponsorSponsorshipShowService		showService;

	@Autowired
	protected AuthenticatedSponsorSponsorshipUpdateService		updateService;

	@Autowired
	protected AuthenticatedSponsorSponsorshipDeleteService		deleteService;

	@Autowired
	protected AuthenticatedSponsorSponsorshipCreateService		createService;

	@Autowired
	protected AuthenticatedSponsorSponsorshipPublishService		publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listMineService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
