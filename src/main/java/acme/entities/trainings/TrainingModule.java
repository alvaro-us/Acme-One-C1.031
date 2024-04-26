
package acme.entities.trainings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.roles.Developer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TrainingModule extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-\\d{3}$", message = "{validation.trainingModule.code}")
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateMoment;

	@NotBlank
	@Length(max = 100)
	private String				details;

	@NotNull
	private DifficultyType		difficultyType;

	@URL
	@Length(max = 255)
	private String				link;

	private int					estimatedTotalTime;

	private boolean				draftMode;

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Project				project;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Developer			developer;

}
