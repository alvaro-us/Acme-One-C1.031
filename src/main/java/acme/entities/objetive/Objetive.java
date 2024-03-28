
package acme.entities.objetive;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Objetive extends AbstractEntity {

	// Serialisation Identifier --------------------------------

	private static final long	serialVersionUID	= 1L;

	// Properties ----------------------------------------------

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private Priority			priority;

	private boolean				status;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				durationStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				durationEnd;

	@URL
	private String				link;
}
