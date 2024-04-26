
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperTrainingSessionsCreateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int tId;
		TrainingModule trainingModule;

		tId = super.getRequest().getData("tsId", int.class);
		trainingModule = this.repository.findTrainingModuleById(tId);
		status = trainingModule != null && trainingModule.isDraftMode() && super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession session;
		int tId;
		TrainingModule module;

		tId = super.getRequest().getData("tId", int.class);
		module = this.repository.findTrainingModuleById(tId);

		session = new TrainingSession();
		session.setTrainingModule(module);

		super.getBuffer().addData(session);
	}
	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "updateMoment", "details", "difficultyLevel", "link", "estimatedTotalTime", "draftMode");

	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession existing;

			existing = this.repository.findTrainingSessionByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "developer.training-session.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startPeriod")) {
			TrainingModule tm;
			int tmId;

			tmId = super.getRequest().getData("tmId", int.class);
			tm = this.repository.findTrainingModuleById(tmId);
			super.state(MomentHelper.isAfter(object.getStartPeriod(), tm.getCreationMoment()), "startPeriod", "developer.training-session.form.error.invalid-creation-moment");
		}

		if (!super.getBuffer().getErrors().hasErrors("endperiod")) {
			Date minTime;

			minTime = MomentHelper.deltaFromMoment(object.getStartPeriod(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(object.getEndPeriod(), minTime), "endPeriod", "developer.training-session.form.error.too-close");
		}

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "updateMoment", "details", "difficultyLevel", "link", "estimatedTotalTime", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
