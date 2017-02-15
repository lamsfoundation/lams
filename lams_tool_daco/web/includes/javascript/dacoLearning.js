	function init(){
	 doSelectTab(currentTab);
	 readHiddenFormValues();
    }
        
    function doSelectTab(tabId) {
	  selectTab(tabId);
	  currentTab = tabId;
    }
        
	function finishSession(){
		$('.btn-disable-on-submit').prop('disabled', true);
		document.location.href = finishUrl;
		return false;
	}
		
	function continueReflect(){
		$('.btn-disable-on-submit').prop('disabled', true);
		document.location.href=continueReflectUrl;
	}
	
	function saveOrUpdateRecord()	{
		$('.btn-disable-on-submit').prop('disabled', true);
		setHiddenFormValues();
		document.getElementById("recordForm").submit();
	}
	
	function setHiddenFormValues(){
		var elementCount = document.getElementById("recordForm").elements.length;
		for (var questionNumber=1;questionNumber<=elementCount;questionNumber++){
			var checkboxQuestion=document.getElementById("checkbox-"+questionNumber);
			if (checkboxQuestion!=null){
				var checkboxQuestionValue = "";
				var checkboxNumber = 1;
				var checkbox = document.getElementById("checkbox-"+questionNumber+"-"+checkboxNumber);
				while (checkbox!=null){
					if (checkbox.checked){
						checkboxQuestionValue += checkboxNumber + "&";
					}
				checkboxNumber++;
				checkbox = document.getElementById("checkbox-"+questionNumber+"-"+checkboxNumber);
				}
				checkboxQuestion.value=checkboxQuestionValue;
			}
		}
	}
	
	function readHiddenFormValues(){
		var recordForm = document.getElementById("recordForm");
		if (recordForm!=null){
			var elementCount = document.getElementById("recordForm").elements.length;
			for (var questionNumber=1;questionNumber<=elementCount;questionNumber++){
				var checkboxQuestion=document.getElementById("checkbox-"+questionNumber);
				if (checkboxQuestion!=null){
					var checkboxValues = checkboxQuestion.value.split("&");
					var checkboxNumber = 1;
					var checkbox = document.getElementById("checkbox-"+questionNumber+"-"+checkboxNumber);
					while (checkbox!=null){
						for (var index = 0; index<checkboxValues.length; index++){
							if (checkboxNumber==checkboxValues[index]){
								checkbox.checked=true;
							}
						}
					checkboxNumber++;
					checkbox = document.getElementById("checkbox-"+questionNumber+"-"+checkboxNumber);
					}
				}
			}
		}
	}
	
	function editRecord (sessionMapID, recordIndex){
		var param = {"sessionMapID":sessionMapID,"recordIndex":recordIndex,"reqID":((new Date()).getTime())};
		$( "#addRecordDiv" ).load( 
			editRecordUrl, 
			param, 
			function() {
				readHiddenFormValues();
				doSelectTab(1);
			});
	}
	
	function clearVisibleFormElements (formName,protectedFormElementNames){
		var formElements = document.getElementById(formName).elements;
		for (var elemNumber = 0;elemNumber < formElements.length; elemNumber++){
			var elem = formElements[elemNumber];
			if (elem.name!=""){
				if (elem.type == "hidden" && protectedFormElementNames!=null){
					var save = false;
					for (var protectedElemNumber = 0; protectedElemNumber<protectedFormElementNames.length;protectedElemNumber++){
						if (protectedFormElementNames[protectedElemNumber]==elem.name){
							save=true;
							break;
						}
					}
					if (!save){
						elem.value="";
					}
				}
				else if (elem.type == "radio" || elem.type == "checkbox"){
					elem.checked=false;
				}
				else {
					elem.value="";
				}
			}
		}
	}
	
	function removeRecord(sessionMapID, recordIndex){
 		var displayedRecordNumber = document.getElementById("displayedRecordNumber").value;
		var param = {"sessionMapID":sessionMapID,"recordIndex":recordIndex,"displayedRecordNumber":displayedRecordNumber,"reqID":((new Date()).getTime())};
		$( "#recordListDiv" ).load( 
			removeRecordUrl, 
			param, 
			function() {
    			if (displayedRecordNumber==recordIndex){
    				clearVisibleFormElements("recordForm",["sessionMapID","displayedRecordNumber"]);
					displayedRecordNumber = parseInt(recordListLength);
					recordListLength--;
				}
				else if (displayedRecordNumber > recordIndex){
					displayedRecordNumber--;
				}
				document.getElementById("displayedRecordNumber").value=displayedRecordNumber;
				document.getElementById("displayedRecordNumberSpan").innerHTML=displayedRecordNumber;
				refreshQuestionSummaries(sessionMapID);
			});
	}
	
	function changeView(sessionMapID,displayedRecordNumber){
		var tabID = getCurrentTabID();
		if ( ! tabID ) {
			tabID = currentTab;
		}
		var param = {"sessionMapID":sessionMapID,"learningCurrentTab":tabID,"displayedRecordNumber":displayedRecordNumber,"reqID":((new Date()).getTime())};
		$( "#body" ).load( 
				changeViewUrl, 
				param, 
				function() {
	    			doSelectTab(tabID);
				});			
	}
	

	function refreshQuestionSummaries(sessionMapID){
		var param = {"sessionMapID":sessionMapID,"reqID":((new Date()).getTime())};
		$( "#questionSummariesDiv" ).load( refreshQuestionSummariesUrl, param);
	}