
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;

@Repository
public interface AuthenticatedDeveloperTrainingSessionsRepository extends AbstractRepository {

	@Query("select ts from TrainingSession ts")
	Collection<TrainingSession> findAllTrainingSessions();

	@Query("select tm from TrainingModule tm")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select ts from TrainingSession ts where ts.id = :id")
	TrainingSession findTrainingSessionById(int id);

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findAllTrainingSessionsByTMId(int id);

	@Query("select ts from TrainingSession ts where ts.code = :code")
	TrainingSession findTrainingSessionByCode(String code);

	@Query("select ts.trainingModule from TrainingSession ts where ts.id = :tsId")
	TrainingModule findTrainingModuleByTSId(int tsId);
}
