
package acme.features.sponsor.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int sponsorId;
		SponsorDashboard dashboard;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		dashboard = new SponsorDashboard();

		dashboard.setTotalNumberOfInvoicesWithTaxLessThanOrEqual21Percent(this.repository.getTotalNumberOfInvoicesWithTaxLessThanOrEqual21Percent(sponsorId));
		dashboard.setTotalNumberOfSponsorshipsWithLink(this.repository.getTotalNumberOfSponsorshipsWithLink(sponsorId));
		dashboard.setAverageAmountOfSponsorships(this.repository.getAverageAmountOfSponsorships(sponsorId));
		dashboard.setDeviationAmountOfSponsorships(this.repository.getDeviationAmountOfSponsorships(sponsorId));
		dashboard.setMinAmountOfSponsorships(this.repository.getMinAmountOfSponsorships(sponsorId));
		dashboard.setMaxAmountOfSponsorships(this.repository.getMaxAmountOfSponsorships(sponsorId));
		dashboard.setAverageQuantityOfInvoices(this.repository.getAverageQuantityOfInvoices(sponsorId));
		dashboard.setDeviationQuantityOfInvoices(this.repository.getDeviationQuantityOfInvoices(sponsorId));
		dashboard.setMinQuantityOfInvoices(this.repository.getMinQuantityOfInvoices(sponsorId));
		dashboard.setMaxQuantityOfInvoices(this.repository.getMaxQuantityOfInvoices(sponsorId));

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final SponsorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfInvoicesWithTaxLessThanOrEqual21Percent", "totalNumberOfSponsorshipsWithLink", "averageAmountOfSponsorships", "deviationAmountOfSponsorships", "minAmountOfSponsorships", "maxAmountOfSponsorships",
			"averageQuantityOfInvoices", "deviationQuantityOfInvoices", "minQuantityOfInvoices", "maxQuantityOfInvoices");

		super.getResponse().addData(dataset);
	}

}
