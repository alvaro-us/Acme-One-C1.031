
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends AbstractEntity {

	// Serialisation identifier -------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Propieties ---------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "IN-[0-9]{4}-[0-9]{4}")
	private String				code;

	@NotNull
	@Past
	private Date				registrationTime;

	@NotNull
	private Date				dueDate;

	@NotNull
	@Min(0)
	private int					quantity;

	@NotNull
	@Min(0)
	private Double				tax;

	@URL
	private String				link;

	// Derived attributes -------------------------------------------------------


	@Transient
	public Double getTotalAmount() {
		return this.tax + this.quantity;
	}

	// Relationships


	@Valid
	@NotNull
	@ManyToOne(optional = true)
	private Sponsorship sponsorship;

}
