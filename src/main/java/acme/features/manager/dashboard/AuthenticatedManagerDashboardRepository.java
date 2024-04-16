
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT a FROM Manager a WHERE a.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT count(a) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'MUST'")
	int getTotalNumberOfMustUserStory(int id);

	@Query("SELECT count(a) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'SHOULD'")
	int getTotalNumberOfShouldUserStory(int id);

	@Query("SELECT count(a) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'COULD'")
	int getTotalNumberOfCouldUserStory(int id);

	@Query("SELECT count(a) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'WONT'")
	int getTotalNumberOfWontUserStory(int id);

	@Query("SELECT avg(a.estimatedCost) FROM UserStory a WHERE a.manager.id = :id")
	double getAverageEstimatedCostUserStory(int id);

	@Query("SELECT stddev(a.estimatedCost) FROM UserStory a WHERE a.manager.id = :id")
	double getDeviationEstimatedCostUserStory(int id);

	@Query("SELECT min(a.estimatedCost) FROM UserStory a WHERE a.manager.id = :id")
	int getMinEstimatedCostUserStory(int id);

	@Query("SELECT max(a.estimatedCost) FROM UserStory a WHERE a.manager.id = :id")
	int getMaxEstimatedCostUserStory(int id);

	@Query("SELECT avg(a.cost) FROM Project a WHERE a.manager.id = :id")
	double getAverageCostProject(int id);

	@Query("SELECT stddev(a.cost) FROM Project a WHERE a.manager.id = :id")
	double getDeviationCostProject(int id);

	@Query("SELECT min(a.cost) FROM Project a WHERE a.manager.id = :id")
	int getMinCostProject(int id);

	@Query("SELECT max(a.cost) FROM Project a WHERE a.manager.id = :id")
	int getMaxCostProject(int id);

}
