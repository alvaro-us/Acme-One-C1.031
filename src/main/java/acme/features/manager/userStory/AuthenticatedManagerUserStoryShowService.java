
package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.UserStory;
import acme.entities.projects.prioType;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerUserStoryShowService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		UserStory object;
		Manager manager;
		int id;
		int id1;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findUserStoryById(id);
		id1 = super.getRequest().getPrincipal().getAccountId();

		manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(Manager.class) && object.getManager().equals(manager) && object.getManager().getUserAccount().getId() == id1;

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
	public void unbind(final UserStory object) {
		assert object != null;
		SelectChoices choices;

		choices = SelectChoices.from(prioType.class, object.getPriorityType());
		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "draftMode");
		dataset.put("priorityType", choices.getSelected().getKey());
		dataset.put("priorityTypes", choices);
		super.getResponse().addData(dataset);
	}

}
