
package acme.features.developer.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuthenticatedDeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(t) from TrainingModule t where t.updateMoment is not null and t.developer.id = :id and t.draftMode = false")
	Integer getTotalNumberOfTrainingModulesWithUpdateMoment(int id);

	@Query("select count(s) from TrainingSession s where s.link is not null and s.trainingModule.developer.id = :id and s.draftMode = false")
	Integer getTotalNumberOfTrainingSessionsWithLink(int id);

	@Query("select avg(t.estimatedTotalTime) from TrainingModule t where t.developer.id = :id and t.draftMode = false")
	Double getAverageTimeTrainingModule(int id);

	@Query("select stddev(t.estimatedTotalTime) from TrainingModule t where t.developer.id = :id and t.draftMode = false")
	Double getDeviationTimeTrainingModule(int id);

	@Query("select min(t.estimatedTotalTime) from TrainingModule t where t.developer.id = :id and t.draftMode = false")
	Double getMinTimeTrainingModule(int id);

	@Query("select max(t.estimatedTotalTime) from TrainingModule t where t.developer.id = :id and t.draftMode = false")
	Double getMaxTimeTrainingModule(int id);

}
