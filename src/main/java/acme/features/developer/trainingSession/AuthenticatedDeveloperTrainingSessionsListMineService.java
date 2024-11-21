
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperTrainingSessionsListMineService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedDeveloperTrainingSessionsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int tId;
		TrainingModule trainingModule;

		tId = super.getRequest().getData("tsId", int.class);
		trainingModule = this.repository.findTrainingModuleById(tId);
		status = trainingModule != null && super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrainingSession> objects;

		final int tmId = super.getRequest().getData("tmId", int.class);
		objects = this.repository.findAllTrainingSessionsByTMId(tmId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link", "draftmode");

		if (object.isDraftMode())
			dataset.put("draftMode", "Yes");
		else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrainingSession> object) {
		assert object != null;

		int tmId;
		TrainingModule module;
		final boolean showCreate;

		tmId = super.getRequest().getData("masterId", int.class);
		module = this.repository.findTrainingModuleById(tmId);
		showCreate = module.isDraftMode() && super.getRequest().getPrincipal().hasRole(module.getDeveloper());

		super.getResponse().addGlobal("masterId", tmId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
