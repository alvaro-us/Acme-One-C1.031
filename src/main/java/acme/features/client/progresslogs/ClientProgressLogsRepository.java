
package acme.features.client.progresslogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLogs;

@Repository
public interface ClientProgressLogsRepository extends AbstractRepository {

	@Query("select pl from ProgressLogs pl where pl.contract.id = :id")
	Collection<ProgressLogs> findProgressLogsByContract(int id);

	@Query("select pl from ProgressLogs pl where pl.recordId = :id")
	ProgressLogs findProgressLogsByRecordId(String id);

	@Query("select pl from ProgressLogs pl where pl.id = :id")
	ProgressLogs findProgressLogsById(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl.contract from ProgressLogs pl where pl.id = :id")
	Contract findOneContractByProgressLogId(int id);

	@Query("select pl from ProgressLogs pl where pl.id = :id")
	ProgressLogs findOneProgressLogById(int id);

	@Query("select pl from ProgressLogs pl where pl.recordId = :recordId")
	ProgressLogs findOneProgressLogsByRecordId(String recordId);

	@Query("select max(pg.completeness) from ProgressLogs pg where pg.contract.id = :contractId and pg.published = true")
	Double findPublishedProgressLogWithMaxCompletenessPublished(int contractId);

}
