
package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.entities.projects.prioType;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		Assignment object1;

		object = new UserStory();
		object1 = new Assignment();

		final int projectId = super.getRequest().getData("projectId", int.class);
		final Project project = this.repository.findProjectById(projectId);
		object1.setProject(project);
		object1.setUserStory(object);
		object.setDraftMode(true);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "draftMode");
		final prioType pType;
		pType = super.getRequest().getData("priorityType", prioType.class);

		object.setPriorityType(pType);

	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

	}
	@Override
	public void perform(final UserStory object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(prioType.class, object.getPriorityType());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "draftMode");
		dataset.put("priorityType", choices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

}
