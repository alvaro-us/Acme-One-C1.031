
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

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
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

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

		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(existing.equals(object), "code", "developer.training-module.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.update-date-not-valid");

		{
			Collection<TrainingSession> sessions;
			int totalSessions;

			sessions = this.repository.findAllTrainingSessionsByTrainingModuleId(object.getId());
			totalSessions = sessions.size();
			super.state(totalSessions >= 1, "*", "developer.training-module.form.error.not-enough-training-sessions");
		}
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "updateMoment", "details", "difficultyLevel", "link", "estimatedTotalTime", "draftMode");

		super.getResponse().addData(dataset);
	}

}
