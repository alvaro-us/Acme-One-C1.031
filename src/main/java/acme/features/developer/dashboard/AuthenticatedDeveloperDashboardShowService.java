
package acme.features.developer.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int developerId;
		DeveloperDashboard dashboard;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();

		dashboard = new DeveloperDashboard();

		dashboard.setTotalNumberOfTrainingModulesWithUpdateMoment(this.repository.getTotalNumberOfTrainingModulesWithUpdateMoment(developerId));
		dashboard.setTotalNumberOfTrainingSessionsWithLink(this.repository.getTotalNumberOfTrainingSessionsWithLink(developerId));
		dashboard.setAverageTimeTrainingModule(this.repository.getAverageTimeTrainingModule(developerId));
		dashboard.setDeviationTimeTrainingModule(this.repository.getDeviationTimeTrainingModule(developerId));
		dashboard.setMinTimeTrainingModule(this.repository.getMinTimeTrainingModule(developerId));
		dashboard.setMaxTimeTrainingModule(this.repository.getMaxTimeTrainingModule(developerId));

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfTrainingModulesWithUpdateMoment", "totalNumberOfTrainingSessionsWithLink", "averageTimeTrainingModule", "deviationTimeTrainingModule", "minTimeTrainingModule", "maxTimeTrainingModule");

		super.getResponse().addData(dataset);
	}

}
