
package acme.entities.projects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
