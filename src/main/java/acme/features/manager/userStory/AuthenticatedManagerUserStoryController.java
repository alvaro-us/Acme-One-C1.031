
package acme.features.manager.userStory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Controller
public class AuthenticatedManagerUserStoryController extends AbstractController<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerUserStoryListService		listService;

	@Autowired
	protected AuthenticatedManagerUserStoryCreateService	createService;

	@Autowired
	protected AuthenticatedManagerUserStoryUpdateService	updateService;

	@Autowired
	protected AuthenticatedManagerUserStoryShowService		showService;

	@Autowired
	protected AuthenticatedManagerUserStoryPublishService	publishService;

	@Autowired
	protected AuthenticatedManagerUserStoryDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("finalise", "update", this.publishService);

	}

}
