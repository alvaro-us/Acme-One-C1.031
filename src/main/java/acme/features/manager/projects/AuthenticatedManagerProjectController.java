
package acme.features.manager.projects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Controller
public class AuthenticatedManagerProjectController extends AbstractController<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerProjectListMineService	listService;

	@Autowired
	protected AuthenticatedManagerProjectCreateService		createService;

	@Autowired
	protected AuthenticatedManagerProjectUpdateService		updateService;

	@Autowired
	protected AuthenticatedManagerProjectShowService		showService;

	@Autowired
	protected AuthenticatedManagerProjectPublishService		publishService;

	@Autowired
	protected AuthenticatedManagerProjectDeleteService		deleteService;

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
