
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface AuthenticatedSponsorInvoiceRepository extends AbstractRepository {

	@Query("SELECT  s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findSponsorshipById(int id);

	@Query("SELECT  i FROM Invoice i WHERE i.code = :code")
	Sponsorship findInvoiceByCode(int code);

	@Query("SELECT  i FROM Invoice i WHERE i.id = :id")
	Invoice findInvoiceById(int id);

	@Query("SELECT  i FROM Invoice i WHERE i.code = :code")
	Invoice findInvoiceByCode(String code);

	@Query("SELECT i FROM Invoice i WHERE i.sponsorship.id = :id")
	Collection<Invoice> findAllInvoiceOfSponsorship(int id);

	@Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :id")
	Collection<Sponsorship> findAllSponsorshipOfSponsor(int id);

	@Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :id AND s.draftMode = true")
	Collection<Sponsorship> findAllSponsorshipOfSponsorPublished(int id);

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findSponsorById(int id);

	@Query("SELECT s FROM Sponsor s WHERE s.userAccount.id = :id")
	Sponsor findSponsorByUserId(int id);

}
