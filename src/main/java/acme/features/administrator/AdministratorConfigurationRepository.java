
package acme.features.administrator;

import acme.entities.configuration.Configuration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorConfigurationRepository extends AbstractRepository {

	@Query("SELECT c from Configuration c")
	Configuration findCurrentSystemConfiguration();

}
