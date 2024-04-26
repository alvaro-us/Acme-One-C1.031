
package acme.features.administrator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.risks.Risk;

@Controller
public class AdministratorRiskController extends AbstractController<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorRiskShowService		showService;

	@Autowired
	protected AdministratorRiskUpdateService	updateService;

	@Autowired
	protected AdministratorRiskCreateService	createService;

	@Autowired
	protected AdministratorRiskDeleteService	deleteService;

	@Autowired
	protected AdministratorRiskListService		listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("list", this.listService);
	}

}
