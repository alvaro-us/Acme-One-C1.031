
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.datatypes.SponsorshipType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	// Serialisation identifier -------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Propieties ---------------------------------------------------------------

	@NotBlank
	@NotNull
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@Column(unique = true)
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	private Date				moment;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date				DurationStart;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date				DurationEnd;

	@NotNull
	@Min(0)
	private Double				amount;

	@NotNull
	private SponsorshipType		type;

	@Email
	private String				email;

	@URL
	private String				link;

}
