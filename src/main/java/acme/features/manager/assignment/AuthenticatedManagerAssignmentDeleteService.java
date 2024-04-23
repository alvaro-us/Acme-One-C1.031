
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerAssignmentDeleteService extends AbstractService<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Assignment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAssignmentById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Assignment object) {
		assert object != null;

		int projectId = super.getRequest().getData("project", int.class);
		Project project = this.repository.findProjectById(projectId);
		object.setProject(project);

		int storyId = super.getRequest().getData("userStory", int.class);
		UserStory story = this.repository.findUserStoryById(storyId);
		object.setUserStory(story);
	}
	@Override
	public void validate(final Assignment object) {
		assert object != null;

		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		status = object.getProject().isDraftMode();

		super.state(status, "*", "manager.project.delete.projectPublished");
	}

	@Override
	public void perform(final Assignment object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		Dataset dataset = null;
		Collection<Project> projects;
		Collection<UserStory> stories;
		SelectChoices choices;
		SelectChoices choices1;

		stories = this.repository.findUserStories();
		projects = this.repository.findAllProjects();

		choices = SelectChoices.from(stories, "title", object.getUserStory());
		choices1 = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "project", "userStory");
		dataset.put("userStory", choices.getSelected().getKey());
		dataset.put("userStories", choices);
		dataset.put("project", choices1.getSelected().getKey());
		dataset.put("projects", choices1);

		super.getResponse().addData(dataset);
	}

}
