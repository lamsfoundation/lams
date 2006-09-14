	/*
	 This is Survey Item instrcution area.
	 */
    var instructionTargetDiv = "instructionArea";
    var itemAttachmentTargetDiv = "itemAttachmentArea";
// Please set these 2 variables in JSP file for using tag reason:
//    var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
//    var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	function removeInstruction(idx){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url= removeInstructionUrl;
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&removeIdx="+idx+"&reqID="+reqIDVar.getTime();
	    removeInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	instructionTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:removeInstructionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	
    function changeMaxAnswerOptions(initVal){
		var tb = document.getElementById("questionTable");
		var num = tb.getElementsByTagName("tr");
		var numLen = num.length;
		//remove button row
		if(numLen > 0)
			--numLen;
			
		var sel = document.getElementById("maxAnswerSelect");
		var newField = sel.options;
		var len = sel.length;
		
		if(numLen == 0){
			sel.disabled = true;
			return;
		}else
			sel.disabled = false;
		
		//when first enter, it should get value from Resource
		var selIdx=initVal < 0?-1:initVal;
		//there is bug in Opera8.5: if add alert before this loop, it will work,weird.
		for (var idx=0;idx<len;idx++)
		{
			if(newField[0].selected && selIdx == -1 ){
				selIdx = newField[0].value;
			}
			sel.removeChild(newField[0]);
		}
	
		for(var i=1;i<=numLen;i++){
			var opt = document.createElement("option");
			var optT =document.createTextNode(i);
			opt.value=i;
			//get back user choosen value
			if(selIdx > 0 && selIdx==i){
				opt.selected = true;
			}else{
				opt.selected = false;
			}
			opt.appendChild(optT);
			sel.appendChild(opt);
		}
	}
	function addInstruction(){
		var url= addInstructionUrl;
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&reqID="+reqIDVar.getTime();
		addInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	instructionTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:addInstructionComplete,
		    		evalScripts:true
		    	}
	    );
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
		var temp = $(repId).value;
		$(repId).value =  $(currId).value;
		$(currId).value= temp;
	}
	function removeInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function removeInstructionComplete(){
		hideBusy(instructionTargetDiv);
		
		changeMaxAnswerOptions(-1);
	}
	
	function addInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function addInstructionComplete(){
		hideBusy(instructionTargetDiv);
		
		changeMaxAnswerOptions(-1);
	}
	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}
	
	function submitSurveyItem(){
		$("instructionList").value = Form.serialize("instructionForm");
		$("surveyItemForm").submit();
		//after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" surveylist and close this window.
	}
	function cancelSurveyItem(){
		window.top.hideMessage();
	}
