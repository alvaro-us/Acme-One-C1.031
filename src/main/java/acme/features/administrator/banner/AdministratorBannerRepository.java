
package acme.features.administrator.banner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Banner;

@Repository
public interface AdministratorBannerRepository extends AbstractRepository {

	@Query("select b from Banner b")
	List<Banner> findAllBanners();

	@Query("select b from Banner b where b.id = :id")
	Banner findBannerById(int id);
}
