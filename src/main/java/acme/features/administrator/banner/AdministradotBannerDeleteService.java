
package acme.features.administrator.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministradotBannerDeleteService extends AbstractService<Administrator, Banner> {

	// Internal state
	@Autowired
	protected AdministratorBannerRepository repository;


	@Override
	public void authorise() {
		final boolean state = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(state);
	}

	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		final Banner b = this.repository.findBannerById(id);
		super.getBuffer().addData(b);
	}

	@Override
	public void bind(final Banner b) {
		assert b != null;
		super.bind(b, "startDisplayPeriod", "endDisplayPeriod", "slogan", "picture", "url");
	}

	@Override
	public void validate(final Banner b) {
		assert b != null;
	}

	@Override
	public void perform(final Banner b) {
		assert b != null;
		this.repository.delete(b);
	}

	@Override
	public void unbind(final Banner b) {
		assert b != null;
		Dataset ds = super.unbind(b, "instantiationMoment", "startDisplayPeriod", "endDisplayPeriod", "slogan", "picture", "url");
		super.getResponse().addData(ds);
	}
}
