
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerAssignmentRepository extends AbstractRepository {

	@Query("SELECT a FROM Assignment a WHERE a.project.manager = :manager")
	Collection<Assignment> findAllAssignments(Manager manager);

	@Query("SELECT  a FROM Assignment a WHERE a.id = :id")
	Assignment findAssignmentById(int id);

	@Query("SELECT  a FROM Project a WHERE a.id = :id")
	Project findProjectById(int id);

	@Query("SELECT  a FROM UserStory a WHERE a.id = :id")
	UserStory findUserStoryById(int id);

	@Query("SELECT  a FROM UserStory a WHERE a.manager = :manager")
	Collection<UserStory> findUserStories(Manager manager);

	@Query("SELECT  a FROM Project a WHERE a.draftMode = true and a.manager = :manager")
	Collection<Project> findProjects(Manager manager);

	@Query("SELECT  a FROM Project a")
	Collection<Project> findAllProjects();

	@Query("SELECT  a FROM UserStory a")
	Collection<UserStory> findAllUserStories();

	@Query("SELECT a FROM Manager a WHERE a.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT count(a) FROM Assignment a WHERE a.project = :project AND a.userStory = :userStory")
	int existsAssignmentWithProjectAndUserStory(Project project, UserStory userStory);
}
