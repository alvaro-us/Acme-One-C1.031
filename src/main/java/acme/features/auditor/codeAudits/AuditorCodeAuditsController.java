
package acme.features.auditor.codeAudits;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.CodeAudits;
import acme.roles.Auditor;

@Controller
public class AuditorCodeAuditsController extends AbstractController<Auditor, CodeAudits> {

	@Autowired
	private AuditorCodeAuditsListAllService	listAllService;

	@Autowired
	private AuditorCodeAuditsShowService		showService;

	@Autowired
	private AuditorCodeAuditsCreateService	createService;

	@Autowired
	private AuditorCodeAuditsUpdateService	updateService;

	@Autowired
	private AuditorCodeAuditsDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("list-all", this.listAllService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
