
package acme.features.client.progresslogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.progressLogs.ProgressLogs;
import acme.roles.client.Client;

@Controller
public class ClientProgressLogsController extends AbstractController<Client, ProgressLogs> {

	@Autowired
	protected ClientProgressLogsListService		listService;

	@Autowired
	protected ClientProgressLogsShowService		showService;

	@Autowired
	protected ClientProgressLogsCreateService	createService;

	@Autowired
	protected ClientProgressLogsUpdateService	updateService;

	@Autowired
	protected ClientProgressLogsPublishService	publishService;

	@Autowired
	protected ClientProgressLogsDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
