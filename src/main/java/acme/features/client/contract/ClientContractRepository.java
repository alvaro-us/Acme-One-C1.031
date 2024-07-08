
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;
import acme.entities.projects.Project;
import acme.roles.client.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.userAccount.id = :id")
	Collection<Contract> findContractsByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select p from Project p where p.id in (select id from Contract c where c.project.id = :id)")
	Project findProjectByContract(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findPublishedProjects();

	@Query("select p from Project p where p.code = :code")
	Project findprojectByCode(String code);

	@Query("select c from Contract c where c.code = :code")
	Contract findContractByCode(String code);

	@Query("select c.project from Contract c where c.client.id = :clientId")
	Collection<Project> findManyProjectsByClientId(int clientId);

	@Query("select pl from ProgressLogs pl where pl.contract.id = :id")
	Collection<ProgressLogs> findProgressLogsByContract(int id);

	@Query("select c from Client c where c.id = :id")
	Client findOneClientById(int id);

	@Query("select c from Contract c where c.project.id = :id")
	Collection<Contract> findContractsFromProject(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select c from Configuration c")
	Configuration findConfiguration();

	@Query("select c from Contract c where c.code = :code")
	Contract findOneContractByCode(String code);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAllProjectsPublished();
}
