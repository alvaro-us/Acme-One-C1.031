
package acme.features.sponsor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Sponsor;

@Repository
public interface AuthenticatedSponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findSponsorById(int id);

	@Query("SELECT count(i) FROM Invoice i WHERE i.tax <= 0.21 AND i.sponsorship.sponsor.id = :id")
	int getTotalNumberOfInvoicesWithTaxLessThanOrEqual21Percent(int id);

	@Query("SELECT count(s) FROM Sponsorship s WHERE s.link != null AND s.sponsor.id = :id")
	int getTotalNumberOfSponsorshipsWithLink(int id);

	@Query("SELECT avg(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id")
	double getAverageAmountOfSponsorships(int id);

	@Query("SELECT stddev(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id")
	double getDeviationAmountOfSponsorships(int id);

	@Query("SELECT min(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id")
	int getMinAmountOfSponsorships(int id);

	@Query("SELECT max(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id")
	int getMaxAmountOfSponsorships(int id);

	@Query("SELECT avg(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id")
	double getAverageQuantityOfInvoices(int id);

	@Query("SELECT stddev(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id")
	double getDeviationQuantityOfInvoices(int id);

	@Query("SELECT min(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id")
	int getMinQuantityOfInvoices(int id);

	@Query("SELECT max(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id")
	int getMaxQuantityOfInvoices(int id);

}
