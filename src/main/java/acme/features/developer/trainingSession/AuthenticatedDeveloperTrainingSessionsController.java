
package acme.features.developer.trainingSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Controller
public class AuthenticatedDeveloperTrainingSessionsController extends AbstractController<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsListMineService	listService;

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsCreateService	createService;

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsUpdateService	updateService;

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsShowService		showService;

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsPublishService	publishService;

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsDeleteService	deleteService;

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
