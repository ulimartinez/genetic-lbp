package Spring.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Ontology {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstName;
	private String lastName;
	private String email;

	private String databaseName;
	private String extractedOntology;
	private String mappingsOntology;
	private String mappingsDirectory;
	private String creationDateString;
	private Date creationDate;
	@ManyToMany
	private List<Skill> skills;

	public Ontology() {
		super();
	}

	public Ontology(String databaseName, String extractedOntology, String mappingsOntology, String mappingsDirectory, Date creationDate) {
		super();
		this.databaseName = databaseName;
		this.extractedOntology = extractedOntology;
		this.mappingsOntology = mappingsOntology;
		this.mappingsDirectory = mappingsDirectory;
		this.creationDate = creationDate;
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - HH:mm:ss");
		this.creationDateString = sdf.format(creationDate);
	}

	public Ontology(String firstName, String lastName, String email,
	                List<Skill> skills) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.skills = skills;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getExtractedOntology() {
		return extractedOntology;
	}

	public void setExtractedOntology(String extractedOntology) {
		this.extractedOntology = extractedOntology;
	}

	public String getMappingsOntology() {
		return mappingsOntology;
	}

	public void setMappingsOntology(String mappingsOntology) {
		this.mappingsOntology = mappingsOntology;
	}

	public String getMappingsDirectory() {
		return mappingsDirectory;
	}

	public void setMappingsDirectory(String mappingsDirectory) {
		this.mappingsDirectory = mappingsDirectory;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDateString() {
		return creationDateString;
	}

	public void setCreationDateString(String creationDateString) {
		this.creationDateString = creationDateString;
	}
}
