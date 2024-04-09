
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class AuthenticatedManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int managerId;
		ManagerDashboard dashboard;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		dashboard = new ManagerDashboard();

		dashboard.setTotalNumberOfMustUserStory(this.repository.getTotalNumberOfMustUserStory(managerId));
		dashboard.setTotalNumberOfShouldUserStory(this.repository.getTotalNumberOfShouldUserStory(managerId));
		dashboard.setTotalNumberOfCouldUserStory(this.repository.getTotalNumberOfCouldUserStory(managerId));
		dashboard.setTotalNumberOfWontUserStory(this.repository.getTotalNumberOfWontUserStory(managerId));

		dashboard.setAverageEstimatedCostUserStory(this.repository.getAverageEstimatedCostUserStory(managerId));
		dashboard.setDeviationEstimatedCostUserStory(this.repository.getDeviationEstimatedCostUserStory(managerId));
		dashboard.setMinEstimatedCostUserStory(this.repository.getMinEstimatedCostUserStory(managerId));
		dashboard.setMaxEstimatedCostUserStory(this.repository.getMaxEstimatedCostUserStory(managerId));

		dashboard.setAverageCostProject(this.repository.getAverageCostProject(managerId));
		dashboard.setDeviationCostProject(this.repository.getDeviationCostProject(managerId));
		dashboard.setMinCostProject(this.repository.getMinCostProject(managerId));
		dashboard.setMaxCostProject(this.repository.getMaxCostProject(managerId));

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfMustUserStory", "totalNumberOfShouldUserStory", "totalNumberOfCouldUserStory", "totalNumberOfWontUserStory", "averageEstimatedCostUserStory", "deviationEstimatedCostUserStory",
			"minEstimatedCostUserStory", "maxEstimatedCostUserStory", "averageCostProject", "deviationCostProject", "minCostProject", "maxCostProject");

		super.getResponse().addData(dataset);
	}

}
