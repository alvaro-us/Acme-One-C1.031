
package acme.features.any.claim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Claim;

@Service
public class AnyClaimCreateService extends AbstractService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository repository;

	// AbstractService<Authenticated, Sponsor> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim object;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		object = new Claim();
		object.setInstantiationMoment(instantiationMoment);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link");
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Claim claim;

			claim = this.repository.findClaimByCode(object.getCode());
			super.state(claim == null, "code", "any.claim.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {

		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "heading", "instantiationMoment", "description", "department", "email", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
