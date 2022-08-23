// NOTE. In order for this JS file to work properly, please, define the following 4 constants in jsp: 
// QUESTION_TYPE, ADD_OPTION_URL, CONFIRM_DELETE_ANSWER_LABEL, SLIDER_NONE_LABEL
$(document).ready(function(){
	initializeAnswers();
});

function removeOption(idx){
	var	deletionConfirmed = confirm(CONFIRM_DELETE_ANSWER_LABEL);
	if (deletionConfirmed) {
		$("#option-table-" + idx).remove();
		checkQuestionNewVersion();
	}
}
	
function addOption(){
	//store old InstanceIds before doing Ajax call. We need to keep record of old ones to prevent reinitializing new CKEditor two times.
	var oldOptionIds = new Array();
		for (var instanceId in CKEDITOR.instances){
		oldOptionIds[instanceId] = instanceId;
	}

	var contentFolderID= $("#contentFolderID").val();
	prepareOptionEditorsForAjaxSubmit();
	var optionList = $("#optionForm").serialize(true);
	$.ajaxSetup({ cache: true });
	$("#optionArea").load(
		ADD_OPTION_URL,
		{
			contentFolderID: contentFolderID,
			questionType: QUESTION_TYPE,
			optionList: optionList 
		}, 
		function() {
			initializeAnswers();
			checkQuestionNewVersion();
		}
	);
}
	
//in order to be able to use option's value, copy it from CKEditor to textarea
function prepareOptionEditorsForAjaxSubmit(){
	if ((QUESTION_TYPE == 1) || (QUESTION_TYPE == 7) || (QUESTION_TYPE == 8)) {
		$("textarea[name^=optionName], textarea[name^=optionFeedback]").each(function()  {
			prepareOptionEditorForAjaxSubmit(this);
		});
			
	} else if (QUESTION_TYPE == 2) {
		$("[name^=matchingPair]").each(function() {
			prepareOptionEditorForAjaxSubmit(this);
		});
			
	} else if ((QUESTION_TYPE == 3) || (QUESTION_TYPE == 4)) {
		$("[name^=optionFeedback]").each(function() {
			prepareOptionEditorForAjaxSubmit(this);
		});
	}
}
function prepareOptionEditorForAjaxSubmit(ckeditor){
	var ckeditorData = CKEDITOR.instances[ckeditor.name].getData();
	//skip out empty values
	ckeditor.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
}

function initializeAnswers() {

	//check if jquery-ui.slider.js library is loaded
	if (typeof(jQuery.ui.slider) != 'undefined'){
		//init grading slider
	    $(".slider").slider({
	        animate: true,
	        min: -1,
	        max: 1,
	        step: 0.05,
	        create: function(event, ui){
	        	//get initial value from the responsible form input
	           	var initialValue = $(this).next("input").val();
	           	$(this).slider('value', initialValue);
	        }
	    }).on('slide',function(event,ui){
	        //ui is not available at the initial call 
	        var newValueInt = ui ? eval(ui.value) : eval($(this).slider('value'));
		        
	        //prepare string value
	        var newValue;
	        switch (newValueInt) {
			  case 0:
				newValue = " " + SLIDER_NONE_LABEL + " ";
			    break;
			  case 1:
			  case -1:
				newValue = parseInt(newValueInt*100) + " %";
				break;
			  default:
				newValue = " " + parseInt(newValueInt*100) + " % ";
			}
		        
			//update slider's label
	       	$('span', $(this)).html('<label><span class="fa fa-angle-left"></span><span>'+newValue+'</span><span class="fa fa-angle-right"></span></label>');
	
	       	//update input with the new value, it it's not initial call
	       	if (ui) {
	        	$(this).next("input").val(ui.value);
	       	}
	       	
	       	var optionDisplayOrderSpan = $(this).closest('.single-option-table').find('.optionDisplayOrderSpan');
	       	if (newValueInt > 0) {
	       		optionDisplayOrderSpan.addClass('correctOption').css('filter', 'brightness(' + (2 - newValueInt)  + ')');
	       	} else {
	       		optionDisplayOrderSpan.removeClass('correctOption').css('filter', 'brightness(1)');
	       	}

			checkQuestionNewVersion();
	    });
	    //update slider's label with the initial value
	    $('.slider').trigger('slide');
	}
    
    //init options sorting feature
	if ($('#option-table.sortable-on').length) {
	    new Sortable($('#option-table.sortable-on')[0], {
		    animation: 150,
		    ghostClass: 'sortable-placeholder',
		    direction: 'vertical',
			onStart: function (evt) {
				//stop answers' hover effect, once element dragging started
				//$("#option-table").removeClass("hover-active");
			},
			onEnd: function (evt) {
				//activate answers' hover effect, once element dragging ended
				//$("#option-table").delay(50).queue(function(next){
				//    $(this).addClass("hover-active");
				//    next();
				//});
			},
			store: {
				set: function (sortable) {
					//update all displayOrders in order to later save it as options' order
					var order = sortable.toArray();
					for (var i = 0; i < order.length; i++) {
					    var optionIndex = order[i];
					    $('input[name="optionDisplayOrder' + optionIndex + '"]').val(i+1);
					    $('span#optionDisplayOrderSpan' + optionIndex).text(alphabet[i]);
					}
					checkQuestionNewVersion();
				}
			}
		});
	}

}
