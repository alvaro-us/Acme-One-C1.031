
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {
	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes

	int							totalNumberOfTrainingModulesWithUpdateMoment;
	int							totalNumberOfTrainingSessionsWithLink;

	double						averageTimeTrainingModule;
	double						deviationTimeTrainingModule;
	double						minTimeTrainingModule;
	double						maxTimeTrainingModule;

}
