
package acme.features.auditor.codeAudits;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.CodeAudits;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditsRepository extends AbstractRepository {

	@Query("select ua from UserAccount u where u.id = id")
	UserAccount findOneUserAccountById(Integer id);

	@Query("select code, executionDate, type, proposedCorrectiveActions, mark, link from CodeAudits c where c.auditor = auditor")
	Collection<CodeAudits> findAllCodeAuditsByAuditor(Auditor auditor);

	@Query("select a from CodeAudits a where a.id = id")
	CodeAudits findOneCodeAuditsById(int id);

	@Query("select count(a) from CodeAudits a where a.id = id")
	int findNumberCodeAuditsByAuditorId(int id);

}
