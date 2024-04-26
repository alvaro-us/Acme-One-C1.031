
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
public class AuthenticatedManagerAssignmentCreateService extends AbstractService<Manager, Assignment> {

	private static final String							PROJECT		= "project";
	private static final String							USERSTORY	= "userStory";

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerAssignmentRepository	repository;

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

		object = new Assignment();

		object.setProject(null);
		object.setUserStory(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Assignment object) {
		assert object != null;

		int projectId = super.getRequest().getData(AuthenticatedManagerAssignmentCreateService.PROJECT, int.class);
		Project project = this.repository.findProjectById(projectId);
		object.setProject(project);

		int storyId = super.getRequest().getData(AuthenticatedManagerAssignmentCreateService.USERSTORY, int.class);
		UserStory story = this.repository.findUserStoryById(storyId);
		object.setUserStory(story);
	}

	@Override
	public void validate(final Assignment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors(AuthenticatedManagerAssignmentCreateService.PROJECT) && object.getProject() != null)
			super.state(object.getProject().isDraftMode(), AuthenticatedManagerAssignmentCreateService.PROJECT, "manager.assignment.project.notDraftMode");

		if (!super.getBuffer().getErrors().hasErrors(AuthenticatedManagerAssignmentCreateService.PROJECT)) {
			int exists = this.repository.existsAssignmentWithProjectAndUserStory(object.getProject(), object.getUserStory());
			super.state(exists == 0, "*", "manager.assignment.project.exists");
		}

		if (!super.getBuffer().getErrors().hasErrors(AuthenticatedManagerAssignmentCreateService.PROJECT))
			super.state(object.getProject() != null, AuthenticatedManagerAssignmentCreateService.PROJECT, "manager.assignment.project.null");

		if (!super.getBuffer().getErrors().hasErrors(AuthenticatedManagerAssignmentCreateService.USERSTORY))
			super.state(object.getUserStory() != null, AuthenticatedManagerAssignmentCreateService.USERSTORY, "manager.assignment.user-story.null");

	}

	@Override
	public void perform(final Assignment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		Dataset dataset = null;
		Collection<Project> projects;
		Collection<UserStory> stories;
		SelectChoices choices;
		SelectChoices choices1;
		Manager manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		stories = this.repository.findUserStories(manager);
		projects = this.repository.findProjects(manager);

		choices = SelectChoices.from(stories, "title", object.getUserStory());
		choices1 = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, AuthenticatedManagerAssignmentCreateService.PROJECT, AuthenticatedManagerAssignmentCreateService.USERSTORY);
		dataset.put(AuthenticatedManagerAssignmentCreateService.USERSTORY, choices.getSelected().getKey());
		dataset.put("userStories", choices);
		dataset.put(AuthenticatedManagerAssignmentCreateService.PROJECT, choices1.getSelected().getKey());
		dataset.put("projects", choices1);

		super.getResponse().addData(dataset);
	}

}
