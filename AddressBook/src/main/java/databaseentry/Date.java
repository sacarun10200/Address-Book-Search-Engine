package databaseentry;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Date {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "date_id")
	private int dateId;

	@Column(name = "contact_id")
	private int contactId;

	@Column(name = "date_type")
	private String dateType;

	@Column(name = "date")
	private Timestamp date;

	public Date(int contactId, String dateType, Timestamp date) {
		this.contactId = contactId;
		this.dateType = dateType;
		this.date = date;
	}

	public Date() {

	}

	public int getDateId() {
		return dateId;
	}

	public void setDateId(int dateId) {
		this.dateId = dateId;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
