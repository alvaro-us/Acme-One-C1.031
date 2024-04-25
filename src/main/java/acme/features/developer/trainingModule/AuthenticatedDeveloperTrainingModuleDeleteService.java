
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperTrainingModuleDeleteService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;
		Developer developer;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		developer = trainingModule == null ? null : trainingModule.getDeveloper();
		status = trainingModule != null && trainingModule.isDraftMode() && super.getRequest().getPrincipal().hasRole(developer);

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
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "updateMoment", "details", "difficultyLevel", "link", "estimatedTotalTime", "draftMode");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;
		Collection<TrainingSession> sessions;

		sessions = this.repository.findAllTrainingSessionsByTrainingModuleId(object.getId());

		this.repository.deleteAll(sessions);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "updateMoment", "details", "difficultyLevel", "link", "estimatedTotalTime", "draftMode");

		super.getResponse().addData(dataset);
	}

}
