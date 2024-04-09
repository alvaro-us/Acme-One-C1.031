
package acme.features.manager.projects;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerProjectRepository extends AbstractRepository {

	@Query("SELECT a FROM Project a WHERE a.manager.id = :id")
	Collection<Project> findAllProjectsOfManager(int id);

	@Query("SELECT a FROM Project a")
	Collection<Project> findAllProjects();

	@Query("SELECT  a FROM Project a WHERE a.id = :id")
	Project findProjectById(int id);

	@Query("SELECT a FROM Manager a WHERE a.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT a FROM Assignment a WHERE a.project.id = :id")
	Collection<Assignment> findAllAssignmentsOfProject(int id);

}
