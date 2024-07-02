
package acme.features.manager.dashboard;

import java.util.HashMap;
import java.util.Map;

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
		final Map<String, Double> averageCostProject = new HashMap<>();
		final Map<String, Double> deviationCostProject = new HashMap<>();
		final Map<String, Double> minCostProject = new HashMap<>();
		final Map<String, Double> maxCostProject = new HashMap<>();

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

		averageCostProject.put("EUR", this.repository.getAverageCostProjectEUR(managerId));
		averageCostProject.put("GBP", this.repository.getAverageCostProjectGBP(managerId));
		averageCostProject.put("USD", this.repository.getAverageCostProjectUSD(managerId));

		deviationCostProject.put("EUR", this.repository.getDeviationCostProjectEUR(managerId));
		deviationCostProject.put("GBP", this.repository.getDeviationCostProjectGBP(managerId));
		deviationCostProject.put("USD", this.repository.getDeviationCostProjectUSD(managerId));

		minCostProject.put("EUR", this.repository.getMinCostProjectEUR(managerId));
		minCostProject.put("GBP", this.repository.getMinCostProjectGBP(managerId));
		minCostProject.put("USD", this.repository.getMinCostProjectUSD(managerId));

		maxCostProject.put("EUR", this.repository.getMaxCostProjectEUR(managerId));
		maxCostProject.put("GBP", this.repository.getMaxCostProjectGBP(managerId));
		maxCostProject.put("USD", this.repository.getMaxCostProjectUSD(managerId));

		dashboard.setAverageCostProject(averageCostProject);
		dashboard.setDeviationCostProject(deviationCostProject);
		dashboard.setMinCostProject(minCostProject);
		dashboard.setMaxCostProject(maxCostProject);

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
