
package acme.entities.contract;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.roles.client.Client;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Contract extends AbstractEntity {

	// Serialisation Identifier --------------------------------

	private static final long	serialVersionUID	= 1L;

	// Properties ----------------------------------------------

	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@Column(unique = true)
	private String				Code;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationMoment;

	@NotBlank
	@Length(max = 75)
	private String				customerName;

	@NotBlank
	@Length(max = 75)
	private String				providerName;

	@NotBlank
	@Length(max = 100)
	private String				goals;

	@NotNull
	@Valid
	private Money				budget;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Client				client;

}
