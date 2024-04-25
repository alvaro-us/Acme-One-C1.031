
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Banner;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	@Autowired
	protected AdministratorBannerRepository repository;


	@Override
	public void authorise() {
		final boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Banner banner = this.repository.findBannerById(id);
		final Date date = MomentHelper.getCurrentMoment();
		banner.setInstallationDate(date);
		super.getBuffer().addData(banner);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;
		super.bind(object, "startDisplayPeriod", "endDisplayPeriod", "pictureLink", "slogan");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;
		final String START_PERIOD = "startDisplayPeriod";
		final String END_PERIOD = "endDisplayPeriod";
		if (!super.getBuffer().getErrors().hasErrors(START_PERIOD) && !super.getBuffer().getErrors().hasErrors(END_PERIOD)) {
			final boolean b = MomentHelper.isAfter(object.getEndDisplayPeriod(), object.getStartDisplayPeriod());
			super.state(b, END_PERIOD, "administrator.banner.form.error.end-before-start");
			if (b) {
				final boolean b2 = MomentHelper.isLongEnough(object.getStartDisplayPeriod(), object.getEndDisplayPeriod(), 7, ChronoUnit.DAYS);
				super.state(b2, END_PERIOD, "administrator.banner.form.error.small-display-period");
			}
			if (!super.getBuffer().getErrors().hasErrors("instantiation")) {
				final boolean b3 = MomentHelper.isAfterOrEqual(object.getStartDisplayPeriod(), object.getEndDisplayPeriod());
				super.state(b3, START_PERIOD, "administrator.banner.form.error.start-before-instantiation");
			}
		}
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		Dataset ds = super.unbind(object, "startDisplayPeriod", "endDisplayPeriod", "slogan", "pictureLink", "url");
		ds.put("instantiation", object.getInstallationDate());
		super.getResponse().addData(ds);
	}
}
