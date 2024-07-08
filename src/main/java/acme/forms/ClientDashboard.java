
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

	Integer						percentageOfTotalNumberCompleteness25;
	Integer						percentageOfTotalNumberCompleteness25At50;
	Integer						percentageOfTotalNumberCompleteness50at75;
	Integer						percentageOfTotalNumberCompletenessMore75;
	Double						averageBudgetOfContract;
	Double						deviationBudgetOfContract;
	Double						minimumBudgetOfContract;
	Double						maximumBudgetOfContract;

}
