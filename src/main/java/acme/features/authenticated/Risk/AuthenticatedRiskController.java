
package acme.features.authenticated.Risk;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.risks.Risk;

@Controller
public class AuthenticatedRiskController extends AbstractController<Any, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedRiskListAllService	listService;

	@Autowired
	protected AuthenticatedRiskShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
