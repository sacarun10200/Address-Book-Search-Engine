package databaseentry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contact {

	@Id
	@Column(name = "contact_id")
	private int contactId;

	@Column(name = "fname")
	private String firstName;

	@Column(name = "mname")
	private String middleName;

	@Column(name = "lname")
	private String lastName;

	public Contact(int contactId, String firstName, String middleName, String lastName) {
		this.contactId = contactId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	public Contact() {

	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
