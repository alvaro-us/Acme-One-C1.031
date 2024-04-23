
package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerUserStoryDeleteService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int userStoryId;
		UserStory userStory;
		Manager manager;
		int id1;

		userStoryId = super.getRequest().getData("id", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);
		id1 = super.getRequest().getPrincipal().getAccountId();

		manager = userStory.getManager();
		status = userStory != null && userStory.isDraftMode() && super.getRequest().getPrincipal().hasRole(Manager.class) && userStory.getManager().getUserAccount().getId() == id1;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priorityType", "link", "draftMode");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		int numberAssignments = this.repository.findNumberAssignmentOfUserStory(id);

		status = numberAssignments == 0;

		super.state(status, "*", "manager.user-story.delete.exist-assignment");

	}
	@Override
	public void perform(final UserStory object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		int managerId;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priorityType", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
