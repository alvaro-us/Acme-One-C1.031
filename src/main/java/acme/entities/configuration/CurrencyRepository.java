
package acme.entities.configuration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CurrencyRepository extends AbstractRepository {

	@Query("select c from Configuration c")
	Configuration findSystemConfiguration();

}
