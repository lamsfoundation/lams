//in order to use this js file, define const VALIDATION_ERROR_LABEL and VALIDATION_ERRORS_LABEL

// skip initial check of new version
let newQuestionVersionCheckTime = new Date().getTime() + 1000,
	// do not check for new version more often than every 2 seconds
	newQuestionVersionCheckThrottle = 2000;


$(document).ready(function(){
	$("#question-settings-link").on('click', function() {
		$('.question-tab:visible').fadeToggle("fast", function() {
			$( ".settings-tab" ).show();
		});
		$('.settings-tab:visible').fadeToggle("fast", function() {
			$( ".question-tab" ).show();
		});

		//toggle Settings button class
		$(this).toggleClass("btn-default btn-primary");
	});

	// trigger is-new-question-version check when changing certain data in a question
	$('#assessmentQuestionForm').on('input', 'input, select, textarea', function(){
		checkQuestionNewVersion(false);
	}).on('change', function(){
		checkQuestionNewVersion(true);
	});
	$('body').on('input paste', '[contenteditable]', function(){
		checkQuestionNewVersion(false);
	});

	$('#collection-uid-select').change(function(){
		let collectionSelect = $(this),
			newValue = collectionSelect.val(),
			previouslySelectedOption = $('option[selected]', collectionSelect);
		if (newValue == -1) {
			// create a new collection on the fly
			let newCollectionName = prompt(ADD_COLLECTION_LABEL),
				newCollectionUid = -1;
			if (newCollectionName) {
				newCollectionName = newCollectionName.trim();
				let data = {
					'name' : newCollectionName
				};
				data[csrfTokenName] = csrfTokenValue;

				$.ajax({
					'url' : LAMS_URL + 'qb/collection/addCollection.do',
					'async' : false,
					'type' : 'post',
					'dataType' : 'text',
					'data' : data,
					success : function (response){
						if (!isNaN(response)) {
							newCollectionUid = +response;
						}
					},
					error : function (xhr) {
						alert(xhr.responseText);
					}
				});
			}

			if (newCollectionUid == -1 || newCollectionUid == 0) {
				// revert to previous selection
				previouslySelectedOption.prop('selected', true);
			} else {
				$('<option>').attr('value', newCollectionUid).attr('selected', 'selected')
					.prop('selected', true).text(newCollectionName).insertBefore($('option[value="-1"]', collectionSelect));
			}
		} else {
			previouslySelectedOption.removeAttr('selected');
			$('option[value="' + newValue + '"]', collectionSelect).attr('selected', 'selected');
		}
	});
});

// submits whole question form in order to check if it changed enough to produce a new question version
function checkQuestionNewVersion(quick){
	if (isNewQuestion) {
		return;
	}

	let currentTime = new Date().getTime();
	// skip initial check of new version
	if (currentTime < newQuestionVersionCheckTime || (!quick && currentTime - newQuestionVersionCheckTime < newQuestionVersionCheckThrottle)) {
		return;
	}
	newQuestionVersionCheckTime = currentTime;

	let form = $('#assessmentQuestionForm'),
		validator = form.data('validator');
	if (!validator) {
		return;
	}
	form.attr('action', CHECK_QUESTION_NEW_VERSION_URL);
	validator.cancelSubmit = true;
	form.submit();
}

function isVersionCheck() {
	return $('#assessmentQuestionForm').attr('action') == CHECK_QUESTION_NEW_VERSION_URL;
}

// post-submit callback 
function afterRatingSubmit(responseText, statusText)  {
	self.parent.refreshThickbox()
	self.parent.tb_remove();
}

function afterVersionCheck(responseText, statusText, c, d){
	$('#assessmentQuestionForm').attr('action', SAVE_QUESTION_URL).data('validator').cancelSubmit = false;
	// the controller produces true/false and is interpreted as JSON
	let newVersion = responseText;
	$('#saveButton').toggle(!newVersion);
	$('#saveAsButton').show().toggleClass('btn-primary', newVersion).toggleClass('btn-default', !newVersion);
}

//form validation handler. It's called when the form contains an error.
function formValidationInvalidHandler(form, validator) {
	var errors = validator.numberOfInvalids();
	if (errors) {
		var message = errors == 1
			? VALIDATION_ERROR_LABEL
			: VALIDATION_ERRORS_LABEL.replace("{errors_counter}", errors);

		$("div.error span").html(message);
		$("div.error").show();

		//show/hide settings tab, if it contains an error
		var showSettingsTab = true;
		$.each(validator.errorMap, function(key, value) {
			showSettingsTab &= key == "maxMark" || key == "penaltyFactor";
		});
		if (showSettingsTab) {
			$("#question-settings-link.btn-default").trigger( "click" );
		} else {
			$("#question-settings-link.btn-primary").trigger( "click" );
		}

	} else {
		$("div.error").hide();
	}
}
function formValidationErrorPlacement( error, element ) {
	// Add the `help-block` class to the error element
	error.addClass( "help-block" );

	// Add `has-feedback` class to the parent div.form-group in order to add icons to inputs
	element.parent().addClass( "has-feedback" );

	if ( element.prop( "type" ) === "checkbox" ) {
		error.insertAfter( element.parent( "label" ) );
	} else {
		error.insertAfter( element );
	}

	// Add the span element, if doesn't exists, and apply the icon classes to it.
	if ( !element.next( "span" )[ 0 ] ) {
		$( "<span class='fa fa-remove form-control-feedback'></span>" ).insertAfter( element );
	}
}

function formValidationHighlight ( element, errorClass, validClass ) {
	$( element ).parent().addClass( "has-error" ).removeClass( "has-success" );
	$( element ).next( "span" ).addClass( "fa-remove" ).removeClass( "fa-check" );
}
function formValidationUnhighlight ( element, errorClass, validClass ) {
	$( element ).parent().addClass( "has-success" ).removeClass( "has-error" );
	$( element ).not(".fake-validation-input").next( "span" ).addClass( "fa-check" ).removeClass( "fa-remove" );
}