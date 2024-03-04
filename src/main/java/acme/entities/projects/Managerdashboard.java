
package acme.entities.projects;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Managerdashboard extends AbstractForm {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberOfMustUserStory;
	int							totalNumberOfShouldUserStory;
	int							totalNumberOfCouldUserStory;
	int							totalNumberOfWontUserStory;

	double						averageEstimatedCostUserStory;
	double						deviationEstimatedCostUserStory;
	double						miniEstimatedCostUserStory;
	double						maxEstimatedCostUserStory;

	double						averageCostProject;
	double						deviationCostProject;
	double						minCostProject;
	double						maxCostProject;

}
