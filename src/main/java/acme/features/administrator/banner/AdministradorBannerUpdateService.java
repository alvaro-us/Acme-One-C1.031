
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministradorBannerUpdateService extends AbstractService<Administrator, Banner> {

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
		super.bind(b, "instantiationMoment", "startDisplayPeriod", "endDisplayPeriod", "slogan", "picture", "url");
	}

	@Override
	public void validate(final Banner b) {
		assert b != null;

		final String PERIOD_START = "startDisplayPeriod";
		final String PERIOD_END = "endDisplayPeriod";
		final String INSTANTIATION = "instatiationMoment";
		if (b.getInstantiationMoment() != null && b.getStartDisplayPeriod() != null && b.getEndDisplayPeriod() != null && !super.getBuffer().getErrors().hasErrors(PERIOD_START) && !super.getBuffer().getErrors().hasErrors(INSTANTIATION)) {
			final boolean startAfterInstantiation = MomentHelper.isAfter(b.getStartDisplayPeriod(), b.getInstantiationMoment());
			super.state(startAfterInstantiation, PERIOD_START, "administrator.banner.form.error.start-before-instantiation");
		}

		if (b.getInstantiationMoment() != null && b.getStartDisplayPeriod() != null && b.getEndDisplayPeriod() != null && !super.getBuffer().getErrors().hasErrors(PERIOD_END) && !super.getBuffer().getErrors().hasErrors(INSTANTIATION)) {
			final boolean startBeforeEnd = MomentHelper.isAfter(b.getEndDisplayPeriod(), b.getStartDisplayPeriod());
			super.state(startBeforeEnd, PERIOD_END, "administrator.banner.form.error.end-before-start");

			if (startBeforeEnd) {
				final boolean startOneWeekBeforeEndMinimum = MomentHelper.isLongEnough(b.getStartDisplayPeriod(), b.getEndDisplayPeriod(), 7, ChronoUnit.DAYS);

				super.state(startOneWeekBeforeEndMinimum, PERIOD_END, "administrator.banner.form.error.small-display-period");
			}
		}
	}

	@Override
	public void perform(final Banner b) {
		assert b != null;
		this.repository.save(b);
	}

	@Override
	public void unbind(final Banner b) {
		assert b != null;
		Dataset ds = super.unbind(b, "instantiationMoment", "startDisplayPeriod", "endDisplayPeriod", "slogan", "picture", "url");
		super.getResponse().addData(ds);
	}
}
