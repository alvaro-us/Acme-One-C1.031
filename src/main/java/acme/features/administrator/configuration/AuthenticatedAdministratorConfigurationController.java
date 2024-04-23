
package acme.features.administrator.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.configuration.Configuration;

@Controller
public class AuthenticatedAdministratorConfigurationController extends AbstractController<Administrator, Configuration> {

	@Autowired
	protected AuthenticatedAdministratorConfigurationUpdateService	updateService;

	@Autowired
	protected AuthenticatedAdministratorConfigurationShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);

	}

}
