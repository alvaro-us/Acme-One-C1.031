
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Pattern(regexp = "AU-[0-9]{4}-[0-9]{3}")
	@Column(unique = true)
	private String				code;

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				auditPeriodStart;

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				auditPeriodEnd;

	@NotNull
	private AuditRecordType		mark;

	private boolean				published;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	private CodeAudits			codeAudits;

	@URL
	@Length(max = 255)
	private String				furtherInformation;
}
