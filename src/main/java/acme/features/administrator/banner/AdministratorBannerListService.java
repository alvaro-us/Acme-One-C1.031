
package acme.features.administrator.banner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerListService extends AbstractService<Administrator, Banner> {

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
		Collection<Banner> banners = this.repository.findAllBanners();
		super.getBuffer().addData(banners);
	}

	@Override
	public void unbind(final Banner b) {
		assert b != null;
		Dataset ds = super.unbind(b, "startDisplayPeriod", "endDisplayPeriod", "slogan");
		super.getResponse().addData(ds);
	}
}
