
package acme.forms;

import java.util.Map;

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

	Map<String, Double>			averageCostProject;
	Map<String, Double>			deviationCostProject;
	Map<String, Integer>		minCostProject;
	Map<String, Integer>		maxCostProject;

}
