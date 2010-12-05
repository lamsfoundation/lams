   var callAttemptedID = 0; //ID of the most recent call (form submit)
   var activityCallRetrievedID = 0; //ID of the activity call that is processed now
   var sequenceDetailsCallRetrievedID = 0; //ID of the sequence details call that is processed now
   var sequenceDetailsValid = true; //Are the submitted sequence details valid?
   var activitiesValid = 0; //Number of valid activities that responded
   var activitiesResponded = 0; //Number of activities that responded
   var actionAfterCompleted; //What action should be called if all activities were saved successfully
   var startPreviewUrl; //Url to start preview
   var learningDesignId; //ID of the design to open
   
   var ACTION_SAVE_AS_SEQUENCE = 0; //After successful submit save learning design in user's personal folder
   var ACTION_PREVIEW = 1; //After successful submit start preview
   var ACTION_OPEN_AUTHOR = 2; //After successful submit open full authoring
   var ACTION_EXPORT = 3; //After successful submit export the learning design 
   var END_HEAD_REGEX_PATTERN = new RegExp('</head','i'); //Regex to find "head" element in a html pages
   var FILE_ELEMENT_NAME = "file[";
       
   // Submit all activities
   function submitAll(action,additionalParameter){
   
    actionAfterCompleted=action;
    if (actionAfterCompleted==ACTION_PREVIEW){
    	startPreviewUrl=additionalParameter;
    }
    else if (actionAfterCompleted==ACTION_SAVE_AS_SEQUENCE || actionAfterCompleted==ACTION_OPEN_AUTHOR || actionAfterCompleted==ACTION_EXPORT){
  	    learningDesignId=additionalParameter;
    }
    callAttemptedID++;
    
    //Clear any current messages
    $('#pedagogicalPlannerErrorArea').hide();
    $('#pedagogicalPlannerErrorArea').html("");
    $('#pedagogicalPlannerInfoArea').hide();
    $('#pedagogicalPlannerBusy').show();
    
    
    $('#callAttemptedID').val(callAttemptedID);
 	var param = $("#sequenceDetailsForm").serialize();
 	
 	//Since sequence title is not an activity, it has to be submitted different way
 	$.ajax({
 		url: saveDetailsUrl,
 		cache: false,
 		data: param,
 		success: onSequenceDetailsResponse
 	});
 
 	for (var activityIndex = 1;activityIndex<=activityCount;activityIndex++){
     //we calculate delay before the form should be submitted
     var effectiveDelay = sendInPortions ? Math.floor((activityIndex - 1) / activitiesPerPortion) * submitDelay : 0;
     //each tool will implement an interface that will provide a simplified authoring page with form named "pedagogicalPlannerForm"
     $('#activity'+activityIndex).contents().find('#callID').val(callAttemptedID);
     $('#activity'+activityIndex).contents().find('#activityOrderNumber').val(activityIndex);
     setTimeout("submitActivityForm("+activityIndex+");",effectiveDelay);
    }
   }
   
   //Submit single activity
   function submitActivityForm(activityIndex){
    var activity = document.getElementById('activity'+activityIndex);
   	var form = $(activity).contents().find('#pedagogicalPlannerForm');
   	if (form.length > 0){
   		//check if activity has a special function that needs to be run before submit
   		if (activity.contentWindow.prepareFormData){
	  	 	activity.contentWindow.prepareFormData();
	  	 }
	  	//prepareFormData above is inside the tool and the one below is here, triggered by jQuery
   		form.ajaxSubmit({
   			beforeSubmit: prepareFormData,
   			success: onActivityResponse,
   			dataType: "html"
   		});
   	}
   }
   
   function prepareFormData(formData, jqForm, options){
  	for (elementIndex = 0;elementIndex<formData.length;elementIndex++){
  		var elementName = formData[elementIndex].name;
  		if (elementName.indexOf(FILE_ELEMENT_NAME)==0 && formData[elementIndex].value==""){
  			var openBracketIndex = elementName.indexOf("[")+1;
  			formData[elementIndex].name="fileDummy["+elementName.substr(openBracketIndex,elementName.length-openBracketIndex-1)+"]";
  		}
  	}
   }
   
   //Called when server responds after a submit
   function onActivityResponse(responseText, statusText){
  	 var activityIndex = $(responseText).find('#activityOrderNumber').val();
 	 var callID = $(responseText).find('#callID').val();
 	 var valid =  $(responseText).find('#valid').val();
 	 
	 //Check if we are processing the current call 
  	 if (callID>activityCallRetrievedID){
  	 	//clear old data
  		activityCallRetrievedID=callID;
  		validForms = 0;
  		activitiesResponded = 0;
  		activitiesValid=0;
  	 }
  	 activitiesResponded++;
  	 if (valid=="true"){
  		activitiesValid++;
    }
   	
   	//Check if it's the last activity
 	checkSubmitOperationsCompleted();
 	var activity = document.getElementById('activity'+activityIndex);
 	if (activity!=null){
	  	 //there is a bug in FF that strips head and body tags from inserted text; that's why it has to be done this way
	  	 var headResponse = responseText.substring(responseText.search(/<head/i),responseText.search(END_HEAD_REGEX_PATTERN ));
	  	 if (headResponse.length>0){
	  	 	$('#activity'+activityIndex).contents().find('head').html();
	  	 }
	  	 activity.contentWindow.document.body.innerHTML=responseText.substring(responseText.search(/<body/i));
	  	 //An activity form may have a function that should be called after form is received; its name must be "fillForm()"
	  	 if (activity.contentWindow.fillForm){
	  	 	activity.contentWindow.fillForm();
	  	 }
	  	 
	  	 // reeavaluate script initializing the CKEditor instance
	  	 if (activity.contentWindow.initializeCKEditor){
		  	activity.contentWindow.initializeCKEditor();
		 }
  	 }
   }
   
   //Called when sequence title was saved
   function onSequenceDetailsResponse(responseText){
    var responseParts = responseText.split("&");
    sequenceDetailsCallRetrievedID = responseParts[1];
    sequenceDetailsValid = responseParts[0]=="OK";
    if (!sequenceDetailsValid){
    	$('#pedagogicalPlannerErrorArea').html($('#pedagogicalPlannerErrorArea').html()+responseParts[0]+"<br />");
    }
 	 checkSubmitOperationsCompleted();
   }
   
   //Check if all forms have responded; if yes, proceed with selected actions
   function checkSubmitOperationsCompleted(){
   if (activitiesResponded==activitySupportingPlannerCount 
  	  && activityCallRetrievedID==callAttemptedID
  	  && sequenceDetailsCallRetrievedID==callAttemptedID){
  	$('#pedagogicalPlannerBusy').hide();
  	if (sequenceDetailsValid &&  activitiesValid==activitiesResponded){
  	   	$('#pedagogicalPlannerInfoArea').show();
  	   	
	   	if (actionAfterCompleted==ACTION_SAVE_AS_SEQUENCE){
	   	 	$.ajax({
	   	 		url: saveDetailsUrl,
	   	 		cache: false,
	   	 		data: "method=copyLearningDesign&ldId="+learningDesignId
	   	 	});
	   	 	
	   	 	//offers to close PedPlanner and open modified sequence
	   	 	if (requestSrc != "") $("#saveSequenceDialog").dialog('open');
  	   	}  	  
	   	else if (actionAfterCompleted==ACTION_PREVIEW){
  	   		startPreview(startPreviewUrl);
  	   	}
  	   	else if (actionAfterCompleted==ACTION_OPEN_AUTHOR){
  	   		 window.resizeTo(authoring_width,authoring_height);
  	   		 $.ajax({
	   	 		url: saveDetailsUrl,
	   	 		cache: false,
	   	 		data: "method=copyLearningDesign&ldId="+learningDesignId
	   	 	});
  	   		 
  	   		var openAuthorURL = "home.do?method=author&learningDesignID=" + learningDesignId;
  	   		if (requestSrc != "") openAuthorURL += "&requestSrc=" + requestSrc;
  	   		if (notifyCloseURL != "") {
  	   			//to prevent losing of query parameters change '&' into '%26'
  	   			notifyCloseURL = notifyCloseURL.replace (/&/g, '%26');
  	   			openAuthorURL += "&notifyCloseURL=" + notifyCloseURL;
  	   		}
  	   		document.location.href = openAuthorURL;
  	   	}
  	   	else if (actionAfterCompleted==ACTION_EXPORT){
  	   		 document.getElementById("downloadFileDummyIframe").src="pedagogicalPlanner.do?method=exportTemplate&ldId="+learningDesignId;
  	   	}
  	}
  	else {
  		if (activitiesValid!=activitiesResponded){
  			$('#pedagogicalPlannerErrorArea').html($('#pedagogicalPlannerErrorArea').html()+errorPlannerNotSaved +"<br />");
  		}
  		$('#pedagogicalPlannerErrorArea').show();
  	}
   }
  }
  
  function startPreview(url){
  	window.open(url,'Preview','width=800,height=600,scrollbars=yes,resizable=yes');
  }
  
  function closePlanner(text){
	 if (text==null || confirm(text)){
		 
		// refresh the parent window
		if (notifyCloseURL != "") {
			window.parent.opener.location.href = notifyCloseURL;
		}

	 	window.close();
  	 }
  }
  
  function leavePageAfterConfirm(text,url){
	 	if (text==null || confirm(text)){
  			document.location.href=url;
  		}
  	 }
  
  function onRemoveFileCheckboxChange(){
  	 document.getElementById("fileInputArea").style.display = document.getElementById("removeFile").checked ? "none" : "block";	
  }
  
  function onNodeTypeChange(){
  	 document.getElementById("fileArea").style.display = document.getElementById("hasSubnodesType").checked ? "none" : "block";
  }
  
  function filterNodes(url,doFilter){
  	if (doFilter){
  		url += "&filterText="+document.getElementById("filterText").value;
  	}
  	document.location.href=url;
  }
  
  function filterNodesOnEnter(url){
	  if (window.event && window.event.keyCode == 13){
	  	filterNodes(url,true);
	  }
  }
  
  function collapseActivity(id, action){
	  $('.collapsible'+id).hide();
	  $('#activity'+id).hide('slow', function () {
		  $('#activity'+action+'Span'+id).show('slow');
	  });
  }
  
  function expandActivity(id, action){
	  $('#activity'+action+'Span'+id).hide('slow', function () {
		  $('#activity'+id).show('slow', function (){
			  $('.collapsible'+id).show();
		  });
	  });
  }
  
  function openActivityAuthor(id, url, title) {
	  collapseActivity(id, 'Edit');
	  
	  var wd = window.open(url,title,'resizable,width=930,height=700,scrollbars');
	  var watchClose = setInterval(function() {
		        if (wd.closed) {
		            clearTimeout(watchClose);
		            $('#activity'+id)[0].contentWindow.location.reload(true);
		            expandActivity(id, 'Edit');
		        }
		}, 500);
	  
	  if (window.focus) {
		  wd.window.focus();
	  }
  }  