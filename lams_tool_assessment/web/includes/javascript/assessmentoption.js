	/*
	 This is Assessment Question option area.
	 */
	var optionTargetDiv = "#optionArea";
	// 	  Please set these 2 variables in JSP file for using tag reason:
	//    var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
	//    var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
	function addOption(){
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
			}
		);
	}
	function removeOption(idx){
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
				}
		);
	}
	function upOption(idx){
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
				}
		);
	}
	function downOption(idx){
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
					//alert("The last 25 entries in the feed have been loaded");
					//reinitializeCKEditorInstances();
					//alert("haha");
				}
		);
	}
	
	//in order to be able to use option's value, copy it from ckeditor to textarea
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
	

