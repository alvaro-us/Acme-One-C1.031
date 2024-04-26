
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
public class AuthenticatedDeveloperTrainingSessionsUpdateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingSessionsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int tsId;
		TrainingModule trainingModule;
		TrainingSession trainingSession;

		tsId = super.getRequest().getData("tsId", int.class);
		trainingModule = this.repository.findTrainingModuleById(tsId);
		trainingSession = this.repository.findTrainingSessionById(tsId);
		status = trainingModule != null && trainingModule.isDraftMode() && super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link", "draftmode");

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

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link", "draftmode");
		dataset.put("masterId", object.getTrainingModule().getId());

		super.getResponse().addData(dataset);
	}

}
