
package acme.features.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;

@Service
public class AdministratorConfigurationUpdateService extends AbstractService<Administrator, Configuration> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorConfigurationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Configuration object;

		object = this.repository.findCurrentSystemConfiguration();

		super.getBuffer().addData(object);
	}
	@Override
	public void bind(final Configuration object) {
		assert object != null;

		super.bind(object, "currency", "acceptedCurrency");
	}

	@Override
	public void validate(final Configuration object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("currency") && !super.getBuffer().getErrors().hasErrors("acceptedCurrency")) {

			String[] acceptedCurrency = object.getAcceptedCurrency().split(",");

			boolean correctCurrency = false;
			boolean acceptedCurrencyUpperCase = true;
			for (String ac : acceptedCurrency) {
				if (object.getCurrency().equals(ac))
					correctCurrency = true;

				if (!ac.toUpperCase().equals(ac))
					acceptedCurrencyUpperCase = false;
			}
			boolean currencyUpperCase = object.getCurrency().toUpperCase().equals(object.getCurrency());

			super.state(correctCurrency, "currency", "administrator.configuration.form.error.correctCurrency");
			super.state(currencyUpperCase, "currency", "administrator.configuration.form.error.currencyUpperCase");
			super.state(acceptedCurrencyUpperCase, "acceptedCurrency", "administrator.configuration.form.error.acceptedCurrencyUpperCase");

		}

	}

	@Override
	public void perform(final Configuration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Configuration object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "currency", "acceptedCurrency");

		super.getResponse().addData(dataset);
	}

}
