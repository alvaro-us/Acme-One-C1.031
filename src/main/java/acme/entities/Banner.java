
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
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

	@Past
	private Date				installationDate;

	@Past
	private Date				modificationDate;

	private Date				displayPeriod;

	private String				pictureLink;

	@Length(max = 77)
	private String				slogan;

	@URL
	private String				url;
}
