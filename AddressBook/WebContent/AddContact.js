function addContact() {
	var addReq = new XMLHttpRequest();
	var addUrl = "http://localhost:8787/AddressBook/contacts?reqFor=add&firstName=" + $('input[name="addFirstName"]').val() + "&middleName="
			+ $('input[name="addMiddleName"]').val() + "&lastName=" + $('input[name="addLastName"]').val() + "&addressHome="
			+ $('input[name="addAddressHome"]').val() + "&cityHome=" + $('input[name="addCityHome"]').val() + "&stateHome="
			+ $('input[name="addStateHome"]').val() + "&zipHome=" + $('input[name="addZipHome"]').val() + "&addressWork="
			+ $('input[name="addAddressWork"]').val() + "&cityWork=" + $('input[name="addCityWork"]').val() + "&stateWork="
			+ $('input[name="addStateWork"]').val() + "&zipWork=" + $('input[name="addZipWork"]').val() + "&addressOther="
			+ $('input[name="addAddressOther"]').val() + "&cityOther=" + $('input[name="addCityOther"]').val() + "&stateOther="
			+ $('input[name="addStateOther"]').val() + "&zipOther=" + $('input[name="addZipOther"]').val() + "&areaCodeHome="
			+ $('input[name="addAreaCodeHome"]').val() + "&numberHome=" + $('input[name="addNumberHome"]').val() + "&areaCodeWork="
			+ $('input[name="addAreaCodeWork"]').val() + "&numberWork=" + $('input[name="addNumberWork"]').val() + "&areaCodeCell="
			+ $('input[name="addAreaCodeCell"]').val() + "&numberCell=" + $('input[name="addNumberCell"]').val() + "&areaCodeOther="
			+ $('input[name="addAreaCodeOther"]').val() + "&numberOther=" + $('input[name="addNumberOther"]').val() + "&dateBirthday="
			+ $('input[name="addDateBirthday"]').val() + "&dateAnniversary=" + $('input[name="addDateAnniversary"]').val() + "&dateOther="
			+ $('input[name="addDateOther"]').val();
	addReq.open('POST', addUrl, false);
	addReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	addReq.send();
	$("#myAddModal").modal('hide');
}