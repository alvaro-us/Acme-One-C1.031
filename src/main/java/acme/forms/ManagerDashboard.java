
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberOfMustUserStory;
	int							totalNumberOfShouldUserStory;
	int							totalNumberOfCouldUserStory;
	int							totalNumberOfWontUserStory;

	double						averageEstimatedCostUserStory;
	double						deviationEstimatedCostUserStory;
	int							minEstimatedCostUserStory;
	int							maxEstimatedCostUserStory;

	double						averageCostProject;
	double						deviationCostProject;
	int							minCostProject;
	int							maxCostProject;

}
