package databaseentry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DatabaseOperations {

	private static SessionFactory factory;

	public static void main(String[] args) {
	}

	public DatabaseOperations() {
		initializeFactorySession();
	}
	
	private void initializeFactorySession() {
		try {
			factory = new Configuration().addAnnotatedClass(Contact.class).addAnnotatedClass(Address.class)
					.addAnnotatedClass(Phone.class).addAnnotatedClass(Date.class).configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public void delete(int contactId) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			String query = "select addressId from Address where contactId =" + contactId;
			List addresses = session.createQuery(query).list();
			System.out.println(addresses.size());
			if (addresses != null) {
				for (int i = 0; i < addresses.size(); i++) {
					Integer addressId = (Integer) addresses.get(i);
					Address ad = session.get(Address.class, addressId);
					session.delete(ad);
				}
			}
			query = "select phoneId from Phone where contactId =" + contactId;
			List phones = session.createQuery(query).list();
			System.out.println(phones.size());
			if (phones != null) {
				for (int i = 0; i < phones.size(); i++) {
					Integer phoneId = (Integer) phones.get(i);
					Phone ph = session.get(Phone.class, phoneId);
					session.delete(ph);
				}
			}
			query = "select dateId from Date where contactId =" + contactId;
			List dates = session.createQuery(query).list();
			System.out.println(dates.size());
			if (dates != null) {
				for (int i = 0; i < dates.size(); i++) {
					Integer dateId = (Integer) dates.get(i);
					Date da = session.get(Date.class, dateId);
					session.delete(da);
				}
			}
			query = "select contactId from Contact where contactId =" + contactId;
			List contacts = session.createQuery(query).list();
			System.out.println(contacts.size());
			if (contacts != null) {
				for (int i = 0; i < contacts.size(); i++) {
					Integer id = (Integer) contacts.get(i);
					Contact c = session.get(Contact.class, id);
					session.delete(c);
				}
			}

			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public void edit(AddressBook modifiedContact) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Contact contact = session.get(Contact.class, modifiedContact.getContact().getContactId());
			contact.setFirstName(modifiedContact.getContact().getFirstName());
			contact.setMiddleName(modifiedContact.getContact().getMiddleName());
			contact.setLastName(modifiedContact.getContact().getLastName());
			session.update(contact);

			for (Address address : modifiedContact.getAddressList()) {
				Address add = session.get(Address.class, address.getAddressId());
				if (add != null) {
					add.setAddressType(address.getAddressType());
					add.setAddress(address.getAddress());
					add.setCity(address.getCity());
					add.setState(address.getState());
					add.setZip(address.getZip());
					session.update(add);
				} else
					session.save(address);
			}
			for (Phone phone : modifiedContact.getPhoneList()) {
				Phone ph = session.get(Phone.class, phone.getPhoneId());
				if (ph != null) {
					ph.setPhoneType(phone.getPhoneType());
					ph.setAreaCode(phone.getAreaCode());
					ph.setNumber(phone.getNumber());
					session.update(ph);
				} else
					session.save(phone);
			}
			for (Date date : modifiedContact.getDateList()) {
				Date d = session.get(Date.class, date.getDateId());
				if (d != null) {
					d.setDateType(date.getDateType());
					d.setDate(date.getDate());
					session.update(d);
				} else
					session.save(date);
			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public void add(AddressBook addressBook) {

		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			session.save(addressBook.getContact());

			String query = "SELECT MAX(contactId) FROM Contact";
			int contactId = (int) session.createQuery(query).list().get(0);

			List<Address> addresses = addressBook.getAddressList();
			for (Address address : addresses) {
				address.setContactId(contactId);
				session.save(address);
			}

			List<Phone> phones = addressBook.getPhoneList();
			for (Phone phone : phones) {
				phone.setContactId(contactId);
				session.save(phone);
			}

			List<Date> dates = addressBook.getDateList();
			for (Date date : dates) {
				date.setContactId(contactId);
				session.save(date);
			}

			tx.commit();
		} catch (

		HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public List<AddressBook> search(String searchText) {

		List<AddressBook> searchResults = new ArrayList<>();

		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			String query = "SELECT c.contactId, c.firstName, c.middleName, c.lastName, a.addressId, a.addressType, a.address, a.city, a.state, a.zip, p.phoneId, p.phoneType,p.areaCode, p.number, d.dateId, d.dateType, d.date FROM Contact c left outer join Address a on c.contactId=a.contactId left outer join Phone p on c.contactId=p.contactId left outer join Date d on c.contactId=d.contactId";
			if (!searchText.isEmpty()) {
				query = query.concat(" WHERE ");
				String[] searchTexts = searchText.split(" ");
				for (int i = 0; i < searchTexts.length; i++) {
					String text = searchTexts[i];
					query = query.concat("c.firstName LIKE '%").concat(text).concat("%' OR c.middleName LIKE '%")
							.concat(text).concat("%' OR c.lastName LIKE '%").concat(text)
							.concat("%' OR a.address like '%").concat(text).concat("%' OR a.city like '%").concat(text)
							.concat("%' OR a.state like '%").concat(text).concat("%' OR a.zip like '%").concat(text)
							.concat("%' OR p.number like '%").concat(text).concat("%'");
					if (i + 1 < searchTexts.length)
						query = query.concat(" OR ");
				}
			}
			List contacts = session.createQuery(query).list();
			List<Integer> alreadyAddedContacts = new ArrayList<>();
			for (int count = 0; count < contacts.size(); count++) {

				Object[] obj = (Object[]) contacts.get(count);
				int contactId = (Integer) obj[0];
				if (alreadyAddedContacts.contains(contactId)) {
					continue;
				}

				AddressBook addressBook = new AddressBook();
				alreadyAddedContacts.add(contactId);
				String contactQuery = "SELECT contactId, firstName, middleName, lastName FROM Contact WHERE contactId="
						+ contactId;
				List contactDetails = session.createQuery(contactQuery).list();
				obj = (Object[]) contactDetails.get(0);
				Contact contact = new Contact();
				contact.setContactId(Integer.parseInt(String.valueOf(obj[0])));
				contact.setFirstName(String.valueOf(obj[1]));
				contact.setMiddleName(String.valueOf(obj[2]));
				contact.setLastName(String.valueOf(obj[3]));
				addressBook.setContact(contact);

				String addressQuery = "SELECT addressId, addressType, address, city, state, zip FROM Address WHERE contactId="
						+ contactId;
				List addressDetails = session.createQuery(addressQuery).list();
				List<Address> addressList = new ArrayList<>();
				for (int i = 0; i < addressDetails.size(); i++) {
					obj = (Object[]) addressDetails.get(i);
					Address address = new Address();
					address.setAddressId(Integer.parseInt(String.valueOf(obj[0])));
					address.setAddressType(String.valueOf(obj[1]));
					address.setAddress(String.valueOf(obj[2]));
					address.setCity(String.valueOf(obj[3]));
					address.setState(String.valueOf(obj[4]));
					address.setZip(String.valueOf(obj[5]));
					addressList.add(address);
				}
				addressBook.setAddressList(addressList);

				String phoneQuery = "SELECT phoneId, phoneType, areaCode, number FROM Phone WHERE contactId="
						+ contactId;
				List phoneDetails = session.createQuery(phoneQuery).list();
				List<Phone> phoneList = new ArrayList<>();
				for (int i = 0; i < phoneDetails.size(); i++) {
					obj = (Object[]) phoneDetails.get(i);
					Phone phone = new Phone();
					phone.setPhoneId(Integer.parseInt(String.valueOf(obj[0])));
					phone.setPhoneType(String.valueOf(obj[1]));
					if (obj[2] != null && String.valueOf(obj[2]) != null && !String.valueOf(obj[2]).equals("null"))
						phone.setAreaCode(Integer.parseInt(String.valueOf(obj[2])));
					phone.setNumber(String.valueOf(obj[3]));
					phoneList.add(phone);
				}
				addressBook.setPhoneList(phoneList);

				String dateQuery = "SELECT dateId, dateType, date FROM Date WHERE contactId=" + contactId;
				List dateDetails = session.createQuery(dateQuery).list();
				List<Date> dateList = new ArrayList<>();
				for (int i = 0; i < dateDetails.size(); i++) {
					obj = (Object[]) dateDetails.get(i);
					Date date = new Date();
					date.setDateId(Integer.parseInt(String.valueOf(obj[0])));
					date.setDateType(String.valueOf(obj[1]));
					if (obj[2] != null && String.valueOf(obj[2]) != null && !String.valueOf(obj[2]).equals("null"))
						date.setDate(Timestamp.valueOf(String.valueOf(obj[2])));
					dateList.add(date);
				}
				addressBook.setDateList(dateList);
				searchResults.add(addressBook);
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return searchResults;
	}

}
