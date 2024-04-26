
package acme.features.administrator.objective;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.objective.Objective;

@Controller
public class AdministratorObjectiveController extends AbstractController<Administrator, Objective> {

	@Autowired
	protected AdministratorObjectiveCreateService createService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("create", this.createService);

	}

}
