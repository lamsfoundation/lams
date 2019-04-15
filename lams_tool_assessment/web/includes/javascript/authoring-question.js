//in order to use this js file, define const VALIDATION_ERROR_LABEL and VALIDATION_ERRORS_LABEL

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
});

// post-submit callback 
function afterRatingSubmit(responseText, statusText)  { 
	self.parent.refreshThickbox()
	self.parent.tb_remove();
}

//form validation handler. It's called when the form contains an error.
function formInvalidHandler(form, validator) {
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
        	showSettingsTab &= key == "defaultGrade" || key == "penaltyFactor";
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