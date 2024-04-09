
package acme.features.any.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;

@Repository
public interface AnyProjectRepository extends AbstractRepository {

	@Query("SELECT a FROM Project a WHERE a.draftMode = false")
	Collection<Project> findAllProjectsPublished();

	@Query("SELECT a FROM Project a WHERE a.id = :id")
	Project findProjectById(int id);

}
