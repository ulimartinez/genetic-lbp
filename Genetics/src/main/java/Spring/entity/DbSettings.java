package Spring.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DbSettings {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotEmpty(message = "Driver field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String driver;
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String schema;
	@NotEmpty(message = "Host field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String host;
	@NotEmpty(message = "Port field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String port;
	@NotEmpty(message = "Database Name field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String dbName;
	@NotEmpty(message = "Password field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String password;
	@NotEmpty(message = "Username field must not be empty")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	private String username;

	public DbSettings() {
	}

	public DbSettings(String driver, String host, String port, String dbName, String username, String password) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.password = password;
		this.username = username;
	}

	public DbSettings(String driver, String host, String port, String dbName, String username, String password, String schema) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.password = password;
		this.username = username;
		this.schema = schema;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
