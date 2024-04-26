
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

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 0")
	int getTotalNumberOfMustUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 1")
	int getTotalNumberOfShouldUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 2")
	int getTotalNumberOfCouldUserStory(int id);

	@Query("SELECT COALESCE(count(a), 0) FROM UserStory a WHERE a.manager.id = :id AND a.priorityType = 3")
	int getTotalNumberOfWontUserStory(int id);

	@Query("SELECT COALESCE(avg(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	double getAverageEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(stddev(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	double getDeviationEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(min(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	int getMinEstimatedCostUserStory(int id);

	@Query("SELECT COALESCE(max(a.estimatedCost), 0) FROM UserStory a WHERE a.manager.id = :id")
	int getMaxEstimatedCostUserStory(int id);

	//------------------- EUR

	@Query("SELECT COALESCE(avg(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'EUR'")
	double getAverageCostProjectEUR(int id);

	@Query("SELECT COALESCE(stddev(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'EUR'")
	double getDeviationCostProjectEUR(int id);

	@Query("SELECT COALESCE(min(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'EUR'")
	int getMinCostProjectEUR(int id);

	@Query("SELECT COALESCE(max(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'EUR'")
	int getMaxCostProjectEUR(int id);

	//------------------ GBP

	@Query("SELECT COALESCE(avg(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'GBP'")
	double getAverageCostProjectGBP(int id);

	@Query("SELECT COALESCE(stddev(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'GBP'")
	double getDeviationCostProjectGBP(int id);

	@Query("SELECT COALESCE(min(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'GBP'")
	int getMinCostProjectGBP(int id);

	@Query("SELECT COALESCE(max(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'GBP'")
	int getMaxCostProjectGBP(int id);

	//------------------ USD

	@Query("SELECT COALESCE(avg(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'USD'")
	double getAverageCostProjectUSD(int id);

	@Query("SELECT COALESCE(stddev(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'USD'")
	double getDeviationCostProjectUSD(int id);

	@Query("SELECT COALESCE(min(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'USD'")
	int getMinCostProjectUSD(int id);

	@Query("SELECT COALESCE(max(a.cost.amount), 0) FROM Project a WHERE a.manager.id = :id and a.cost.currency = 'USD'")
	int getMaxCostProjectUSD(int id);

}
