function search() {
	$("#resultTable").removeClass("hidden");
	var noOfRows = document.getElementById("resultTable").rows.length;
	for (var i = 1; i < noOfRows; i++) {
		document.getElementById("resultTable").deleteRow(1);
	}

	var parameter = "searchText=" + document.getElementById("searchText").value;
	const url = "http://localhost:8787/AddressBook/contacts?" + parameter;
	var req = new XMLHttpRequest();
	req.open('GET', url, false);
	req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	req.send();
	var data = JSON.parse(req.responseText);
	$("#hits").removeClass("hidden");
	document.getElementById("noOfHits").innerText = data.length + " Hits";
	for (var i = 0; i < data.length; i++) {
		tr = $('<tr id=tr-' + i + '/>');
		if (data[i]['contact'].middleName !== undefined) {
			tr.append("<td>" + data[i]['contact'].firstName + " " + data[i]['contact'].middleName + " " + data[i]['contact'].lastName + "</td>");
		} else {
			tr.append("<td>" + data[i]['contact'].firstName + " " + data[i]['contact'].lastName + "</td>");
		}
		tr.append("<td><button id='edit-" + i + "' class='editClass'>Edit</button></td>");
		tr.append("<td><button id='delete-" + i + "' class='deleteClass'>Delete</button></td>");
		$('table').append(tr);
	}
	$(".editClass").click(
			function() {
				var chosenId = this.id.split("-")[1];
				$("#myEditModal").modal('show');
				document.getElementById("editForm").reset();
				if (data[chosenId]['contact'].firstName !== "null") {
					$('input[name="firstName"]').val(data[chosenId]['contact'].firstName);
				}
				if (data[chosenId]['contact'].middleName !== "null") {
					$('input[name="middleName"]').val(data[chosenId]['contact'].middleName);
				}
				if (data[chosenId]['contact'].lastName !== "null") {
					$('input[name="lastName"]').val(data[chosenId]['contact'].lastName);
				}
				var addresses = data[chosenId]['addressList'];
				for (var i = 0; i < addresses.length; i++) {
					if (addresses[i].addressType.toLowerCase() === "home") {
						if (addresses[i].address !== "null") {
							$('input[name="addressHome"]').val(addresses[i].address);
						}
						if (addresses[i].city !== "null") {
							$('input[name="cityHome"]').val(addresses[i].city);
						}
						if (addresses[i].state !== "null") {
							$('input[name="stateHome"]').val(addresses[i].state);
						}
						if (addresses[i].zip !== "null") {
							$('input[name="zipHome"]').val(addresses[i].zip);
						}
					} else if (addresses[i].addressType.toLowerCase() === "work") {
						if (addresses[i].address !== "null") {
							$('input[name="addressWork"]').val(addresses[i].address);
						}
						if (addresses[i].city !== "null") {
							$('input[name="cityWork"]').val(addresses[i].city);
						}
						if (addresses[i].state !== "null") {
							$('input[name="stateWork"]').val(addresses[i].state);
						}
						if (addresses[i].zip !== "null") {
							$('input[name="zipWork"]').val(addresses[i].zip);
						}
					} else if (addresses[i].addressType.toLowerCase() === "other") {
						if (addresses[i].address !== "null") {
							$('input[name="addressOther"]').val(addresses[i].address);
						}
						if (addresses[i].city !== "null") {
							$('input[name="cityOther"]').val(addresses[i].city);
						}
						if (addresses[i].state !== "null") {
							$('input[name="stateOther"]').val(addresses[i].state);
						}
						if (addresses[i].zip !== "null") {
							$('input[name="zipOther"]').val(addresses[i].zip);
						}
					}
				}
				var phones = data[chosenId]['phoneList'];
				for (var i = 0; i < phones.length; i++) {
					if (phones[i].phoneType.toLowerCase() === "home") {
						$('input[name="areaCodeHome"]').val(phones[i].areaCode);
						if (phones[i].number !== "null") {
							$('input[name="numberHome"]').val(phones[i].number);
						}
					} else if (phones[i].phoneType.toLowerCase() === "cell") {
						$('input[name="areaCodeCell"]').val(phones[i].areaCode);
						if (phones[i].number !== "null") {
							$('input[name="numberCell"]').val(phones[i].number);
						}
					} else if (phones[i].phoneType.toLowerCase() === "work") {
						$('input[name="areaCodeWork"]').val(phones[i].areaCode);
						if (phones[i].number !== "null") {
							$('input[name="numberWork"]').val(phones[i].number);
						}
					} else if (phones[i].phoneType.toLowerCase() === "other") {
						$('input[name="areaCodeOther"]').val(phones[i].areaCode);
						if (phones[i].number !== "null") {
							$('input[name="numberOther"]').val(phones[i].number);
						}
					}
				}
				var dates = data[chosenId]['dateList'];
				for (var i = 0; i < dates.length; i++) {
					if (dates[i].dateType.toLowerCase() === "birthday") {
						if (dates[i].date !== null) {
							var date = new Date(dates[i].date).toISOString().substring(0, 10);
							$('input[name="dateBirthday"]').val(date);
						}
					} else if (dates[i].dateType.toLowerCase() === "anniversary") {
						if (dates[i].date !== null) {
							var date = new Date(dates[i].date).toISOString().substring(0, 10);
							$('input[name="dateAnniversary"]').val(date);
						}
					} else if (dates[i].dateType.toLowerCase() === "other") {
						if (dates[i].date !== null) {
							var date = new Date(dates[i].date).toISOString().substring(0, 10);
							$('input[name="dateOther"]').val(date);
						}
					}
				}
				// $("#date").datepicker(date);
				$("#firstName").change(function() {
					data[chosenId]['contact'].firstName = $('input[name="firstName"]').val();
				});
				$("#middleName").change(function() {
					data[chosenId]['contact'].middleName = $('input[name="middleName"]').val();
				});
				$("#lastName").change(function() {
					data[chosenId]['contact'].lastName = $('input[name="lastName"]').val();
				});
				$("#addressHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "home") {
							data[chosenId]['addressList'][i].address = $('input[name="addressHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Home";
						data[chosenId]['addressList'][index].address = $('input[name="addressHome"]').val();
					}
				});
				$("#addressWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "work") {
							data[chosenId]['addressList'][i].address = $('input[name="addressWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Work";
						data[chosenId]['addressList'][index].address = $('input[name="addressWork"]').val();
					}
				});
				$("#addressOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "other") {
							data[chosenId]['addressList'][i].address = $('input[name="addressOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Other";
						data[chosenId]['addressList'][index].address = $('input[name="addressOther"]').val();
					}
				});
				$("#cityHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "home") {
							data[chosenId]['addressList'][i].city = $('input[name="cityHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Home";
						data[chosenId]['addressList'][index].city = $('input[name="cityHome"]').val();
					}
				});
				$("#cityWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "work") {
							data[chosenId]['addressList'][i].city = $('input[name="cityWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Work";
						data[chosenId]['addressList'][index].city = $('input[name="cityWork"]').val();
					}
				});
				$("#cityOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "other") {
							data[chosenId]['addressList'][i].city = $('input[name="cityOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Other";
						data[chosenId]['addressList'][index].city = $('input[name="cityOther"]').val();
					}
				});
				$("#stateHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "home") {
							data[chosenId]['addressList'][i].state = $('input[name="stateHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Home";
						data[chosenId]['addressList'][index].state = $('input[name="stateHome"]').val();
					}
				});
				$("#stateWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "work") {
							data[chosenId]['addressList'][i].state = $('input[name="stateWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Work";
						data[chosenId]['addressList'][index].state = $('input[name="stateWork"]').val();
					}
				});
				$("#stateOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "other") {
							data[chosenId]['addressList'][i].state = $('input[name="stateOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Other";
						data[chosenId]['addressList'][index].state = $('input[name="stateOther"]').val();
					}
				});
				$("#zipHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "home") {
							data[chosenId]['addressList'][i].zip = $('input[name="zipHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Home";
						data[chosenId]['addressList'][index].zip = $('input[name="zipHome"]').val();
					}
				});
				$("#zipWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "work") {
							data[chosenId]['addressList'][i].zip = $('input[name="zipWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Work";
						data[chosenId]['addressList'][index].zip = $('input[name="zipWork"]').val();
					}
				});
				$("#zipOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['addressList'].length; i++) {
						if (data[chosenId]['addressList'][i].addressType.toLowerCase() === "other") {
							data[chosenId]['addressList'][i].zip = $('input[name="zipOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['addressList'].length;
						data[chosenId]['addressList'][index] = {};
						data[chosenId]['addressList'][index].addressType = "Other";
						data[chosenId]['addressList'][index].zip = $('input[name="zipOther"]').val();
					}
				});
				$("#numberHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "home") {
							data[chosenId]['phoneList'][i].number = $('input[name="numberHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Home";
						data[chosenId]['phoneList'][index].number = $('input[name="numberHome"]').val();
					}
				});
				$("#numberCell").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "cell") {
							data[chosenId]['phoneList'][i].number = $('input[name="numberCell"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Cell";
						data[chosenId]['phoneList'][index].number = $('input[name="numberCell"]').val();
					}
				});
				$("#numberWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "work") {
							data[chosenId]['phoneList'][i].number = $('input[name="numberWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Work";
						data[chosenId]['phoneList'][index].number = $('input[name="numberWork"]').val();
					}
				});
				$("#numberOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "other") {
							data[chosenId]['phoneList'][i].number = $('input[name="numberOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Other";
						data[chosenId]['phoneList'][index].number = $('input[name="numberOther"]').val();
					}
				});
				$("#areaCodeHome").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "home") {
							data[chosenId]['phoneList'][i].areaCode = $('input[name="areaCodeHome"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Home";
						data[chosenId]['phoneList'][index].areaCode = $('input[name="areaCodeHome"]').val();
					}
				});
				$("#areaCodeCell").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "cell") {
							data[chosenId]['phoneList'][i].areaCode = $('input[name="areaCodeCell"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Cell";
						data[chosenId]['phoneList'][index].areaCode = $('input[name="areaCodeCell"]').val();
					}
				});
				$("#areaCodeWork").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "work") {
							data[chosenId]['phoneList'][i].areaCode = $('input[name="areaCodeWork"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Work";
						data[chosenId]['phoneList'][index].areaCode = $('input[name="areaCodeWork"]').val();
					}
				});
				$("#areaCodeOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['phoneList'].length; i++) {
						if (data[chosenId]['phoneList'][i].phoneType.toLowerCase() === "other") {
							data[chosenId]['phoneList'][i].areaCode = $('input[name="areaCodeOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['phoneList'].length;
						data[chosenId]['phoneList'][index] = {};
						data[chosenId]['phoneList'][index].phoneType = "Other";
						data[chosenId]['phoneList'][index].areaCode = $('input[name="areaCodeOther"]').val();
					}
				});
				$("#dateBirthday").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['dateList'].length; i++) {
						if (data[chosenId]['dateList'][i].dateType.toLowerCase() === "birthday") {
							data[chosenId]['dateList'][i].date = $('input[name="dateBirthday"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['dateList'].length;
						data[chosenId]['dateList'][index] = {};
						data[chosenId]['dateList'][index].dateType = "Birthday";
						data[chosenId]['dateList'][index].date = $('input[name="dateBirthday"]').val();
					}
				});
				$("#dateAnniversary").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['dateList'].length; i++) {
						if (data[chosenId]['dateList'][i].dateType.toLowerCase() === "anniversary") {
							data[chosenId]['dateList'][i].date = $('input[name="dateAnniversary"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['dateList'].length;
						data[chosenId]['dateList'][index] = {};
						data[chosenId]['dateList'][index].dateType = "Anniversary";
						data[chosenId]['dateList'][index].date = $('input[name="dateAnniversary"]').val();
					}
				});
				$("#dateOther").change(function() {
					var entryModified = false;
					for (var i = 0; i < data[chosenId]['dateList'].length; i++) {
						if (data[chosenId]['dateList'][i].dateType.toLowerCase() === "other") {
							data[chosenId]['dateList'][i].date = $('input[name="dateOther"]').val();
							entryModified = true;
						}
					}
					if (!entryModified) {
						var index = data[chosenId]['dateList'].length;
						data[chosenId]['dateList'][index] = {};
						data[chosenId]['dateList'][index].dateType = "Other";
						data[chosenId]['dateList'][index].date = $('input[name="dateOther"]').val();
					}
				});
				var modified = false;
				$("#modifyContact").unbind("click").click(
						function() {
							if (!modified) {
								var postUrl = "http://localhost:8787/AddressBook/contacts?reqFor=edit&contact="
										+ JSON.stringify(data[chosenId]['contact']) + "&addressList=" + JSON.stringify(data[chosenId]['addressList'])
										+ "&phoneList=" + JSON.stringify(data[chosenId]['phoneList']) + "&dateList="
										+ JSON.stringify(data[chosenId]['dateList']);
								postUrl = encodeURI(postUrl);
								var postReq = new XMLHttpRequest();
								postReq.open('POST', postUrl, false);
								postReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
								postReq.send();
								modified = true;
								$("#myEditModal").modal('hide');
								setTimeout(function() {
									modified = false;
								}, 900);
							}
						});
			});
	$(".deleteClass").click(function() {
		var chosenId = this.id.split("-")[1];
		alert("Deleting " + data[chosenId]['contact'].firstName + " " + data[chosenId]['contact'].lastName + " from the database.");
		var deleteUrl = "http://localhost:8787/AddressBook/contacts?reqFor=delete&contactId=" + data[chosenId]['contact'].contactId;
		var deleteReq = new XMLHttpRequest();
		deleteReq.open('POST', deleteUrl, false);
		deleteReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		deleteReq.send();
	});
}
// $("#addContact").click(

