	/*
	 This is Assessment Question option area.
	 */
	var optionTargetDiv = "#optionArea";
	// 	  Please set these 2 variables in JSP file for using tag reason:
	//    var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
	//    var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
	function addOption(){
		var url= addOptionUrl;
		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
			url,
			{
				questionType: questionType,
				optionList: optionList 
			}
		);
	}
	function removeOption(idx){
 		var url= removeOptionUrl;
 		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
				url,
				{
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				}
		);
	}
	function upOption(idx){
 		var url= upOptionUrl;
 		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
				url,
				{
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				}
		);
	}
	function downOption(idx){
 		var url= downOptionUrl;
 		var optionList = $("#optionForm").serialize(true);
		$(optionTargetDiv).load(
				url,
				{
					questionType: questionType,
					optionIndex: idx,
					optionList: optionList 
				}
		);
	}
	

