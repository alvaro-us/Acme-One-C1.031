
package acme.components;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface BannerResposotory extends AbstractRepository {

	@Query("select count(b) from Banner b WHERE b.startDisplayPeriod < :currentDate AND b.endDisplayPeriod > :currentDate")
	int countCurrentBanner(Date currentDate);

	@Query("select b from Banner b WHERE b.startDisplayPeriod < :currentDate AND b.endDisplayPeriod > :currentDate")
	List<Banner> findManyCurrentBanners(PageRequest pRequest, Date currentDate);

	default Banner findRandomBanner() {
		Banner res;
		Date currentDate = MomentHelper.getCurrentMoment();
		int acum = this.countCurrentBanner(currentDate);
		int index = RandomHelper.nextInt(0, acum);
		PageRequest page;
		List<Banner> listBanners;
		if (acum == 0)
			res = null;
		else {
			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			listBanners = this.findManyCurrentBanners(page, currentDate);
			res = listBanners.isEmpty() ? null : listBanners.get(0);
		}
		return res;
	}
}
