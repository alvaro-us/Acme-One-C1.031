
package acme.features.administrator.banner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Banner;

@Service
public class AdministratorBannerListService extends AbstractService<Administrator, Banner> {

	@Autowired
	protected AdministratorBannerRepository repository;


	@Override
	public void authorise() {
		final boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Banner> banners = this.repository.findAllBanners();
		super.getBuffer().addData(banners);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		Dataset ds = super.unbind(object, "startDisplayPeriod", "endDisplayPeriod", "slogan", "pictureLink", "url");
		super.getResponse().addData(ds);
	}
}
