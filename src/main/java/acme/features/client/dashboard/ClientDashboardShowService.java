
package acme.features.client.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.client.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ClientDashboard dashboard;
		Integer totalNumberOfProgressLogsBelow25Percent;
		Integer totalNumberOfProgressLogs25To50Percent;
		Integer totalNumberOfProgressLogs50To75Percent;
		Integer totalNumberOfProgressLogsAbove75Percent;
		Double averageBudgetOfContracts;
		Double deviationBudgetOfContracts;
		Double minBudgetOfContracts;
		Double maxBudgetOfContracts;

		totalNumberOfProgressLogsBelow25Percent = this.repository.totalNumberOfProgressLogsBelow25Percent();
		totalNumberOfProgressLogs25To50Percent = this.repository.totalNumberOfProgressLogs25To50Percent();
		totalNumberOfProgressLogs50To75Percent = this.repository.totalNumberOfProgressLogs50To75Percent();
		totalNumberOfProgressLogsAbove75Percent = this.repository.totalNumberOfProgressLogsAbove75Percent();
		averageBudgetOfContracts = this.repository.averageBudgetOfContracts();
		deviationBudgetOfContracts = this.repository.deviationBudgetOfContracts();
		minBudgetOfContracts = this.repository.minBudgetOfContracts();
		maxBudgetOfContracts = this.repository.maxBudgetOfContracts();

		dashboard = new ClientDashboard();
		dashboard.setTotalNumberOfProgressLogsBelow25Percent(totalNumberOfProgressLogsBelow25Percent);
		dashboard.setTotalNumberOfProgressLogs25To50Percent(totalNumberOfProgressLogs25To50Percent);
		dashboard.setTotalNumberOfProgressLogs50To75Percent(totalNumberOfProgressLogs50To75Percent);
		dashboard.setTotalNumberOfProgressLogsAbove75Percent(totalNumberOfProgressLogsAbove75Percent);
		dashboard.setAverageBudgetOfContracts(averageBudgetOfContracts);
		dashboard.setDeviationBudgetOfContracts(deviationBudgetOfContracts);
		dashboard.setMinBudgetOfContracts(minBudgetOfContracts);
		dashboard.setMaxBudgetOfContracts(maxBudgetOfContracts);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalNumberOfProgressLogsBelow25Percent", "totalNumberOfProgressLogs25To50Percent", // 
			"totalNumberOfProgressLogs50To75Percent", "totalNumberOfProgressLogsAbove75Percent", //
			"averageBudgetOfContracts", "deviationBudgetOfContracts", //
			"minBudgetOfContracts", "maxBudgetOfContracts");

		super.getResponse().addData(dataset);
	}

}
