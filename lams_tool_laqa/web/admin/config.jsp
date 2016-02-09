<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<%@ include file="/includes/jsp/qaWizardCommon.jsp"%>
		
		<title><fmt:message key="pageTitle.admin" /></title>
		<lams:css/>
		<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
		
		<script src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
		<script src="<lams:LAMSURL/>/includes/javascript/jquery-ui.js"></script>

		<script type="text/javascript">
			// Creating a 3-dimentional javascript array for category/cognitive skill/question
			// -------------------------------------------------------------------------------
			var categoryArray = new Array();
			var categoryIndex = 0;
			var skillIndex = 0;
			var qIndex = 0;
			<c:forEach var="category" items="${categories}">
				categoryIndex = categoryArray.length;
				addCategory(unescape("${category.title}"), "${category.uid}",  categoryArray.length);
				<c:forEach var="skill" items="${category.cognitiveSkills}">
					skillIndex = categoryArray[categoryIndex].skills.length;
					addSkill(unescape("${skill.title}"), "${skill.uid}", categoryIndex , skillIndex);
					<c:forEach var="question" items="${skill.questions}">
						qIndex = categoryArray[categoryIndex].skills[skillIndex].questions.length;
						addQuestion(unescape("${question.question}"), "${question.uid}", categoryIndex, skillIndex, qIndex)
					</c:forEach>
				</c:forEach>
			</c:forEach>
			// -------------------------------------------------------------------------------
			
			// Arrays of items to be deleted
			var deleteCategories = new Array();
			var deleteSkills = new Array();
			var deleteQuestions = new Array();
			
			// the menus
			var catMenu = null;
			var skillMenu = null;
			var qMenu = null;
			
			// Offset for the dialog box
			var dialogOffSet=0;
			
			// Sets up the triple menu with the appropriate data
			function setUpTripleMenu() {
				var qaWizardEnabledBox = document.laqa11AdminForm.qaWizardEnabled;
				var wizardDiv = document.getElementById("wizardDiv");
				
				if (qaWizardEnabledBox.checked)	{
					wizardDiv.style.display = "block";
				}
				
				qaWizardEnabledBox.onclick=toggleWizard;
				
				catMenu = document.laqa11AdminForm.catMenu;
				skillMenu = document.laqa11AdminForm.skillMenu;
				qMenu = document.laqa11AdminForm.qMenu;
				
				nullOptions(catMenu);
				nullOptions(skillMenu);
				nullOptions(qMenu);
				
				var i;
				with (catMenu) {
					options[0] = new Option("<fmt:message key="wizard.selectCategory" />", "none");
					for(i = 0; i < categoryArray.length; i++) {
						options[i+1] = new Option(categoryArray[i].title, i);
					} 
					options[0].selected = true;
				}
			}
			
			// Edit an existing category
			function editCategory(title, index)	{
				var cat = categoryArray[index];
				cat.title = title;
				categoryArray[index] = cat;
			}
			
			// Edit an existing skill
			function editSkill(title, catIndex, index)	{
				var skill = categoryArray[catIndex].skills[index];
				skill.title = title;
				categoryArray[catIndex].skills[index] = skill;
			}
			
			// Edit an existing question
			function editQuestion(question, catIndex, skillIndex, index){
				var questionObj = categoryArray[catIndex].skills[skillIndex].questions[index];
				questionObj.question = question;
				categoryArray[catIndex].skills[skillIndex].questions[index] = questionObj;
			}
			
			// Toggle the wizard view when the checkbox is selected
			function toggleWizard()	{
				$("div.wizard").toggle("fast");
			}
			
			// Delete a category
			function deleteCategory(){
				if (catMenu.selectedIndex == null || catMenu.selectedIndex <= 0){
					alert("<fmt:message key="wizard.delete.notselected" />");
				} else {
					if (confirm("<fmt:message key="wizard.delete.warnDeleteCategory" />")) {
						var uid = categoryArray[catMenu.selectedIndex-1].uid;
						
						if(uid != null && uid) {
							deleteCategories[deleteCategories.length] = uid;
						}
						
						categoryArray.splice(catMenu.selectedIndex-1, 1);
						setUpTripleMenu();
						disableExport();
					}
				}
			}
			
			// Delete a skill
			function deleteSkill() {
				if (skillMenu.selectedIndex == null || skillMenu.selectedIndex <= 0) {
					alert("<fmt:message key="wizard.delete.notselected" />");
				} else {
					if (confirm("<fmt:message key="wizard.delete.warnDeleteSkill" />"))	{
						var uid = categoryArray[catMenu.selectedIndex-1].skills[skillMenu.selectedIndex-1].uid;
						
						if(uid != null && uid) {
							deleteSkills[deleteSkills.length] = uid;
						}
						
						categoryArray[catMenu.selectedIndex-1].skills.splice(skillMenu.selectedIndex-1, 1);
						changeCategory();
						disableExport();
					}
				}
			}
			
			// Delete a question
			function deleteQuestion() {
				if (qMenu.selectedIndex == null || qMenu.selectedIndex <= 0) {
					alert("<fmt:message key="wizard.delete.notselected" />");
				} else	{
					if (confirm("<fmt:message key="wizard.delete.warnDeleteQuestion" />"))	{
						var uid = categoryArray[catMenu.selectedIndex-1].skills[skillMenu.selectedIndex-1].questions[qMenu.selectedIndex-1].uid;
						
						if(uid != null && uid) {
							deleteQuestions[deleteQuestions.length] = uid;
						}
						
						categoryArray[catMenu.selectedIndex-1].skills[skillMenu.selectedIndex-1].questions.splice(qMenu.selectedIndex-1, 1);
						changeSkill();
						disableExport();
					}
				}
			}
			
			function trim(str) {
				return str.replace(/^\s+|\s+$/g, '');
			}
			
			// Special dialog for an edit
			function editDialog(inputType, isAdd, titleStr, selectDDM) {
				if(selectDDM.selectedIndex != null && selectDDM.selectedIndex > 0) {
					var existingString = selectDDM.options[selectDDM.selectedIndex].text;
					document.getElementById('inputText').value = existingString;
					openDialog(inputType, isAdd, titleStr);
				} else {
					alert("<fmt:message key="wizard.edit.notSelected" />");
				}
			}
			
			// Opens the required dialog based on the parameters
			function openDialog(inputType, isAdd, titleStr)	{

				disableExport();
				$("#catDialog").dialog("close");
				$("#catDialog").dialog({ 
					buttons: { 
						"<fmt:message key="wizard.ok" />": function() { 
					    	if(handleDialogInput(inputType, isAdd)) {
								document.getElementById('inputText').value = "";
					        	$(this).dialog("close"); 
					        }
					    }, 
					    "<fmt:message key="label.cancel" />": function() { 
					    	document.getElementById('inputText').value = "";
					    	$(this).dialog("close"); 
					    } 
					},
					height: 120,
					width: 725,
					title: titleStr,
					position: [33,290 + dialogOffSet]
				});
				document.getElementById("inputText").focus();
			}
			
			function handleDialogInput(inputType, isAdd){
				var inputText = trim(document.getElementById("inputText").value);
				if (inputText == null || inputText == ""){
					alert("<fmt:message key="wizard.edit.fieldRequired" />");
					return false
				}

				if (inputType=="category")	{
					with (catMenu) {
						if (isAdd) {
							addCategory(inputText, null, categoryArray.length, true);
							setUpTripleMenu();
							options[categoryArray.length].selected = true;
						} else {
							editCategory(inputText, catMenu.selectedIndex -1);
							var indexSelect = catMenu.selectedIndex;
							setUpTripleMenu();
							options[indexSelect].selected = true;
							changeCategory();
							changeSkill();
						}
						
					}
				} else if (inputType=="skill") {
					with (skillMenu) {
					
						if (isAdd) {
							if (catMenu.selectedIndex != null && catMenu.selectedIndex > 0) {
								addSkill(inputText, null, catMenu.selectedIndex - 1 , categoryArray[catMenu.selectedIndex - 1].skills.length);
								changeCategory();
								options[options.length -1].selected = true;
								
							} else {
								alert("<fmt:message key="wizard.add.mustSelectCategory" />");
								return false;
							}
						} else {
							editSkill(inputText, catMenu.selectedIndex -1, skillMenu.selectedIndex -1);
							indexSelect = skillMenu.selectedIndex;
							changeCategory();
							options[indexSelect].selected = true;
							changeSkill();
						}
					}
				} else if (inputType=="question") {
					with (qMenu) {
						var catIndex = catMenu.selectedIndex - 1;
						var skillIndex = skillMenu.selectedIndex -1;
						if (isAdd) {
							if (catMenu.selectedIndex != null && catMenu.selectedIndex > 0 && skillMenu.selectedIndex != null && skillMenu.selectedIndex > 0 )	{
								var qIndex = categoryArray[catIndex].skills[skillIndex].questions.length;
								addQuestion(inputText, null, catIndex, skillIndex, qIndex)
								changeSkill();
								options[options.length -1].selected = true;
							} else {
								alert("<fmt:message key="wizard.add.mustSelectSkill" />");
								return false;
							}
							
						} else {
							var indexSelect = qMenu.selectedIndex;
							editQuestion(inputText, catIndex, skillIndex, indexSelect -1)
							changeSkill();
							options[indexSelect].selected = true;
						}
					}
				}
				return true;	
			}
			
			// Serialises the javascript data befor performing the submit
			function submitForm(dispatch) {
				// Serialise the categores, skills an questions in the 3d array
				// into an xml string
				if (categoryArray.length > 0) {
					var xmlString = '<?xml version="1.0"?><categories>';
					var i, j, k;
					for (i=0; i<categoryArray.length; i++) {
						var categoryString = '<category'+
							' uid="' + categoryArray[i].uid + '"'+
							' title="' + escape(categoryArray[i].title) + '"'+
							' >';
					
						for(j=0; j<categoryArray[i].skills.length; j++)	{
							var skillString = '<skill'+
								' uid="'+ categoryArray[i].skills[j].uid + '"'+
								' title="'+ escape(categoryArray[i].skills[j].title) + '"'+
								' >';
							
							for (k=0; k<categoryArray[i].skills[j].questions.length; k++) {
								var questionString = '<question'+
									' uid="'+ categoryArray[i].skills[j].questions[k].uid + '"'+
									' question="'+ escape(categoryArray[i].skills[j].questions[k].question) + '"'+
									' />';
								skillString += questionString;
							}
							skillString += "</skill>";
							categoryString += skillString;
						}
						categoryString += "</category>";
						xmlString += categoryString;
					}
					xmlString += "</categories>";
					document.getElementById("serialiseXML").value = xmlString;
				}
				
				// serialise the deleted catergories
				if (deleteCategories.length >0) {
					var i;
					var CSV = deleteCategories[0];
					for (i=1; i< deleteCategories.length; i++) {
						CSV += "," + deleteCategories[i];
					}
					document.getElementById("deleteCategoriesCSV").value = CSV;
				}
				
				// serialise the deleted skills
				if (deleteSkills.length >0) {
					var i;
					var CSV = deleteSkills[0];
					for (i=1; i< deleteSkills.length; i++) {
						CSV += "," + deleteSkills[i];
					}
					document.getElementById("deleteSkillsCSV").value = CSV;
				}
				
				// serialise the deleted questions
				if (deleteQuestions.length >0) {
					var i;
					var CSV = deleteQuestions[0];
					for (i=1; i< deleteQuestions.length; i++) {
						CSV += "," + deleteQuestions[i];
					}
					document.getElementById("deleteQuestionsCSV").value = CSV;
				}
				
				document.getElementById("dispatch").value = dispatch;
				document.laqa11AdminForm.submit();
			}
			
			function customSubmit(dispatch) {
				document.getElementById("dispatch").value = dispatch;
				document.laqa11AdminForm.submit();
			}
			
			function disableExport() {
				msg = "<fmt:message key="wizard.export.savefirst" />"
				document.getElementById("exportButton").href = "javascript:alert('" + msg + "');";
			}
			
			function importFile() {
				if (document.getElementById("importFile").value.length == 0) {
					alert("<fmt:message key='wizard.import.nofile'/>");
				} else {
					if(confirm("<fmt:message key="wizard.import.warn" />")) {
						customSubmit("importWizard");
					}
				}
			}
		</script>
	</lams:head>
	
	<body class="stripes" onload="javascript:setUpTripleMenu();">

		<div id="content">
	
		<h1>
			<fmt:message key="pageTitle.admin" />
		</h1>
		
		<a href="<lams:LAMSURL/>/admin/sysadminstart.do"><fmt:message key="admin.return" /></a>

		<c:if test="${error}">
			<p class="warning">
				<fmt:message key="${errorKey}" />
			</p>
			<script type="text/javascript">
			<!--
				dialogOffSet += 60;
			//-->
			</script>
		</c:if>
		<c:if test="${savedSuccess}">
			<p class="info">
				<fmt:message key="admin.success" />
			</p>
			<script type="text/javascript">
			<!--
				dialogOffSet += 60;
			//-->
			</script>
		</c:if>
		<c:if test="${deleteSuccess}">
			<p class="info">
				<fmt:message key="${deleteLangKey}" />
			</p>
			<script type="text/javascript">
			<!--
				dialogOffSet += 60;
			//-->
			</script>
		</c:if>
		
		<html:form action="/laqa11admin" styleId="laqa11AdminForm" method="post" enctype="multipart/form-data">
			<html:hidden property="serialiseXML" styleId="serialiseXML" value="" />
			<html:hidden property="deleteCategoriesCSV" styleId="deleteCategoriesCSV" value="" />
			<html:hidden property="deleteSkillsCSV" styleId="deleteSkillsCSV" value="" />
			<html:hidden property="deleteQuestionsCSV" styleId="deleteQuestionsCSV" value="" />
			<html:hidden property="dispatch" styleId="dispatch" value="saveContent" />
			
			<table class="alternative-color">
				<tr>
					<td width="70%">
						<fmt:message key="admin.enableWizard" />
					</td>
					<td width="30%">
						<html:checkbox property="qaWizardEnabled" styleId="qaWizardEnabled"></html:checkbox>
					</td>
				</tr>
			</table>

			<div class="wizard" style="display:none;" id="wizardDiv">
				<h3><fmt:message key="wizard.wizardTitle" /></h3>
				<br />
				<table border="0" cellspacing="10px">
					<tbody>
					
					    <tr>
					      	<td align="left" valign="top" width="300">
								<select name="catMenu" onchange="changeCategory()" size="1" style="font-size:10px; width:100%;">
								</select> 
					      	</td>
					      	<td align="left">
					      		<a href='javascript:openDialog("category", true, &quot;<fmt:message key="wizard.addCategory" />&quot;);' class="button"  ><fmt:message key="wizard.add" /></a>
					      		&nbsp;
					      		<a href='javascript:editDialog("category", false, &quot;<fmt:message key="wizard.editCategory" />&quot;, document.laqa11AdminForm.catMenu);' class="button"  ><fmt:message key="wizard.edit" /></a>
					      		&nbsp;
					      		<a href='javascript:deleteCategory();' class="button"  ><fmt:message key="wizard.delete" /></a>
					      	</td>
					    </tr>
					    
					    <tr>
					      	<td align="left" valign="top" width="300">
						      	<select name="skillMenu" onchange="changeSkill()" size="1" style="font-size:10px; width:100%;">
						      	</select> 
					      	</td>
					      	<td align="left" valign="top">
					      		<a href='javascript:openDialog("skill", true, &quot;<fmt:message key="wizard.addSkill" />&quot;);' class="button"  ><fmt:message key="wizard.add" /></a>
					      		&nbsp;
					      		<a href='javascript:editDialog("skill", false, &quot;<fmt:message key="wizard.editSkill" />&quot;, document.laqa11AdminForm.skillMenu);' class="button"  ><fmt:message key="wizard.edit" /></a>
					      		&nbsp;
					      		<a href='javascript:deleteSkill();' class="button"  ><fmt:message key="wizard.delete" /></a>
					      	</td>
					    </tr>
					    
					    <tr>
					      	<td align="left" valign="top" width="300">
					      		<select name="qMenu" size="1" style="font-size:10px; width:100%;">
					      		</select>
					      	</td>
					      	<td align="left" valign="top" >
					      		<a href='javascript:openDialog("question", true, &quot;<fmt:message key="wizard.addQuestion" />&quot;);' class="button"  ><fmt:message key="wizard.add" /></a>
					      		&nbsp;
					      		<a href='javascript:editDialog("question", false, &quot;<fmt:message key="wizard.editQuestion" />&quot;, document.laqa11AdminForm.qMenu);' class="button"  ><fmt:message key="wizard.edit" /></a>
					      		&nbsp;
					      		<a href='javascript:deleteQuestion();' class="button"  ><fmt:message key="wizard.delete" /></a>
					      	</td>
					    </tr>
					    
					</tbody>
				</table>
				<br />
				
				<a href="javascript:customSubmit('exportWizard')" id="exportButton" class="button"  >
					<fmt:message key="wizard.export.export" />
				</a>
				&nbsp;
				
				<a href="javascript:importFile()" class="button"  >
					<fmt:message key="wizard.import.import" />
				</a>
				
				<html:file property="importFile" styleId="importFile" />
				<br />
				<br />
				<br />
				<br />
			</div>
		
			<a href="javascript:submitForm('saveContent')" class="button"  >
				<fmt:message key="label.save" />
			</a>
		
			<div id="dialogDiv" style="display: none;">
				<div id="catDialog" class="ui-dialog ui-draggable ui-resizable">
					<input type="text" id="inputText" width="255" size="90">
				</div>	
			</div>
		</html:form>	
		</div><!--closes content-->
	
		<div id="footer"></div><!--closes footer-->
	</body>

</head>