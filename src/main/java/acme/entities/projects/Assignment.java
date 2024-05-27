
package acme.entities.projects;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {
	@Index(columnList = "project_id, user_story_id")
})

public class Assignment extends AbstractEntity {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	@ManyToOne(optional = false)
	@Valid
	private Project				project;

	@ManyToOne(optional = false)
	@Valid
	private UserStory			userStory;

}
