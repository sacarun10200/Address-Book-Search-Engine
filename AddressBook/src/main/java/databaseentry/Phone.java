package databaseentry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Phone implements Serializable {

	private static final long serialVersionUID = 538909327607831632L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "phone_id")
	private int phoneId;

	@Column(name = "contact_id")
	private int contactId;

	@Column(name = "phone_type")
	private String phoneType;

	@Column(name = "area_code")
	private Integer areaCode;

	@Column(name = "number")
	private String number;

	public Phone(int contactId, String phoneType, Integer areaCode, String number) {
		this.contactId = contactId;
		this.phoneType = phoneType;
		this.areaCode = areaCode;
		this.number = number;
	}

	public Phone(int contactId, String phoneType, String number) {
		this.contactId = contactId;
		this.phoneType = phoneType;
		this.number = number;
	}

	public Phone() {

	}

	public int getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(int phoneId) {
		this.phoneId = phoneId;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
