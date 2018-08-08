function removeOutcome(outcomeId) {
	if (confirm(LABELS.REMOVE_OUTCOME_CONFIRM_LABEL)) {
		document.location.href = "outcome.do?method=outcomeRemove&outcomeId=" + outcomeId 
								 + '&organisationID=' + organisationId;
	}
}

function openEditOutcomeDialog(outcomeId) {
	window.parent.showDialog("dialogOutcomeEdit", {
		'height': Math.max(380, Math.min(700, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'title' : outcomeId ? LABELS.EDIT_OUTCOME_TITLE : LABELS.ADD_OUTCOME_TITLE,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'outcome.do?method=outcomeEdit&'
						+ (outcomeId ? "outcomeId=" + outcomeId : "organisationID=" + organisationId));
		}
	});
}