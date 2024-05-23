
package acme.features.authenticated.notice;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.notices.Notice;

@Service
public class AuthenticatedNoticeListService extends AbstractService<Authenticated, Notice> {

	@Autowired
	protected AuthenticatedNoticeRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Notice> objects;
		Date limite;

		limite = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);

		objects = this.repository.listing(limite);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "title", "author");
		super.getResponse().addData(dataset);
	}

}
