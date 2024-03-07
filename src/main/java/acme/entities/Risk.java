
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Risk extends AbstractEntity {
	// Serialisation Identifier

	private static final long	serialVersionUID	= 1L;

	// Atributes

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-\\d{3}", message = "La referencia debe seguir el patrón R-XXX")
	private String				reference;

	@NotNull
	@Past
	private Date				identificationDate;

	@PositiveOrZero
	@NotNull
	private Double				impact;

	@NotNull
	@Min(0)
	@Max(1)
	private Double				probability;

	@NotNull
	private Double				value;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	private String				link;

}
