
package acme.entities.projects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserStory extends AbstractEntity {

	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@Digits(integer = 3, fraction = 2)
	@PositiveOrZero
	private double				estimatedCost;

	@NotBlank
	@Length(max = 75)
	private String				acceptanceCriteria;

	@NotNull
	private prioType			priorityType;

	@URL
	private String				link;

	@ManyToOne(optional = false)
	@Valid
	private Project				project;

}
