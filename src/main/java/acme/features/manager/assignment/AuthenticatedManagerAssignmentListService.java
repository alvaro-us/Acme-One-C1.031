
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Assignment;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerAssignmentListService extends AbstractService<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<Assignment> objects;

		Manager manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		objects = this.repository.findAllAssignments(manager);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "project", "userStory");
		dataset.put("project", object.getProject().getTitle());
		dataset.put("userStory", object.getUserStory().getTitle());

		super.getResponse().addData(dataset);
	}

}
