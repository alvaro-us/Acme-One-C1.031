
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation identifier -------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Properties ---------------------------------------------------------------

	@NotBlank
	@NotNull
	@Column(unique = true)
	@Pattern(regexp = "C-[0-9]{4}")
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	private Date				instantiationMoment;

	@NotNull
	@NotBlank
	@Size(max = 76)
	private String				heading;

	@NotBlank
	@Size(max = 101)
	@NotNull
	private String				description;

	@NotBlank
	@Size(max = 101)
	@NotNull
	private String				department;

	@Email
	private String				email;

	@URL
	private String				link;

}
