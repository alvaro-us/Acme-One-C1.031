
package acme.entities.Audit;

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
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class CodeAudits extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				executionDate;

	protected EnumType			type;

	@NotBlank
	@Length(max = 100)
	private String				proposedCorrectiveActions;

	private Double				mark;

	@URL
	private String				link;

	// Relationships ----------------------------------------------------------
	@ManyToOne(optional = false)
	@NotNull
	@Valid
	private Auditor				auditor;

	private boolean				published;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	private Project				project;
}
