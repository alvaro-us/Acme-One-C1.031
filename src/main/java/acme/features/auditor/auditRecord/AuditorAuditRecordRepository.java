
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Audit.AuditRecord;
import acme.entities.Audit.CodeAudits;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditRecord a where a.codeAudit.id = :id")
	Collection<AuditRecord> findAuditRecordsByCodeAuditId(int id);

	@Query("select a from AuditRecord a where a.codeAudit.auditor.id = :id")
	Collection<AuditRecord> findAuditRecordsByAuditorId(int id);

	@Query("select a from CodeAudits a where a.id = :id")
	CodeAudits findCodeAuditbyId(Integer id);

	@Query("select a from AuditRecord a where a.id = :id")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select c from CodeAudit c")
	Collection<CodeAudits> findAllCodeAudits();

	@Query("select a from AuditRecord a where a.code = :code")
	AuditRecord findAuditRecordByCode(String code);

}
