
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;

public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<String, Integer>		totalNumberOfPrincipals;
	Double						ratioOfNoticesWithEmail;
	Double						ratioOfNoticesWithLink;
	Double						ratioOfCriticalObjetives;
	Double						ratioOfNonCriticalObjetives;
	Double						averageValueOfRisk;
	Double						mimimumValueOfRisk;
	Double						maximumValueOfRisk;
	Double						StandardDesviationValueOfRisk;
	Double						averageNumberOfClaims;
	Double						mimimumNumberOfClaims;
	Double						maximumNumberOfClaims;
	Double						StandardDesviationNumberOfClaims;
}
