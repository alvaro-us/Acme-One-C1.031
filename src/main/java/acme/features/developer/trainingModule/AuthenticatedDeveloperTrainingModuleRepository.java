
package acme.features.developer.trainingModule;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Repository
public interface AuthenticatedDeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select t from TrainingModule t")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select t from TrainingModule t where t.developer.id = :developerId")
	List<TrainingModule> findAllTrainigModulesByDeveloperId(int developerId);

	@Query("select t.details from TrainingModule t where t.developer.id = :developerId")
	List<String> findAllTrainingModulesDetailsByDeveloperId(int developerId);

	@Query("select t from TrainingModule t where t.id = :tId")
	TrainingModule findTrainingModuleById(int tId);

	@Query("select d from Developer d where d.id = :developerId")
	Developer findDeveloperById(int developerId);

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("select t from TrainingModule t where t.code = :tCode")
	TrainingModule findTrainingModuleByCode(String tCode);

	@Query("select s from TrainingSession s where s.trainingModule.id = :tId")
	Collection<TrainingSession> findAllTrainingSessionsByTrainingModuleId(int tId);
}
