
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface AuthenticatedSponsorSponsorshipRepository extends AbstractRepository {

	@Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :id")
	Collection<Sponsorship> findAllSponsorshipsOfSponsor(int id);

	@Query("SELECT s FROM Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("SELECT s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findSponsorshipById(int id);

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findSponsorById(int id);

}
