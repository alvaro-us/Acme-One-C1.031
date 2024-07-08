
package acme.entities.progressLogs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import acme.client.data.AbstractEntity;
import acme.entities.contract.Contract;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProgressLogs extends AbstractEntity {

	// Serialisation Identifier --------------------------------

	private static final long	serialVersionUID	= 1L;

	// Properties ----------------------------------------------

	@NotBlank
	@Pattern(regexp = "PG-[A-Z]{1,2}-[0-9]{4}")
	@Column(unique = true)
	private String				recordId;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@NotBlank
	@Length(max = 100)
	private String				comment;

	@NotNull
	@Digits(fraction = 2, integer = 3)
	@Range(min = 0, max = 100)
	private double				completeness;

	@NotBlank
	@Length(max = 75, min = 0)
	private String				responsable;

	private boolean				published;

	// Relationships ----------------------------------------------

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Contract			contract;
}
