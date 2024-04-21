
package acme.features.manager.assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractController;
import acme.entities.projects.Assignment;
import acme.roles.Manager;

public class AuthenticatedManagerAssignmentController extends AbstractController<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerAssignmentListService		listService;

	@Autowired
	protected AuthenticatedManagerAssignmentCreateService	createService;

	@Autowired
	protected AuthenticatedManagerAssignmentShowService		showService;

	@Autowired
	protected AuthenticatedManagerAssignmentDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("show", this.showService);

	}

}
