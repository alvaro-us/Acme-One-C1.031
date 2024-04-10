
package acme.features.manager.projects;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int projectId;
		Project project;
		Manager manager;
		int id1;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findProjectById(projectId);
		id1 = super.getRequest().getPrincipal().getAccountId();

		manager = project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(Manager.class) && project.getManager().getUserAccount().getId() == id1;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<Assignment> assignments;

		assignments = this.repository.findAllAssignmentsOfProject(object.getId());

		this.repository.deleteAll(assignments);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		int managerId;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		dataset = super.unbind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
