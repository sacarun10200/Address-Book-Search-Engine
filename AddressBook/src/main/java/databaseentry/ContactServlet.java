package databaseentry;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

/**
 * Servlet implementation class ContactServlet
 */
@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ContactServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DatabaseOperations databaseOperations = new DatabaseOperations();
		List<AddressBook> contacts = databaseOperations.search(request.getParameter("searchText"));
		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(contacts));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("reqFor").equals("add")) {
			addContact(request);
		} else if (request.getParameter("reqFor").equals("edit")) {
			editContact(request);
		} else if (request.getParameter("reqFor").equals("delete")) {
			deleteContact(request);
		}

	}

	private void deleteContact(HttpServletRequest request) {
		int contactId = Integer.parseInt(request.getParameter("contactId"));
		DatabaseOperations databaseOperations = new DatabaseOperations();
		databaseOperations.delete(contactId);
	}

	private void editContact(HttpServletRequest request) {

		AddressBook addressBook = new AddressBook();

		String contactString = request.getParameter("contact");
		String addressString = request.getParameter("addressList");
		String phoneString = request.getParameter("phoneList");
		String dateString = request.getParameter("dateList");

		JSONObject contactObject = new JSONObject(contactString);

		Contact contact = new Contact();
		contact.setContactId(contactObject.getInt("contactId"));
		contact.setFirstName(contactObject.getString("firstName"));
		contact.setMiddleName(contactObject.getString("middleName"));
		contact.setLastName(contactObject.getString("lastName"));
		addressBook.setContact(contact);

		List<Address> addressList = new ArrayList<>();
		if (addressString != null) {
			String[] addresses = addressString.split("}");
			for (int i = 0; i < addresses.length - 1; i++) {
				JSONObject addressObject = new JSONObject(addresses[i].substring(1).concat("}"));
				if (addressObject.has("addressType") && !addressObject.getString("addressType").equals("null")) {
					Address address = new Address();
					address.setContactId(contactObject.getInt("contactId"));
					if (addressObject.has("addressId"))
						address.setAddressId(addressObject.getInt("addressId"));
					address.setAddressType(addressObject.getString("addressType"));
					if (addressObject.has("address") && !addressObject.getString("address").equals("null"))
						address.setAddress(addressObject.getString("address"));
					if (addressObject.has("city") && !addressObject.getString("city").equals("null"))
						address.setCity(addressObject.getString("city"));
					if (addressObject.has("state") && !addressObject.getString("state").equals("null"))
						address.setState(addressObject.getString("state"));
					if (addressObject.has("zip") && !addressObject.getString("zip").equals("null"))
						address.setZip(addressObject.getString("zip"));
					addressList.add(address);
				}
			}
		}
		addressBook.setAddressList(addressList);

		List<Phone> phoneList = new ArrayList<>();
		if (phoneString != null) {
			String[] phones = phoneString.split("}");
			for (int i = 0; i < phones.length - 1; i++) {
				JSONObject phoneObject = new JSONObject(phones[i].substring(1).concat("}"));
				if (phoneObject.has("phoneType") && !phoneObject.getString("phoneType").equals("null")) {
					Phone phone = new Phone();
					phone.setContactId(contactObject.getInt("contactId"));
					if (phoneObject.has("phoneId"))
						phone.setPhoneId(phoneObject.getInt("phoneId"));
					phone.setPhoneType(phoneObject.getString("phoneType"));
					if (phoneObject.has("areaCode") && phoneObject.get("areaCode") != null
							&& !String.valueOf(phoneObject.get("areaCode")).equals("null")) {
						phone.setAreaCode(phoneObject.getInt("areaCode"));
					}
					if (phoneObject.has("number") && !phoneObject.getString("number").equals("null"))
						phone.setNumber(phoneObject.getString("number"));
					phoneList.add(phone);
				}
			}
		}
		addressBook.setPhoneList(phoneList);

		List<Date> dateList = new ArrayList<>();
		if (dateString != null) {
			String[] dates = dateString.split("}");
			for (int i = 0; i < dates.length - 1; i++) {
				JSONObject dateObject = new JSONObject(dates[i].substring(1).concat("}"));
				if (dateObject.has("dateType") && !dateObject.getString("dateType").equals("null")) {
					Date date = new Date();
					date.setContactId(contactObject.getInt("contactId"));
					if (dateObject.has("dateId"))
						date.setDateId(dateObject.getInt("dateId"));
					date.setDateType(dateObject.getString("dateType"));
					if (dateObject.has("date")) {
						if (dateObject.get("date").getClass() == Long.class) {
							Long dateLong = dateObject.getLong("date");
							date.setDate(new Timestamp(dateLong));
						} else if (dateObject.get("date").getClass() == String.class) {
							String modifiedDate = dateObject.getString("date");
							if (modifiedDate != null && !modifiedDate.equals("null")
									&& !modifiedDate.equals("Invalid Date") && !modifiedDate.isEmpty()) {
								final String OLD_FORMAT = "yyyy-MM-dd";
								final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
								String oldDateString = modifiedDate;
								String newDateString;

								DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
								java.util.Date d = null;
								try {
									d = formatter.parse(oldDateString);
									((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
									newDateString = formatter.format(d);
									date.setDate(Timestamp.valueOf(newDateString));
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
					}

					dateList.add(date);
				}
			}
		}
		addressBook.setDateList(dateList);

		DatabaseOperations databaseOperations = new DatabaseOperations();
		databaseOperations.edit(addressBook);
	}

	private void addContact(HttpServletRequest request) {
		AddressBook addressBook = new AddressBook();

		Contact contact = new Contact();
		contact.setFirstName(request.getParameter("firstName"));
		contact.setMiddleName(request.getParameter("middleName"));
		contact.setLastName(request.getParameter("lastName"));
		addressBook.setContact(contact);

		List<Address> addressList = new ArrayList<>();

		Address address = new Address();
		address.setAddressType("Home");
		address.setAddress(request.getParameter("addressHome"));
		address.setCity(request.getParameter("cityHome"));
		address.setState(request.getParameter("stateHome"));
		address.setZip(request.getParameter("zipHome"));
		addressList.add(address);

		address = new Address();
		address.setAddressType("Work");
		address.setAddress(request.getParameter("addressWork"));
		address.setCity(request.getParameter("cityWork"));
		address.setState(request.getParameter("stateWork"));
		address.setZip(request.getParameter("zipWork"));
		addressList.add(address);

		address = new Address();
		address.setAddressType("Other");
		address.setAddress(request.getParameter("addressOther"));
		address.setCity(request.getParameter("cityOther"));
		address.setState(request.getParameter("stateOther"));
		address.setZip(request.getParameter("zipOther"));
		addressList.add(address);

		addressBook.setAddressList(addressList);

		List<Phone> phoneList = new ArrayList<>();

		Phone phone = new Phone();
		phone.setPhoneType("Home");
		String areaCodeHome = request.getParameter("areaCodeHome");
		if (areaCodeHome != null && !areaCodeHome.equals("null") && !areaCodeHome.equals("")) {
			phone.setAreaCode(Integer.parseInt(areaCodeHome));
		}
		phone.setNumber(request.getParameter("numberHome"));
		phoneList.add(phone);

		phone = new Phone();
		phone.setPhoneType("Work");
		String areaCodeWork = request.getParameter("areaCodeWork");
		if (areaCodeWork != null && !areaCodeWork.equals("null") && !areaCodeWork.equals("")) {
			phone.setAreaCode(Integer.parseInt(areaCodeWork));
		}
		phone.setNumber(request.getParameter("numberWork"));
		phoneList.add(phone);

		phone = new Phone();
		phone.setPhoneType("Cell");
		String areaCodeCell = request.getParameter("areaCodeCell");
		if (areaCodeCell != null && !areaCodeCell.equals("null") && !areaCodeCell.equals("")) {
			phone.setAreaCode(Integer.parseInt(areaCodeCell));
		}
		phone.setNumber(request.getParameter("numberCell"));
		phoneList.add(phone);

		phone = new Phone();
		phone.setPhoneType("Other");
		String areaCodeOther = request.getParameter("areaCodeOther");
		if (areaCodeOther != null && !areaCodeOther.equals("null") && !areaCodeOther.equals("")) {
			phone.setAreaCode(Integer.parseInt(areaCodeOther));
		}
		phone.setNumber(request.getParameter("numberOther"));
		phoneList.add(phone);

		addressBook.setPhoneList(phoneList);

		List<Date> dateList = new ArrayList<>();

		Date date = new Date();
		date.setDateType("Birthday");
		String dateBirthdayString = request.getParameter("dateBirthday");
		if (dateBirthdayString != null && !dateBirthdayString.equals("null")
				&& !dateBirthdayString.equals("Invalid Date") && !dateBirthdayString.isEmpty())
			date.setDate(getDate(dateBirthdayString));
		dateList.add(date);

		date = new Date();
		date.setDateType("Anniversary");
		String dateAnniversaryString = request.getParameter("dateAnniversary");
		if (dateAnniversaryString != null && !dateAnniversaryString.equals("null")
				&& !dateAnniversaryString.equals("Invalid Date") && !dateAnniversaryString.isEmpty())
			date.setDate(getDate(dateAnniversaryString));
		dateList.add(date);

		date = new Date();
		date.setDateType("Other");
		String dateOtherString = request.getParameter("dateOther");
		if (dateOtherString != null && !dateOtherString.equals("null") && !dateOtherString.equals("Invalid Date")
				&& !dateOtherString.isEmpty())
			date.setDate(getDate(dateOtherString));
		dateList.add(date);

		addressBook.setDateList(dateList);

		DatabaseOperations databaseOperations = new DatabaseOperations();
		databaseOperations.add(addressBook);
	}

	private Timestamp getDate(String date) {
		final String OLD_FORMAT = "yyyy-MM-dd";
		final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
		String oldDateString = date;
		String newDateString;

		DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
		java.util.Date d = null;
		try {
			d = formatter.parse(oldDateString);
			((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
			newDateString = formatter.format(d);
			return Timestamp.valueOf(newDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
