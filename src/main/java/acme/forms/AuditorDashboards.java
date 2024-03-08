
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboards extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberCodeAudits;
	double						averageAuditorDashboards;
	double						desviationAuditorDashboards;
	double						minimumAuditorDashboardsRecords;
	double						maximmAuditorDashboardsRecords;
	double						minimumAuditorDashboardsPeriodLength;
	double						maximumAuditorDashboardsPeriodLength;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
