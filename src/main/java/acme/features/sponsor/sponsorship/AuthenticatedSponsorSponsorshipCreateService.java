
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.CurrencyService;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorSponsorshipRepository	repository;

	@Autowired
	protected CurrencyService							service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsor sponsor;
		Sponsorship object;

		sponsor = this.repository.findSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Sponsorship();
		object.setMoment(MomentHelper.getCurrentMoment());
		object.setSponsor(sponsor);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project");

	}

	@Override
	public void validate(final Sponsorship object) {

		assert object != null;

		// Code

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Boolean codeDuplicated = this.repository.findSponsorshipByCode(object.getCode()) == null;
			super.state(codeDuplicated, "code", "sponsor.sponsorship.form.error.codeDuplicated");
		}

		// Date
		if (!super.getBuffer().getErrors().hasErrors("durationStart")) {
			Boolean momentBeforeDurationStart;
			momentBeforeDurationStart = MomentHelper.isAfter(object.getDurationStart(), MomentHelper.deltaFromMoment(object.getMoment(), 0l, ChronoUnit.DAYS));
			super.state(momentBeforeDurationStart, "durationStart", "sponsor.sponsorship.form.error.momentBeforeDurationStart");
			if (!super.getBuffer().getErrors().hasErrors("durationEnd")) {
				boolean durationStart1MothBeforeDurationEnd;
				durationStart1MothBeforeDurationEnd = MomentHelper.isAfter(object.getDurationEnd(), MomentHelper.deltaFromMoment(object.getDurationStart(), 1l, ChronoUnit.MONTHS));
				super.state(durationStart1MothBeforeDurationEnd, "durationEnd", "sponsor.sponsorship.form.error.durationStart1MothBeforeDurationEnd");
			}
		}

		// Amount
		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			boolean isAcceptedCurrency = this.service.isAcceptedCurrency(object.getAmount().getCurrency());
			boolean amountPositive = object.getAmount().getAmount() >= 0;
			super.state(amountPositive, "amount", "sponsor.sponsorship.form.error.amountPositive");
			super.state(isAcceptedCurrency, "amount", "sponsor.sponsorship.form.error.isAcceptedCurrency");
		}

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjectsPublished();

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());
		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project", "draftMode");

		dataset.put("types", choices);
		dataset.put("projects", choicesProjects);
		super.getResponse().addData(dataset);
	}

}
