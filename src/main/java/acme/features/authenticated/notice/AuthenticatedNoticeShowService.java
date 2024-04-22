
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
public class AuthenticatedNoticeShowService extends AbstractService<Authenticated, Notice> {

	@Autowired
	protected AuthenticatedNoticeRepository repository;


	@Override
	public void authorise() {
		Collection<Notice> objects;
		Date limite;
		Boolean authorised;
		int id;
		Notice note2;
		limite = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);

		objects = this.repository.listing(limite);
		id = super.getRequest().getData("id", int.class);
		note2 = this.repository.findNoticeById(id);
		authorised = objects.contains(note2) && super.getRequest().getPrincipal().isAuthenticated();

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Notice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findNoticeById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "instMoment", "message", "author", "link", "email");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		super.getResponse().addData(dataset);
	}

}
