
package acme.features.client.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
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

		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		final Client client = this.repository.findOneClientByUserAccountId(userAccountId);
		final int numberOfContract = this.repository.findNumberContracts(client);
		final int numberOfProgressLogs = this.repository.findNumberProgressLogs(client);

		Integer percentageOfTotalNumberCompleteness25;
		Integer percentageOfTotalNumberCompleteness25At50;
		Integer percentageOfTotalNumberCompleteness50at75;
		Integer percentageOfTotalNumberCompletenessMore75;
		Double averageBudgetOfContract;
		Double deviationBudgetOfContract;
		Double minimumBudgetOfContract;
		Double maximumBudgetOfContract;

		if (numberOfContract >= 2) {
			averageBudgetOfContract = this.repository.averageBudgetOfContract(client).orElse(0.0);
			deviationBudgetOfContract = this.repository.deviationBudgetOfContract(client).orElse(0.0);
		} else {
			averageBudgetOfContract = null;
			deviationBudgetOfContract = null;
		}

		if (numberOfContract >= 1) {
			minimumBudgetOfContract = this.repository.minimumBudgetOfContract(client).orElse(0.0);
			maximumBudgetOfContract = this.repository.maximumBudgetOfContract(client).orElse(0.0);
		} else {
			minimumBudgetOfContract = null;
			maximumBudgetOfContract = null;
		}

		if (numberOfProgressLogs >= 1) {
			percentageOfTotalNumberCompleteness25 = this.repository.percentageOfTotalNumberCompleteness25(client).orElse(0);
			percentageOfTotalNumberCompleteness25At50 = this.repository.percentageOfTotalNumberCompleteness25At50(client).orElse(0);
			percentageOfTotalNumberCompleteness50at75 = this.repository.percentageOfTotalNumberCompleteness50at75(client).orElse(0);
			percentageOfTotalNumberCompletenessMore75 = this.repository.percentageOfTotalNumberCompletenessMore75(client).orElse(0);

		} else {
			percentageOfTotalNumberCompleteness25 = null;
			percentageOfTotalNumberCompleteness25At50 = null;
			percentageOfTotalNumberCompleteness50at75 = null;
			percentageOfTotalNumberCompletenessMore75 = null;
		}

		dashboard = new ClientDashboard();
		dashboard.setPercentageOfTotalNumberCompleteness25(percentageOfTotalNumberCompleteness25);
		dashboard.setPercentageOfTotalNumberCompleteness25At50(percentageOfTotalNumberCompleteness25At50);
		dashboard.setPercentageOfTotalNumberCompleteness50at75(percentageOfTotalNumberCompleteness50at75);
		dashboard.setPercentageOfTotalNumberCompletenessMore75(percentageOfTotalNumberCompletenessMore75);
		dashboard.setAverageBudgetOfContract(averageBudgetOfContract);
		dashboard.setDeviationBudgetOfContract(deviationBudgetOfContract);
		dashboard.setMinimumBudgetOfContract(minimumBudgetOfContract);
		dashboard.setMaximumBudgetOfContract(maximumBudgetOfContract);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"percentageOfTotalNumberCompleteness25", "percentageOfTotalNumberCompleteness25At50", // 
			"percentageOfTotalNumberCompleteness50at75", "percentageOfTotalNumberCompletenessMore75", //
			"averageBudgetOfContract", "deviationBudgetOfContract", //
			"minimumBudgetOfContract", "maximumBudgetOfContract");

		super.getResponse().addData(dataset);
	}

}
