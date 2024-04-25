
package acme.features.manager.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerProjectDeleteService extends AbstractService<Manager, Project> {

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

		super.bind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		int numberAssignments = this.repository.findNumberAssignmentOfProject(id);
		int numberContracts = this.repository.findNumberContractOfProject(id);
		//int numberRisks = this.repository.findNumberRisksOfProject(id);
		//int numberObjective = this.repository.findNumberObjectiveOfProject(id);
		int numberSponsorship = this.repository.findNumberSponsorshipOfProject(id);
		int numberTrainingModule = this.repository.findNumberTrainingModuleOfProject(id);

		status = numberAssignments == 0;
		boolean status1 = numberContracts == 0;
		//boolean status2 = numberRisks == 0;
		//boolean status3 = numberObjective == 0;
		boolean status4 = numberSponsorship == 0;
		boolean status5 = numberTrainingModule == 0;

		super.state(status1, "*", "manager.project.delete.exist-contract");
		super.state(status, "*", "manager.project.delete.exist-assignment");
		super.state(status4, "*", "manager.project.delete.exist-sponsorship");
		super.state(status5, "*", "manager.project.delete.exist-training-module");

	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstrat", "indicator", "cost", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
