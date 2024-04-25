
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.CurrencyService;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorSponsorshipRepository	repository;

	@Autowired
	protected CurrencyService							service;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		Sponsorship object;
		Sponsor sponsor;
		int id;
		int idUser;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findSponsorshipById(id);
		idUser = super.getRequest().getPrincipal().getAccountId();

		sponsor = this.repository.findSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(Sponsor.class) && object.getSponsor().equals(sponsor) && object.getSponsor().getUserAccount().getId() == idUser;

		super.getResponse().setAuthorised(status);
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
		SelectChoices choices;
		SelectChoices choicesProjects;
		Collection<Project> projects = this.repository.findAllProjectsPublished();

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());
		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "type", "email", "link", "project", "draftMode");

		Money amountBase = this.service.changeCurrencyToBase(object.getAmount());
		dataset.put("amountBase", amountBase);
		dataset.put("types", choices);
		dataset.put("projects", choicesProjects);
		super.getResponse().addData(dataset);

	}

}
