package databaseentry;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class DatabaseEntry {

	private List<Contact> contactList;
	private List<Address> addressList;
	private List<Phone> phoneList;
	private List<Date> dateList;

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public List<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public List<Date> getDateList() {
		return dateList;
	}

	public void setDateList(List<Date> dateList) {
		this.dateList = dateList;
	}

	public static void main(String[] args) {
		try {

			DatabaseEntry entry = new DatabaseEntry();
			String fileName = "Contacts.csv";
			entry.readCsvFileLineByLine(fileName);
			Connection conn = getConnection();
			persistContactWithDataClass(conn, entry.getContactList());
			persistAddressWithDataClass(conn, entry.getAddressList());
			persistPhoneWithDataClass(conn, entry.getPhoneList());
			persistDateWithDataClass(conn, entry.getDateList());

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void readCsvFileLineByLine(String fileName) throws IOException {
		List<Contact> contactList = new ArrayList<>();
		List<Address> addressList = new ArrayList<>();
		List<Phone> phoneList = new ArrayList<>();
		List<Date> dateList = new ArrayList<>();
		String[] nextLine;
		CSVReader reader = new CSVReader(new FileReader(fileName), ',', '\"', 1);
		while ((nextLine = reader.readNext()) != null) {
			contactList.add(new Contact(Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2], nextLine[3]));
			if (!(nextLine[6].isEmpty() && nextLine[7].isEmpty() && nextLine[8].isEmpty() && nextLine[9].isEmpty()))
				addressList.add(new Address(Integer.parseInt(nextLine[0]), "Home", nextLine[6], nextLine[7],
						nextLine[8], nextLine[9]));
			if (!(nextLine[11].isEmpty() && nextLine[12].isEmpty() && nextLine[13].isEmpty() && nextLine[14].isEmpty()))
				addressList.add(new Address(Integer.parseInt(nextLine[0]), "Work", nextLine[11], nextLine[12],
						nextLine[13], nextLine[14]));
			if (!nextLine[4].isEmpty())
				phoneList.add(new Phone(Integer.parseInt(nextLine[0]), "Home", nextLine[4]));
			if (!nextLine[5].isEmpty())
				phoneList.add(new Phone(Integer.parseInt(nextLine[0]), "Cell", nextLine[5]));
			if (!nextLine[10].isEmpty())
				phoneList.add(new Phone(Integer.parseInt(nextLine[0]), "Work", nextLine[10]));
			String birthdate = nextLine[15];
			if (birthdate.length() > 0) {

				final String OLD_FORMAT = "yyyy-MM-dd";
				final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				String oldDateString = birthdate;
				String newDateString;

				DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
				java.util.Date d = null;
				try {
					d = formatter.parse(oldDateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
				newDateString = formatter.format(d);
				dateList.add(new Date(Integer.parseInt(nextLine[0]), "Birthday", Timestamp.valueOf(newDateString)));
			}
		}
		this.contactList = contactList;
		this.addressList = addressList;
		this.phoneList = phoneList;
		this.dateList = dateList;
	}

	public static Connection getConnection() {
		Connection conn = null; // sets values to null before actually attempting a connection
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connectionStringURL = "jdbc:mysql://localhost:3306/contacts"; // database
																					// name
			conn = DriverManager.getConnection(connectionStringURL, "root", "root"); // username, password
			if (conn == null) // check to make sure that it actually connected
				System.out.println("Connection Failed");
			else
				System.out.println("Success");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	private static void persistContactWithDataClass(Connection conn, List<Contact> contactList) throws SQLException {
		String insertStatement = " insert into Contact (contact_id,fname,mname,lname) values ( ?, ?, ?, ?)";
		PreparedStatement preparedStmt = conn.prepareStatement(insertStatement);
		for (Contact contact : contactList) {
			preparedStmt.setInt(1, contact.getContactId());
			preparedStmt.setString(2, contact.getFirstName());
			preparedStmt.setString(3, contact.getMiddleName());
			preparedStmt.setString(4, contact.getLastName());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();
	}

	private static void persistAddressWithDataClass(Connection conn, List<Address> addressList) throws SQLException {
		String insertStatement = " insert into Address (contact_id,address_type,address,city,state,zip) values ( ?, ?, ?, ?,?,?)";
		PreparedStatement preparedStmt = conn.prepareStatement(insertStatement);
		for (Address address : addressList) {
			preparedStmt.setInt(1, address.getContactId());
			preparedStmt.setString(2, address.getAddressType());
			preparedStmt.setString(3, address.getAddress());
			preparedStmt.setString(4, address.getCity());
			preparedStmt.setString(5, address.getState());
			preparedStmt.setString(6, address.getZip());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();
	}

	private static void persistPhoneWithDataClass(Connection conn, List<Phone> phoneList) throws SQLException {
		String insertStatement = " insert into Phone (contact_id,phone_type,number) values ( ?, ?, ?)";
		PreparedStatement preparedStmt = conn.prepareStatement(insertStatement);
		for (Phone phone : phoneList) {
			preparedStmt.setInt(1, phone.getContactId());
			preparedStmt.setString(2, phone.getPhoneType());
			preparedStmt.setString(3, phone.getNumber());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();
	}

	private static void persistDateWithDataClass(Connection conn, List<Date> dateList) throws SQLException {
		String insertStatement = " insert into Date (contact_id,date_type,date) values ( ?, ?, ?)";
		PreparedStatement preparedStmt = conn.prepareStatement(insertStatement);
		for (Date date : dateList) {
			preparedStmt.setInt(1, date.getContactId());
			preparedStmt.setString(2, date.getDateType());
			preparedStmt.setTimestamp(3, date.getDate());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();
	}

}
