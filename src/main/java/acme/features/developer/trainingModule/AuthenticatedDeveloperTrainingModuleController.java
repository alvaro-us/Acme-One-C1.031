
package acme.features.developer.trainingModule;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Controller
public class AuthenticatedDeveloperTrainingModuleController extends AbstractController<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleListMineService	listService;

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleCreateService		createService;

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleUpdateService		updateService;

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleShowService		showService;

	@Autowired
	protected AuthenticatedDeveloperTrainingModulePublishService	publishService;

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleDeleteService		deleteService;

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
