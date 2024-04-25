
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerUserStoryListMineService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId;

		Collection<UserStory> objects;
		userAccountId = super.getRequest().getPrincipal().getActiveRoleId();

		objects = this.repository.findAllUserStoryOfManager(userAccountId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "priorityType", "estimatedCost");
		super.getResponse().addData(dataset);

	}

}
