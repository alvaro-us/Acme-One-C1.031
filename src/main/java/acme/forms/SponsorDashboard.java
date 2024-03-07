
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberOfInvoicesWithTaxLessThanOrEqual21Percent;
	int							totalNumberOfSponsorshipsWithLink;
	double						averageAmountOfSponsorships;
	double						deviationAmountOfSponsorships;
	int							minAmountOfSponsorships;
	int							maxAmountOfSponsorships;
	double						averageQuantityOfInvoices;
	double						deviationQuantityOfInvoices;
	int							minQuantityOfInvoices;
	int							maxQuantityOfInvoices;

}
