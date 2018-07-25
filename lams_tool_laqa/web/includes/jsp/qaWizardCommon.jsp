<!-- Javascript functions used for q&a wizard, made into jsp for language tags  -->
<script language="JavaScript" type="text/JavaScript">
	
	// Add a category to the categoryArray
	function addCategory(title, uid, index) {
		var cat = new Object();
		cat.title = title;
		cat.uid = uid;
		cat.skills = new Array();
		categoryArray[index] = cat;
	}
	
	// Adds a skill object to the skill array (containted in the category array)
	function addSkill(title, uid, catIndex, index) {
		var skill = new Object();
		skill.title = title;
		skill.uid = uid;
		skill.questions = new Array();
		categoryArray[catIndex].skills[index] = skill;
	}
	
	// Adds a question object to the question array, (contained within the skill array0
	function addQuestion(question, uid, catIndex, skillIndex, index) {
		var questionObj = new Object();
		questionObj.uid = uid;
		questionObj.question = question;
		categoryArray[catIndex].skills[skillIndex].questions[index] = questionObj;
	}
	
	// Removes all the options in a menu
	function nullOptions(aMenu) {
		aMenu.options.length = 0;
	}
	
	// Changes a category when it is selected, the skills options are populated with the corresponding data
	function changeCategory() {
		nullOptions(skillMenu);
		nullOptions(qMenu);
	
		if (catMenu.selectedIndex != 0)
		{
			var skillArray = categoryArray[catMenu.selectedIndex -1].skills;
			
			var i;
			with (skillMenu) {
				options[0] = new Option("<fmt:message key="wizard.selectSkill" />", "none");
				for(i = 0; i < skillArray.length; i++)
				{
					options[i+1] = new Option(skillArray[i].title, i);
				} 
				options[0].selected = true;
			}
		}
	}
	
	// Changes a skill when selected, the questions options are poulated with the corresponding data
	function changeSkill() {
		nullOptions(qMenu);
	
		if (catMenu.selectedIndex != 0 && skillMenu.selectedIndex != 0){
			var questionArray = categoryArray[catMenu.selectedIndex -1].skills[skillMenu.selectedIndex -1].questions;
			
			var i;
			with (qMenu) {
				options[0] = new Option("<fmt:message key="wizard.selectQuestion" />", "none");
				for(i = 0; i < questionArray.length; i++) {
					options[i+1] = new Option(questionArray[i].question, i);
				} 
				options[0].selected = true;
			}
		}
	}
</script>