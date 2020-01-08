$(document).ready(function(){
	$('#importInput').change(function(){
		if (this.files.length == 1) {
			$('#importForm').submit();
		} 
	});
});

function removeOutcome(outcomeId) {
	if (confirm(LABELS.REMOVE_OUTCOME_CONFIRM_LABEL)) {
		var f = document.getElementById(outcomeId);
		f.submit();
	}
}

function removeScale(scaleId) {
	if (confirm(LABELS.REMOVE_SCALE_CONFIRM_LABEL)) {
		document.location.href = 'scaleRemove.do?organisationID=' + organisationId + '&scaleId=' + scaleId;
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
						+ 'outcome/outcomeEdit.do?organisationID=' + organisationId
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
						+ 'outcome/scaleEdit.do?organisationID=' + organisationId
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
						+ 'outcome/scaleManage.do?organisationID='
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

function exportOutcome(isScaleExport){
	var exportButton = $('#exportButton').button('loading'),
		token = new Date().getTime(),
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
			    //unBlock export button
				window.clearInterval(fileDownloadCheckTimer);
				$.cookie('fileDownloadToken', null); //clears this cookie value
				exportButton.button('reset');
			}
		}, 1000);
	document.location.href = LAMS_URL + 'outcome/' + (isScaleExport ? 'scaleExport' : 'outcomeExport') + '.do?downloadTokenValue=' + token;
	return false;
}
