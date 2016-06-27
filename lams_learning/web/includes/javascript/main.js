// ----- CONTROL FRAME & WINDOW MANIPULATION -----

function restartLesson(){
	if (confirm(LABELS.CONFIRM_RESTART)) {
		$('#contentFrame').attr('src',APP_URL + 'learner.do?method=restartLesson&lessonID=' + lessonId);
	}
}

function viewNotebookEntries(){
	openPopUp(APP_URL + "notebook.do?method=viewAll&lessonID=" + lessonId,
			"Notebook",
			570,796,
			"no");
}

function closeWindow() {
	// refresh the parent window
	if (parentURL != "") {
		if (window.parent.opener == null
				|| '${param.noopener}' == 'true'
				|| parentURL.indexOf('noopener=true') >= 0) {
			window.location.href = parentURL;
		} else {
			window.parent.opener.location.href = parentURL;
		}
		return true;
	}

	top.window.close();
}

// open/close Notebook or Support Activity frames
function toggleBarPart(name) {
	var part = $('#' + name + 'Part');
	var togglerCell = $('#' + name + 'TogglerCell');
	if (part.is(':visible')) {
		part.hide();
		togglerCell.html('▲');
	} else {
		part.show();
		togglerCell.html('▼');
	}
	resizeElements();
}

function validateNotebookForm(fields) {
	var formFilled = false;
	$.each(fields,function(index, field){
		// if title and text are blank, do not submit
		if ((field.name == 'title' || field.name == 'entry') 
				&& field.value) {
			formFilled = true;
			// stop iterating
			return false;
		}
	});
	return formFilled;
}