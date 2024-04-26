
package acme.features.developer.trainingModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.trainings.DifficultyType;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	private static final String									DIFFICULTYTYPE	= "difficultyType";

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperTrainingModuleRepository	repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Developer developer;
		TrainingModule object;

		developer = this.repository.findDeveloperById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new TrainingModule();
		object.setDraftMode(true);
		object.setDeveloper(developer);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "updateMoment", "details", AuthenticatedDeveloperTrainingModuleCreateService.DIFFICULTYTYPE, "link", "estimatedTotalTime", "draftMode");
		final DifficultyType dType;
		dType = super.getRequest().getData(AuthenticatedDeveloperTrainingModuleCreateService.DIFFICULTYTYPE, DifficultyType.class);

		object.setDifficultyType(dType);

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-module.form.error.duplicated");
		}

		if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.update-date-not-valid");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices;

		choices = SelectChoices.from(DifficultyType.class, object.getDifficultyType());

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "updateMoment", "details", "link", "estimatedTotalTime", "draftMode");
		dataset.put(AuthenticatedDeveloperTrainingModuleCreateService.DIFFICULTYTYPE, choices.getSelected().getKey());
		dataset.put("difficultyTypeTypes", choices);

		super.getResponse().addData(dataset);
	}

}
