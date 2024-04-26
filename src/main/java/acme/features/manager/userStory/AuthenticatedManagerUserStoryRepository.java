
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerUserStoryRepository extends AbstractRepository {

	@Query("SELECT  a FROM Project a WHERE a.id = :id")
	Project findProjectById(int id);

	@Query("SELECT  a FROM UserStory a WHERE a.id = :id")
	UserStory findUserStoryById(int id);

	@Query("SELECT a.userStory FROM Assignment a WHERE a.project.id = :id")
	Collection<UserStory> findAllUserStoryOfProject(int id);

	@Query("SELECT a FROM UserStory a WHERE a.manager.id = :id")
	Collection<UserStory> findAllUserStoryOfManager(int id);

	@Query("SELECT a FROM Assignment a WHERE a.userStory.id = :id")
	Collection<Assignment> findAllAssignmentsOfUserStory(int id);

	@Query("SELECT a FROM Manager a WHERE a.id = :id")
	Manager findManagerById(int id);

	@Query("select count(a) from Assignment a where a.userStory.id = :id")
	int findNumberAssignmentOfUserStory(int id);

}
