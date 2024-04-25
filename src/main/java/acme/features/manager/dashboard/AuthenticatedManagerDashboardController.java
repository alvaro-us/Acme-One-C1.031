
package acme.features.manager.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Controller
public class AuthenticatedManagerDashboardController extends AbstractController<Manager, ManagerDashboard> {

	@Autowired
	protected AuthenticatedManagerDashboardShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
