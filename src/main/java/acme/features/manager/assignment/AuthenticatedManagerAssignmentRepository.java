
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;

@Repository
public interface AuthenticatedManagerAssignmentRepository extends AbstractRepository {

	@Query("SELECT a FROM Assignment a")
	Collection<Assignment> findAllAssignments();

	@Query("SELECT  a FROM Assignment a WHERE a.id = :id")
	Assignment findAssignmentById(int id);

	@Query("SELECT  a FROM Project a WHERE a.id = :id")
	Project findProjectById(int id);

	@Query("SELECT  a FROM UserStory a WHERE a.id = :id")
	UserStory findUserStorytById(int id);

	@Query("SELECT  a FROM UserStory a")
	Collection<UserStory> findUserStories();

	@Query("SELECT  a FROM Project a WHERE a.draftMode = true")
	Collection<Project> findProjects();
}
