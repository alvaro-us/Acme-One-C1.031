
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
public class AuthenticatedManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	private static final String							ESTIMATEDCOST	= "estimatedCost";
	private static final String							PRIORITYTYPE	= "priorityType";

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerUserStoryRepository	repository;

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
		Manager manager;

		object = new UserStory();
		object.setDraftMode(true);
		manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setManager(manager);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", AuthenticatedManagerUserStoryCreateService.ESTIMATEDCOST, "acceptanceCriteria", AuthenticatedManagerUserStoryCreateService.PRIORITYTYPE, "link", "draftMode");
		final prioType pType;
		pType = super.getRequest().getData(AuthenticatedManagerUserStoryCreateService.PRIORITYTYPE, prioType.class);

		object.setPriorityType(pType);

	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors(AuthenticatedManagerUserStoryCreateService.ESTIMATEDCOST))
			super.state(object.getEstimatedCost() >= 0.0, AuthenticatedManagerUserStoryCreateService.ESTIMATEDCOST, "manager.project.error.cost.negative-price");

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

		dataset = super.unbind(object, "title", "description", AuthenticatedManagerUserStoryCreateService.ESTIMATEDCOST, "acceptanceCriteria", "link", "draftMode");
		dataset.put(AuthenticatedManagerUserStoryCreateService.PRIORITYTYPE, choices.getSelected().getKey());
		dataset.put("priorityTypes", choices);
		super.getResponse().addData(dataset);
	}

}
