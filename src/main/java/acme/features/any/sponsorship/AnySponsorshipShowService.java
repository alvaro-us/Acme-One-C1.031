
package acme.features.any.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.CurrencyService;
import acme.entities.sponsorship.Sponsorship;

@Service
public class AnySponsorshipShowService extends AbstractService<Any, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnySponsorshipRepository	repository;

	@Autowired
	private CurrencyService				service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findSponsorshipById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link");
		dataset.put("sponsorName", object.getSponsor().getUserAccount().getUsername());
		dataset.put("project", object.getProject().getCode());
		Money amountBase = this.service.changeCurrencyToBase(object.getAmount());
		dataset.put("amountBase", amountBase);
		super.getResponse().addData(dataset);
	}

}
