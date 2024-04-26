
package acme.features.auditor.codeAudits;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.Audit.AuditRecordType;
import acme.entities.Audit.CodeAudits;
import acme.entities.projects.Project;
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

	@Query("select c from CodeAudit c where c.auditor.id = :id")
	Collection<CodeAudits> findCodeAuditByAuditorId(int id);

	@Query("select ad.mark from AuditRecord ad where ad.codeAudit.id=:codeAuditId")
	Collection<AuditRecordType> findMarksByCodeAuditId(int id);

	default AuditRecordType averageMark(final List<AuditRecordType> ls) {
		double average = ls.stream().mapToDouble(m -> this.changeMarkToNumber(m)).average().orElse(0);
		AuditRecordType res = this.changeNumberToMark(average);
		return res;
	}

	default AuditRecordType changeNumberToMark(final double media) {
		AuditRecordType res = AuditRecordType.C;
		double average = Math.round(media);
		if (average < 1)
			res = AuditRecordType.FMINUS;
		else if (average < 2 && average >= 1)
			res = AuditRecordType.F;
		else if (average < 3 && average >= 2)
			res = AuditRecordType.C;
		else if (average < 4 && average >= 3)
			res = AuditRecordType.B;
		else if (average < 5 && average >= 4)
			res = AuditRecordType.A;
		else
			res = AuditRecordType.APLUS;
		return res;
	}

	default int changeMarkToNumber(final AuditRecordType mark) {
		int res = 0;

		if (mark == AuditRecordType.FMINUS)
			res = 0;
		else if (mark == AuditRecordType.F)
			res = 1;
		else if (mark == AuditRecordType.C)
			res = 2;
		else if (mark == AuditRecordType.B)
			res = 3;
		else if (mark == AuditRecordType.A)
			res = 4;
		else
			res = 5;

		return res;
	}
	@Query("select p from Project p")
	Collection<Project> findAllProjects();

}
