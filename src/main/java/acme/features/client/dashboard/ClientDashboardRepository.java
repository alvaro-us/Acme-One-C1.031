/*
 * AdministratorDashboardRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.client.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(pl) FROM ProgressLogs pl WHERE pl.completeness < 25")
	Integer totalNumberOfProgressLogsBelow25Percent();

	@Query("SELECT COUNT(pl) FROM ProgressLogs pl WHERE pl.completeness >= 25 AND pl.completeness < 50")
	Integer totalNumberOfProgressLogs25To50Percent();

	@Query("SELECT COUNT(pl) FROM ProgressLogs pl WHERE pl.completeness >= 50 AND pl.completeness < 75")
	Integer totalNumberOfProgressLogs50To75Percent();

	@Query("SELECT COUNT(pl) FROM ProgressLogs pl WHERE pl.completeness >= 75")
	Integer totalNumberOfProgressLogsAbove75Percent();

	@Query("SELECT AVG(c.budget.amount) FROM Contract c")
	Double averageBudgetOfContracts();

	@Query("SELECT STDDEV(c.budget.amount) FROM Contract c")
	Double deviationBudgetOfContracts();

	@Query("SELECT MIN(c.budget.amount) FROM Contract c")
	Double minBudgetOfContracts();

	@Query("SELECT MAX(c.budget.amount) FROM Contract c")
	Double maxBudgetOfContracts();

}
