<%@ include file="/common/taglibs.jsp"%>

<!--   Advance Tab Content    -->

<lams:SimplePanel titleKey="label.posting.options" titleHelpURL="https://wiki.lamsfoundation.org/display/lamsdocs/Home">

<div class="checkbox">
<label for="rich-editor"><html:checkbox property="forum.allowRichEditor" styleId="rich-editor"/>
<fmt:message key="label.authoring.advance.use.richeditor" /></label>
</div>

<div class="checkbox">
<label for="allow-upload"><html:checkbox property="forum.allowUpload" styleId="allow-upload"/>
<fmt:message key="label.authoring.advance.allow.upload" /></label>
</div>

<div class="checkbox">
<label for="allowEdit"><html:checkbox property="forum.allowEdit" styleId="allowEdit" />
<fmt:message key="label.authoring.advance.allow.edit" /></label>
</div>

<div class="checkbox">
<label for="limited-min-characters"><html:checkbox property="forum.limitedMinCharacters" styleId="limited-min-characters"/>
<fmt:message key="label.authoring.advance.min.limited.input" />
</label>
<html:text property="forum.minCharacters" styleId="min-characters"  styleClass="form-control form-control-inline input-sm"/>
</div>

<div class="checkbox">
<label for="limited-max-characters"><html:checkbox property="forum.limitedMaxCharacters" styleId="limited-max-characters"/>
<fmt:message key="label.authoring.advance.limited.input" /></label>
<html:text property="forum.maxCharacters" styleId="max-characters"  styleClass="form-control form-control-inline input-sm"/>
</div>

<div class="panel panel-default">
<div class="panel-body panel-body-sm">

<div class="checkbox">
<label for="allowRateMessages"><html:checkbox property="forum.allowRateMessages" styleId="allowRateMessages"  onclick="checkRating()"/>
<fmt:message key="label.authoring.advance.allow.rate.postings" /></label>
</div>

	<div class="form-inline loffset20">
		<div class="form-group">
		<label for="minimumRate"><fmt:message key="label.authoring.advance.minimum.reply" /></label>
		<html:select property="forum.minimumRate" styleId="minimumRate"  onmouseup="validateRatings(true);" styleClass="form-control input-sm">
			<html:option value="0">
				<fmt:message key="label.authoring.advance.no.minimum" />
			</html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
		</div>
		<div class="form-group">
		<label for="maximumRate"><fmt:message key="label.authoring.advance.maximum.reply" /></label>
		<html:select property="forum.maximumRate" styleId="maximumRate"   onmouseup="validateRatings(false);"  styleClass="form-control input-sm">
			<html:option value="0">
				<fmt:message key="label.authoring.advance.no.maximum" />
			</html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
		</div>
	</div>
</div>
</div>

<div class="panel panel-default">
<div class="panel-body panel-body-sm">
	<fmt:message key="message.posting.limiting" />
	<div class="radio">
	<label for="allowNewTopic1">
	<html:radio property="forum.allowNewTopic" value="true"	onclick="allowNewTopic()" styleId="allowNewTopic1"/>
	<fmt:message key="label.authoring.advance.allow.new.topics" />
	</label>
	</div>
	
	<div class="radio">
	<label for="allowNewTopic2">
	<html:radio property="forum.allowNewTopic" value="false" onclick="allowNewTopic()" styleId="allowNewTopic2"/>
	<fmt:message key="label.authoring.advance.number.reply" />
	</label>
	</div>
	
	<div class="form-inline loffset20">
		<div class="form-group">
		<label for="minimumReply"><fmt:message key="label.authoring.advance.minimum.reply" /></label>
		<html:select property="forum.minimumReply" styleId="minimumReply" styleClass="form-control input-sm">
			<html:option value="0">
				<fmt:message key="label.authoring.advance.no.minimum" />
			</html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
		</div>
		<div class="form-group">
		<label for="maximumReply"><fmt:message key="label.authoring.advance.maximum.reply" /></label>
		<html:select property="forum.maximumReply" styleId="maximumReply" styleClass="form-control input-sm">
			<html:option value="0">
				<fmt:message key="label.authoring.advance.no.maximum" />
			</html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
		</div>
	</div>
</div>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
<div class="form-group">
<label><fmt:message key="label.authoring.advanced.send.emails.to" /></label>
<div class="form-inline">
	<div class="checkbox loffset20">
	<label for="notifyLearnersOnForumPosting"><html:checkbox property="forum.notifyLearnersOnForumPosting" styleId="notifyLearnersOnForumPosting"/>&nbsp;
	<fmt:message key="label.authoring.advanced.learners" /></label>
	</div>
	<div class="checkbox loffset10">
	<label for="notifyTeachersOnForumPosting"><html:checkbox property="forum.notifyTeachersOnForumPosting" styleId="notifyTeachersOnForumPosting"/>&nbsp;
	<fmt:message key="label.authoring.advanced.teachers" /></label>
	</div>
</div>
</div>

<div class="checkbox">
<label for="notifyLearnersOnMarkRelease"><html:checkbox property="forum.notifyLearnersOnMarkRelease" styleId="notifyLearnersOnMarkRelease"/>
<fmt:message key="label.authoring.advanced.notify.mark.release" /></label>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
<label for="lockWhenFinished"><html:checkbox property="forum.lockWhenFinished" styleId="lockWhenFinished" />
<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
</div>

<div class="checkbox">
<label for="reflectOn"><html:checkbox property="forum.reflectOnActivity"	styleId="reflectOn"/>
<fmt:message key="advanced.reflectOnActivity" /></label>
</div>
<div class="form-group">
<html:textarea property="forum.reflectInstructions" styleId="reflectInstructions"  styleClass="form-control" rows="3"
	onkeyup="javascript:turnOnReflect()"/>
</div>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOn");
	function turnOnRefect() {
		if (isEmpty(ra.value)) {
			//turn off	
			rao.checked = false;
		} else {
			//turn on
			rao.checked = true;
		}
	}

	ra.onkeyup = turnOnRefect;
</script>

</lams:SimplePanel>

<script type="text/javascript">

	var limitedMinCharacters = document.getElementById("limited-min-characters");
	var minCharacters = document.getElementById("min-characters");
	var limitedMaxCharacters = document.getElementById("limited-max-characters");
	var maxCharacters = document.getElementById("max-characters");

	limitedMinCharacters.onclick = initAdvanced;
	limitedMaxCharacters.onclick = initAdvanced;
	function initAdvanced() {
		if (limitedMinCharacters.checked) {
			minCharacters.disabled = false;
		} else {
			minCharacters.disabled = true;
		}

		if (limitedMaxCharacters.checked) {
			maxCharacters.disabled = false;
		} else {
			maxCharacters.disabled = true;
		}
	}

	initAdvanced();

	minCharacters.onblur = checkIntValue;
	maxCharacters.onblur = checkIntValue;

	function checkIntValue() {
		var min = 0;
		var errors = '';
		var num = parseFloat(this.value);
		if (isNaN(num)) {
			errors = "<fmt:message key="js.error.invalid.number"/>\n";
		} else if (num <= min) {
			errors = "<fmt:message key="js.error.min.number"/>";
		}

		if (errors) {
			alert("<fmt:message key="js.error.title"/>\n" + errors);
		}
	}

	function allowNewTopic() {
		var allowNew = document.getElementsByName("forum.allowNewTopic");
		var min = document.getElementById("minimumReply");
		var max = document.getElementById("maximumReply");

		//enable if rate is on
		if (allowNew[0].checked) {
			min.disabled = true;
			max.disabled = true;
		}
		//enable reply limited drop list
		if (allowNew[1].checked) {
			min.disabled = false;
			max.disabled = false;
		}
	}
	allowNewTopic();

	function checkRating() {
		var allowRate = document.getElementsByName("forum.allowRateMessages");
		var min = document.getElementById("minimumRate");
		var max = document.getElementById("maximumRate");

		if (allowRate[0].checked) {
			min.disabled = false;
			max.disabled = false;
		} else {
			min.disabled = true;
			max.disabled = true;
		}
	}
	checkRating();

	function validateRatings(isMinimunRateDropdownUsed) {
		var minRateDropDown = document.getElementById("minimumRate");
		var minRatings = parseInt(minRateDropDown.options[minRateDropDown.selectedIndex].value);
		var maxRateDropDown = document.getElementById("maximumRate");
		var maxRatings = parseInt(maxRateDropDown.options[maxRateDropDown.selectedIndex].value);

		if ((minRatings > maxRatings) && !(maxRatings == 0)) {
			if (isMinimunRateDropdownUsed) {
				minRateDropDown.selectedIndex = maxRateDropDown.selectedIndex;
			} else {
				maxRateDropDown.selectedIndex = minRateDropDown.selectedIndex;
			}

			alert('<fmt:message key="js.error.validate.number"/>');
		}
	}

	function checkReflection() {
		var ropt = document.getElementById("reflectOn");
		var rins = document.getElementById("reflectInstructions");
		if (ropt.checked) {
			rins.disabled = false;
		} else {
			rins.disabled = true;
		}
	}
</script>

