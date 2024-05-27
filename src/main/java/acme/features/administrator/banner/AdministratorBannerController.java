
package acme.features.administrator.banner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.banner.Banner;

@Controller
public class AdministratorBannerController extends AbstractController<Administrator, Banner> {

	// Internal state 
	@Autowired
	protected AdministratorBannerListService	listService;

	@Autowired
	protected AdministratorBannerShowService	showService;

	@Autowired
	protected AdministratorBannerCreateService	createService;

	@Autowired
	protected AdministradorBannerUpdateService	updateService;

	@Autowired
	protected AdministradotBannerDeleteService	deleteService;


	// Contructors
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

	}
}
