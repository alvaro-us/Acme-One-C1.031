
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberOfProgressLogsBelow25Percent;
	int							totalNumberOfProgressLogs25To50Percent;
	int							totalNumberOfProgressLogs50To75Percent;
	int							totalNumberOfProgressLogsAbove75Percent;
	double						averageBudgetOfContracts;
	double						deviationBudgetOfContracts;
	double						minBudgetOfContracts;
	double						maxBudgetOfContracts;

}
