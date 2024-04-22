
package acme.features.administrator.configuration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;

@Repository
public interface AuthenticatedAdministratorConfigurationRepository extends AbstractRepository {

	@Query("SELECT a FROM Configuration a")
	Configuration findConfiguration();

}
