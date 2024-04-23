
package acme.features.manager.projects;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerProjectPublishService extends AbstractService<Manager, Project> {

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

		super.bind(object, "code", "title", "abstrat", "indicator", "cost", "link");

	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		int notPublishedUserStory = this.repository.findNumberUserStoryNotPublishedOfProject(id);

		int hasAssignments = this.repository.findNumberAssignmentOfProject(id);

		boolean status1 = hasAssignments != 0;
		super.state(status1, "*", "manager.project.publish.userStory.noUserStories");

		status = notPublishedUserStory == 0;
		super.state(status, "*", "manager.project.publish.userStory.notPublished");

		if (!super.getBuffer().getErrors().hasErrors("indicator")) {
			boolean indicator;
			indicator = object.isIndicator();

			super.state(indicator == false, "indicator", "manager.project.error.indicator.notFalse");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.repository.findOneCourseByCodeAndDistinctId(object.getCode(), object.getId());

			super.state(existing == null || !object.getCode().equals(existing.getCode()), "code", "manager.project.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			Configuration config;
			config = this.repository.findConfiguration();

			super.state(Arrays.asList(config.getAcceptedCurrency().trim().split(",")).contains(object.getCost().getCurrency()), "cost", "manager.project.error.cost.currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost"))
			super.state(object.getCost().getAmount() >= 0., "retailPrice", "manager.project.error.cost.negative-price");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
