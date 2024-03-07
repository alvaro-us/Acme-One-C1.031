
package acme.entities.risks;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Risk extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-\\d{3}", message = "{validation.risk.reference}")
	private String				reference;

	@NotNull
	@Past
	private Date				identificationDate;

	@Positive
	private double				impact;

	@Min(0)
	@Max(1)
	private double				probability;

	@Transient
	private double				value;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	@Length(max = 255)
	private String				link;

	// Derived attributes -----------------------------------------------------


	@PostLoad // Este método se ejecuta después de que se carga una instancia desde la base de datos
	private void calculateValue() {
		this.value = this.impact * this.probability;
	}

}
