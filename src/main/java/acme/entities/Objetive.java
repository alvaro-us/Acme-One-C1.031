
package acme.entities;

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
import acme.datatypes.Priority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Objetive extends AbstractEntity {

	// Serialisation Identifier --------------------------------

	private static final long	serialVersionUID	= 1L;

	// Properties ----------------------------------------------

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationMoment;

	@NotNull
	@NotBlank
	private String				title;

	@NotNull
	@NotBlank
	@Length(max = 101)
	private String				description;

	@NotNull
	private Priority			priority;

	@NotNull
	private Boolean				status;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationEnd;

	@URL
	private String				link;
}
