function removeOutcome(outcomeId) {
	if (confirm(LABELS.REMOVE_OUTCOME_CONFIRM_LABEL)) {
		document.location.href = 'outcome.do?method=outcomeRemove&organisationID=' + organisationId + '&outcomeId=' + outcomeId;
	}
}

function removeScale(scaleId) {
	if (confirm(LABELS.REMOVE_SCALE_CONFIRM_LABEL)) {
		document.location.href = 'outcomeScale.do?method=scaleRemove&organisationID=' + organisationId + '&scaleId=' + scaleId;
	}
}

function openEditOutcomeDialog(outcomeId) {
	window.parent.showDialog("dialogOutcomeEdit", {
		'height': Math.max(380, Math.min(700, $(window.parent).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window.parent).width() - 60)),
		'title' : outcomeId ? LABELS.EDIT_OUTCOME_TITLE : LABELS.ADD_OUTCOME_TITLE,
		'open'  : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'outcome.do?method=outcomeEdit&organisationID=' + organisationId
						+ (outcomeId ? "&outcomeId=" + outcomeId : ""));
		}
	});
}

function openEditScaleDialog(scaleId) {
	window.parent.showDialog("dialogOutcomeScaleEdit", {
		'height': Math.max(380, Math.min(700, $(window.parent).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window.parent).width() - 60)),
		'title' : scaleId ? LABELS.EDIT_SCALE_TITLE : LABELS.ADD_SCALE_TITLE,
		'open'  : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'outcomeScale.do?method=scaleEdit&organisationID=' + organisationId
						+ (scaleId ? "&scaleId=" + scaleId : ""));
		}
	});
}

function openOutcomeScaleDialog() {
	window.parent.showDialog("dialogOutcomeScale", {
		'height': Math.max(380, Math.min(700, $(window.parent).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window.parent).width() - 60)),
		'title' : organisationId ? LABELS.OUTCOME_SCALE_COURSE_MANAGE_TITLE : LABELS.OUTCOME_SCALE_MANAGE_TITLE,
		'open'  : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'outcomeScale.do?method=scaleManage&organisationID='
						+ organisationId);
		}
	});
}

function submitOutcome(){
	CKEDITOR.instances.description.updateElement();
	document.getElementById("outcomeForm");
}   

function submitScale(){
	CKEDITOR.instances.description.updateElement();
	document.getElementById("scaleForm");
}  