	function init(){
	 doSelectTab(currentTab);
	 readCheckboxQuestionsValues();
    }
        
    function doSelectTab(tabId) {
	  selectTab(tabId);
	  currentTab = tabId;
    }
        
	function finishSession(){
		document.getElementById("finishButton").disabled = true;
		document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		return false;
	}
		
	function continueReflect(){
		document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
	}
	
	function saveOrUpdateRecord()	{
		setCheckboxQuestionsValues();
		document.getElementById("recordForm").submit();
	}
	
	function setCheckboxQuestionsValues(){
		var elementCount = document.getElementById("recordForm").elements.length;
		for (var checkboxQuestionNumber=1;checkboxQuestionNumber<=elementCount;checkboxQuestionNumber++){
			var checkboxQuestion=document.getElementById("checkbox-"+checkboxQuestionNumber);
			if (checkboxQuestion!=null){
				var checkboxQuestionValue = "";
				var checkboxNumber = 1;
				var checkbox = document.getElementById("checkbox-"+checkboxQuestionNumber+"-"+checkboxNumber);
				while (checkbox!=null){
					if (checkbox.checked){
						checkboxQuestionValue += checkboxNumber + "&";
					}
				checkboxNumber++;
				checkbox = document.getElementById("checkbox-"+checkboxQuestionNumber+"-"+checkboxNumber);
				}
				checkboxQuestion.value=checkboxQuestionValue;
			}
		}
	}
	
	function readCheckboxQuestionsValues(){
		var elementCount = document.getElementById("recordForm").elements.length;
		for (var checkboxQuestionNumber=1;checkboxQuestionNumber<=elementCount;checkboxQuestionNumber++){
			var checkboxQuestion=document.getElementById("checkbox-"+checkboxQuestionNumber);
			if (checkboxQuestion!=null){
				var checkboxValues = checkboxQuestion.value.split("&");
				var checkboxNumber = 1;
				var checkbox = document.getElementById("checkbox-"+checkboxQuestionNumber+"-"+checkboxNumber);
				while (checkbox!=null){
					for (var index = 0; index<checkboxValues.length; index++){
						if (checkboxNumber==checkboxValues[index]){
							checkbox.checked=true;
						}
					}
				checkboxNumber++;
				checkbox = document.getElementById("checkbox-"+checkboxQuestionNumber+"-"+checkboxNumber);
				}
			}
		}
	}
	
	function editRecord (sessionMapID, recordIndex){
		var param = "sessionMapID="+sessionMapID+"&recordIndex="+recordIndex+"&reqID="+((new Date()).getTime());
	    new Ajax.Updater(
		    	document.getElementById("addRecordDiv"),
		    	editRecordUrl,
		    	{
		    		method:'post',
		    		parameters:param,
		    		onComplete: function(){
		    			readCheckboxQuestionsValues();
		    			doSelectTab(1);
		    		},
		    		evalScripts:true
		    	}
	    );
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
	   	var param = "sessionMapID="+sessionMapID+"&recordIndex="+recordIndex+"&displayedRecordNumber="+displayedRecordNumber+"&reqID="+((new Date()).getTime());    
	    new Ajax.Updater(
		    	document.getElementById("recordListDiv"),
		    	removeRecordUrl,
		    	{
		    		method:'post',
		    		parameters:param,
		    		onComplete: function(){
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
		    		},
		    		evalScripts:true
		    	}
	    );
	}
	
	function changeView(sessionMapID,displayedRecordNumber){
	   	var param = "sessionMapID="+sessionMapID+"&reqID="+((new Date()).getTime())+"&displayedRecordNumber="+displayedRecordNumber;
	   	var previousCurrentTab = currentTab;
	    new Ajax.Updater(
	    		"body",
		    	changeViewUrl,
		    	{
		    		method:'post',
		    		parameters:param,
		    		onComplete: function(){
		    			var tabNumber = 1;
		    			while (document.getElementById("tabbody"+tabNumber)!=null){
		    				doSelectTab(tabNumber++);
		    			}
		    			doSelectTab(previousCurrentTab);
		    		},
		    		evalScripts: true
		    	}
	    );
	}
	

	function resizeHorizontalRecordListFrame(){
		var horizontalRecordListFrame = document.getElementById('horizontalRecordListFrame');
		if (horizontalRecordListFrame!=null){
			horizontalRecordListFrame.style.height=((questionListLength+1)*111)+'px';		
		}
	}