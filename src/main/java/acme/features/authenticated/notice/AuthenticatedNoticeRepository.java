
package acme.features.authenticated.notice;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.notices.Notice;

@Repository
public interface AuthenticatedNoticeRepository extends AbstractRepository {

	@Query("SELECT b FROM Notice b where b.instMoment > :limit")
	Collection<Notice> listing(Date limit);

	@Query("SELECT b FROM Notice b")
	Collection<Notice> findAllNotices();

	@Query("SELECT b FROM Notice b WHERE b.id = :id")
	Notice findNoticeById(int id);

	@Query("SELECT a from UserAccount a where a.id = :id")
	UserAccount findUserAccountById(int id);

}
