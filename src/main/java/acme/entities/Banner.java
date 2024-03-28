
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				installationDate;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				modificationDate;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startDisplayPeriod;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endDisplayPeriod;

	@NotNull
	@URL
	private String				pictureLink;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@NotNull
	@URL
	private String				url;
}
