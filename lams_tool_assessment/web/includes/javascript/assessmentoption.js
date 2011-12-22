	/*
	 This is Assessment Question option area.
	 */
	var optionTargetDiv = "#optionArea";
	// 	  Please set these 2 variables in JSP file for using tag reason:
	//    var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
	//    var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
	function addOption(){
		//store old InstanceIds before doing Ajax call. We need to keep record of old ones to prevent reinitializing new CKEditor two times.
		var oldOptionIds = new Array();
 		for (var instanceId in CKEDITOR.instances){
			oldOptionIds[instanceId] = instanceId;
		}
		
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
				reinitializeOptionEditors(oldOptionIds);
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
				},
				function(){
					reinitializeOptionEditors(null);
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
				},
				function(){
					reinitializeOptionEditors(null);
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
					reinitializeOptionEditors(null);
				}
		);
	}
	
	//reinitialize all CKEditors responsible for options after Ajax call has done
	function reinitializeOptionEditors(optionIds){
		if (optionIds == null) {
			optionIds = CKEDITOR.instances;
		}
		
		for (var instanceId in optionIds){
			//skip general fckeditors
			if (instanceId.match("^(optionString|optionQuestion|optionFeedback)")) { 
				if (instanceId == null) continue;
				var instance = CKEDITOR.instances[instanceId];
				if (instance == null) continue;
				var initializeFunction = instance.initializeFunction;
				CKEDITOR.remove(instance);
				//don't initialize elements that were deleted
				if ($("#" + instanceId).length == 0) continue;
				instance = initializeFunction();
				instance.initializeFunction = initializeFunction;
			}
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
	

