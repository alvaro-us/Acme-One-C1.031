
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.AuditRecord;
import acme.entities.CodeAudits;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditsRecord a where a.codeAudit = id")
	Collection<AuditRecord> findAllAuditRecordByCodeAudits(CodeAudits id);

	@Query("select a from CodeAudits a where a.id = id")
	CodeAudits findCodeAuditbyId(Integer id);

	@Query("select count(a) from AuditRecord a where a.id = id ")
	AuditRecord findOneAuditRecordById(int id);

}
