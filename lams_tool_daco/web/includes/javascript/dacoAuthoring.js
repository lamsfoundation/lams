	//"Div" area that holds the answer options
	var answerOptionTargetDiv = "#answerOptionsArea";
	
	//Checks if the element exists and has an empty value
	function checkNonDefaultValue(elementName){
	    var elem = document.getElementById(elementName);
    	return (elem!=null && elem.value!="");
	}
	
	//Checks if the additional options area has all the default values, so it may stay hidden
   function defaultShowAdditionaOptionsArea(){
    if (checkNonDefaultValue("max") 
    	|| checkNonDefaultValue("min")
    	|| checkNonDefaultValue("digitsDecimal")
    	|| $('#questionRequired').attr('checked')
    	|| ($("#noSummaryOption").length>0 && !$("#noSummaryOption").attr("selected"))) {
   		toggleAdditionalOptionsArea();
   }
  }
  
  	//Shows or hides the additional options area
   function toggleAdditionalOptionsArea(){
	$("#additionalOptionsArea").toggle("slow",function(){
			if ($('#additionalOptionsArea').is(':visible')){
				$('#toggleAdditionalOptionsAreaLink').text(msgHideAdditionalOptions);
				$('#faIcon').toggleClass('fa-plus-square-o fa-minus-square-o');
			}
			else {
				$('#toggleAdditionalOptionsAreaLink').text(msgShowAdditionalOptions);
				$('#faIcon').toggleClass('fa-plus-square-o fa-minus-square-o')
				
			}
		});
	}
	
	//Creates an additional blank answer option
	function addAnswerOption(){
	    var reqIDVar = new Date();
	    var param = $("#answerOptionsForm").serializeArray();
	    
	    addToJSON(param,'reqID',reqIDVar.getTime());
	    
	    answerOperationStarted();
	    $(answerOptionTargetDiv).load(addAnswerOptionUrl,param,answerOperationComplete);
	}
	
	//Removes the chosen answer option
	function removeAnswerOption(index){
	    var reqIDVar = new Date();
	    var param = $("#answerOptionsForm").serializeArray();
	    
	    addToJSON(param,'reqID',reqIDVar.getTime());
	    addToJSON(param,'removeIndex',index);
	    	    
	    answerOperationStarted();
	    $(answerOptionTargetDiv).load(removeAnswerOptionUrl,param,answerOperationComplete);
	}
	
	//Moves the chosen answer option one place up
	function upItem(itemIndex){
		if(itemIndex == 0)
			return;
		var currId = "#answerOptionItemDesc" + itemIndex;
		var repId = "#answerOptionItemDesc" + (--itemIndex);
		switchValue(currId,repId);
	}
	
	//Moves the chosen answer option one place down
	function downItem(itemIndex,maxSize){
		if(itemIndex == (maxSize -1))
			return;
		var currId = "#answerOptionItemDesc" + itemIndex;
		var repId = "#answerOptionItemDesc" + (++itemIndex);
		switchValue(currId,repId);
	}
	
	//Switches the value of two elements
	function switchValue(currId,repId){
		var temp = $(repId).val();
		$(repId).val($(currId).val());
		$(currId).val(temp);
	}
	
	//Displays the busy status of the answer option operation
	function answerOperationStarted(){
		showBusy(answerOptionTargetDiv);
	}
	
	//Hides the busy status of the answer option operation
	function answerOperationComplete(){
		hideBusy(answerOptionTargetDiv);
	}
	
	//Packs additional elements and submits the question form
	function submitDacoQuestion(){
	
		if ( typeof CKEDITOR !== 'undefined' ) {
			for ( instance in CKEDITOR.instances )
				CKEDITOR.instances[instance].updateElement();
		}

		var questionType = $("#questionType").val();
    
		if(questionType==7 || questionType==8 || questionType==9){
			$("#answerOptionList").val($("#answerOptionsForm").serialize());
		}
		
		else if (questionType == 10){
			var longlatMapsString = "";
			$("#longlatMaps option:selected").each(function (){
				longlatMapsString += this.value + "&";
			});
			$("#longlatMapsSelected").val(longlatMapsString);
		}

	    $.ajax({
	           type: $("#dacoQuestionForm").attr('method'),
	           url: $("#dacoQuestionForm").attr('action'),
	           data: $("#dacoQuestionForm").serialize(), // serializes the form's elements.
	           success: function(data) {
	               $('#questionInputArea').html(data);
	           }
	         });
	}   

	//Cancels a question adding procedure
	function cancelDacoQuestion(){
		window.hideQuestionInputArea ? window.hideQuestionInputArea() : window.parent.hideQuestionInputArea();
	}