	/*
	 This is Assessment Question option area.
	 */
	var optionTargetDiv = "#optionArea";
	// 	  Please set these 2 variables in JSP file for using tag reason:
	//    var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
	//    var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
	function addOption(){
		var oldCkeditorInstances = storeOldCkeditorInstances();
		
		var url= addOptionUrl;
		var contentFolderID= $("#contentFolderID").val();
		prepareOptionEditorsForAjaxSubmit();
		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
			url,
			{
				contentFolderID: contentFolderID,
				questionType: questionType,
				optionList: optionList 
			},
			function(){
				reinitializeGeneralCKEditorInstances(oldCkeditorInstances);
			}
		);
	}
	function removeOption(idx){
		var oldCkeditorInstances = storeOldCkeditorInstances();
		
 		var url= removeOptionUrl;
		var contentFolderID= $("#contentFolderID").val();
		prepareOptionEditorsForAjaxSubmit();
 		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
				url,
				{
					contentFolderID: contentFolderID,					
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				},
				function(){
					reinitializeGeneralCKEditorInstances(oldCkeditorInstances);
				}
		);
	}
	function upOption(idx){
		var oldCkeditorInstances = storeOldCkeditorInstances();
		
 		var url= upOptionUrl;
		var contentFolderID= $("#contentFolderID").val();
		prepareOptionEditorsForAjaxSubmit();
 		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
				url,
				{
					contentFolderID: contentFolderID,					
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				},
				function(){
					reinitializeGeneralCKEditorInstances(oldCkeditorInstances);
				}
		);
	}
	function downOption(idx){
		var oldCkeditorInstances = storeOldCkeditorInstances();
		
 		var url= downOptionUrl;
		var contentFolderID= $("#contentFolderID").val(); 	
		prepareOptionEditorsForAjaxSubmit();
 		var optionList = $("#optionForm").serialize(true);
 		$(optionTargetDiv).load(
				url,
				{
					contentFolderID: contentFolderID,	
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				},
				function(){
					reinitializeGeneralCKEditorInstances(oldCkeditorInstances);
				}
		);
	}
	
	//store references to general CKEditors before doing Ajax call
	function storeOldCkeditorInstances(oldCkeditorInstances){
 		var oldCkeditorInstances = new Array();
 		oldCkeditorInstances.question = CKEDITOR.instances["question"];
 		oldCkeditorInstances.generalFeedback = CKEDITOR.instances["generalFeedback"];  
 		oldCkeditorInstances.feedbackOnCorrect = CKEDITOR.instances["feedbackOnCorrect"];
 		oldCkeditorInstances.feedbackOnIncorrect = CKEDITOR.instances["feedbackOnIncorrect"];  
 		oldCkeditorInstances.feedbackOnCorrectOutsideForm = CKEDITOR.instances["feedbackOnCorrectOutsideForm"];
 		oldCkeditorInstances.feedbackOnPartiallyCorrectOutsideForm = CKEDITOR.instances["feedbackOnPartiallyCorrectOutsideForm"];
 		oldCkeditorInstances.feedbackOnIncorrectOutsideForm = CKEDITOR.instances["feedbackOnIncorrectOutsideForm"];
 		return oldCkeditorInstances;
	}
	
	//reinitialize all general CKEditor after Ajax call has done
	function reinitializeGeneralCKEditorInstances(oldCkeditorInstances){
		for (var instanceId in oldCkeditorInstances){
			if (instanceId == null) continue;
			var instance = oldCkeditorInstances[instanceId];
			if (instance == null) continue;
			var initializeFunction = instance.initializeFunction;
			instance.destroy();
			instance = initializeFunction();
			instance.initializeFunction = initializeFunction;
		}
	}
	
	//in order to be able to use option's value, copy it from CKEditor to textarea
	function prepareOptionEditorsForAjaxSubmit(){
		if ((questionType == 1) ||  (questionType == 7)) {
			$("textarea[name^=optionString]").each(function() {
				prepareOptionEditorForAjaxSubmit(this);
			});
			
		} else if (questionType == 2) {
			$("[name^=optionQuestion]").each(function() {
				prepareOptionEditorForAjaxSubmit(this);
			});
			
		} else if ((questionType == 3) || (questionType == 4)) {
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
	

