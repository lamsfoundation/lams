	/*
	 This is Survey Item instruction area.
	 */
// Please set these 2 variables in JSP file for using tag reason:
//    var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
//    var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	function removeInstruction(idx){
	    var reqIDVar = new Date();
	    var param = $("#instructionForm").serialize()+"&removeIdx="+idx+"&reqID="+reqIDVar.getTime();
 		var url= removeInstructionUrl+"?"+param;
	    showInstructionBusy();
	    $.ajaxSetup({ cache: true });
	    $('#instructionArea').load(url,function(data) {
	    	hideInstructionBusy();
	    });
	}
	
	function addInstruction(){
	    var reqIDVar = new Date();
	    var param = $("#instructionForm").serialize()+"&reqID="+reqIDVar.getTime();
		var url= addInstructionUrl+"?"+param;
	    showInstructionBusy();
	    $.ajaxSetup({ cache: true });
	    $('#instructionArea').load(url,function() {
	    	hideInstructionBusy();
	    });
	}
	function upItem(itemIdx){
		if(itemIdx == 0)
			return;
		var currId = "instructionItemDesc" + itemIdx;
		var repId = "instructionItemDesc" + (--itemIdx);
		switchValue(currId,repId);
	}
	function downItem(itemIdx,maxSize){
		if(itemIdx == (maxSize -1))
			return;
		var currId = "instructionItemDesc" + itemIdx;
		var repId = "instructionItemDesc" + (++itemIdx);
		switchValue(currId,repId);
	}
	function switchValue(currId,repId){
		var current = document.getElementById(currId);
		var rep = document.getElementById(repId);
		var temp = rep.value;
		rep.value = current.value;
		current.value= temp;
	}
	function showInstructionBusy(){
		$('#instructionAreaBusy').show();
	}
	function hideInstructionBusy(){
		$('#instructionAreaBusy').hide();
	}
	
	function refreshCKEditors() {
		// make sure all the ckeditors are refreshed, not just the validated ones
		for (var i in CKEDITOR.instances) {
       		CKEDITOR.instances[i].updateElement();
   		}
	}

	function submitSurveyItem(){
		refreshCKEditors();
		if (document.getElementById("instructionForm")) {
			document.getElementById("instructionList").value = $("#instructionForm").serialize();
		}
		$.ajax({
			data: $("#surveyItemForm").serialize(),
	       	type: $("#surveyItemForm").attr('method'),
			url: $("#surveyItemForm").attr('action'),
			success: function(data) {
	           $('#questionInputArea').html(data);
			}
		});
	}
	
	function cancelSurveyItem(){
		hideMessage();
	}
