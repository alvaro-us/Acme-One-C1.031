
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT a FROM Manager a WHERE a.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id")
	int getTotalNumberOfUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM Project a WHERE a.manager.id = :id")
	int getTotalNumberOfProject(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'MUST'")
	int getTotalNumberOfMustUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'SHOULD'")
	int getTotalNumberOfShouldUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'COULD'")
	int getTotalNumberOfCouldUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 'WONT'")
	int getTotalNumberOfWontUserStory(int id);

	@Query("SELECT COALESCE(avg(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	double getAverageEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(stddev(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	double getDeviationEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(min(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	int getMinEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(max(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	int getMaxEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(avg(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id")
	double getAverageCostProject(int id);

	@Query("SELECT COALESCE(stddev(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id")
	double getDeviationCostProject(int id);

	@Query("SELECT COALESCE(min(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id")
	int getMinCostProject(int id);

	@Query("SELECT COALESCE(max(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id")
	int getMaxCostProject(int id);

}
