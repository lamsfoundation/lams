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
		prepareFCKEditorsForAjaxSubmit();
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
		prepareFCKEditorsForAjaxSubmit();
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
 		prepareFCKEditorsForAjaxSubmit();
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
 		prepareFCKEditorsForAjaxSubmit();
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
	function prepareFCKEditorsForAjaxSubmit(){
		if ((questionType == 1) || (questionType == 3) || (questionType == 4)) {
			$("[name^=optionFeedback]").each(function() {
				this.value = FCKeditorAPI.GetInstance(this.name).GetXHTML();
			});
		}
		if (questionType == 2) {
			$("[name^=optionQuestion]").each(function() {
				this.value = FCKeditorAPI.GetInstance(this.name).GetXHTML();
			});
		}
	}
	

