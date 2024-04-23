
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
public class AuthenticatedManagerProjectUpdateService extends AbstractService<Manager, Project> {

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.repository.findOneCourseByCodeAndDistinctId(object.getCode(), object.getId());

			super.state(existing == null, "code", "manager.project.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			Configuration config;
			config = this.repository.findConfiguration();

			super.state(Arrays.asList(config.getAcceptedCurrency().trim().split(",")).contains(object.getCost().getCurrency()), "cost", "manager.project.currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost"))
			super.state(object.getCost().getAmount() >= 0., "retailPrice", "manager.project.negative-price");

	}

	@Override
	public void perform(final Project object) {
		assert object != null;

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
