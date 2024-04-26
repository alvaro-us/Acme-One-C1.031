
package acme.entities.configuration;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CurrencyRatioRepository extends AbstractRepository {

	@Query("SELECT c from CurrencyRatio c")
	public Collection<CurrencyRatio> findAllCurrencyRatio();

	@Query("SELECT c from CurrencyRatio c WHERE c.fromCurrency = :fromCurrency AND c.toCurrency = :toCurrency")
	public CurrencyRatio findCurrencyRatioFromTo(String fromCurrency, String toCurrency);

}
