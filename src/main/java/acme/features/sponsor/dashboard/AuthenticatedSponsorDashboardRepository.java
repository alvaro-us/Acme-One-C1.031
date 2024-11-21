
package acme.features.sponsor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Sponsor;

@Repository
public interface AuthenticatedSponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findSponsorById(int id);

	@Query("SELECT COALESCE(count(i), 0) FROM Invoice i WHERE i.draftMode = false AND i.tax <= 0.21 AND i.sponsorship.sponsor.id = :id")
	int getTotalNumberOfInvoicesWithTaxLessThanOrEqual21Percent(int id);

	@Query("SELECT COALESCE(count(s), 0) FROM Sponsorship s WHERE s.draftMode = false AND s.link <> '' AND s.sponsor.id = :id")
	int getTotalNumberOfSponsorshipsWithLink(int id);

	@Query("SELECT COALESCE(avg(s.amount.amount), 0) FROM Sponsorship s WHERE s.draftMode = false AND s.sponsor.id = :id")
	double getAverageAmountOfSponsorships(int id);

	@Query("SELECT COALESCE(stddev(s.amount.amount), 0) FROM Sponsorship s WHERE s.draftMode = false AND s.sponsor.id = :id")
	double getDeviationAmountOfSponsorships(int id);

	@Query("SELECT COALESCE(min(s.amount.amount), 0) FROM Sponsorship s WHERE s.draftMode = false AND s.sponsor.id = :id")
	int getMinAmountOfSponsorships(int id);

	@Query("SELECT COALESCE(max(s.amount.amount), 0) FROM Sponsorship s WHERE s.draftMode = false AND s.sponsor.id = :id")
	int getMaxAmountOfSponsorships(int id);

	@Query("SELECT COALESCE(avg(i.quantity.amount), 0) FROM Invoice i WHERE i.draftMode = false AND i.sponsorship.sponsor.id = :id")
	double getAverageQuantityOfInvoices(int id);

	@Query("SELECT COALESCE(stddev(i.quantity.amount), 0) FROM Invoice i WHERE i.draftMode = false AND i.sponsorship.sponsor.id = :id")
	double getDeviationQuantityOfInvoices(int id);

	@Query("SELECT COALESCE(min(i.quantity.amount), 0) FROM Invoice i WHERE i.draftMode = false AND i.sponsorship.sponsor.id = :id")
	int getMinQuantityOfInvoices(int id);

	@Query("SELECT COALESCE(max(i.quantity.amount), 0) FROM Invoice i WHERE i.draftMode = false AND i.sponsorship.sponsor.id = :id")
	int getMaxQuantityOfInvoices(int id);

}
