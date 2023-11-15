<%@ include file="/common/taglibs.jsp"%>

<!--   Advance Tab Content    -->



<lams:SimplePanel titleKey="label.posting.options">

	<div class="checkbox">
		<label for="rich-editor"><form:checkbox
				path="forum.allowRichEditor" id="rich-editor" /> <fmt:message
				key="label.authoring.advance.use.richeditor" /></label>
	</div>

	<div class="checkbox">
		<label for="allow-upload"><form:checkbox
				path="forum.allowUpload" id="allow-upload" /> <fmt:message
				key="label.authoring.advance.allow.upload" /></label>
	</div>

	<div class="checkbox">
		<label for="allowEdit"><form:checkbox
				path="forum.allowEdit" id="allowEdit" /> <fmt:message
				key="label.authoring.advance.allow.edit" /></label>
	</div>

	<div class="checkbox">
		<label for="allow-anon"><form:checkbox
				path="forum.allowAnonym" id="allowAnonym" /> <fmt:message
				key="label.authoring.advance.enable.anonymous.posts" /></label>
	</div>

	<div class="checkbox">
		<label for="limited-min-characters"><form:checkbox
				path="forum.limitedMinCharacters"
				id="limited-min-characters" /> <fmt:message
				key="label.authoring.advance.min.limited.input" /> </label> <input
			type="number" name="forum.minCharacters" id="min-characters"
			onchange="validatePostings(true);"
			value="${forumForm.forum.minCharacters}"
			class="form-control form-control-inline input-sm" min="0" />
	</div>

	<div class="checkbox">
		<label for="limited-max-characters"><form:checkbox
				path="forum.limitedMaxCharacters"
				id="limited-max-characters" /> <fmt:message
				key="label.authoring.advance.limited.input" /></label> <input type="number"
			name="forum.maxCharacters" id="max-characters"
			onchange="validatePostings(true);"
			value="${forumForm.forum.maxCharacters}"
			class="form-control form-control-inline input-sm" max="5000" />
	</div>

	<lams:SimplePanel panelBodyClass="panel-body-sm">
		<div class="checkbox">
			<label for="allowRateMessages"><form:checkbox
					path="forum.allowRateMessages" id="allowRateMessages"
					onclick="checkRating()" /> <fmt:message
					key="label.authoring.advance.allow.rate.postings" /></label>
		</div>

		<div class="form-inline loffset20">
			<div class="form-group">
				<label for="minimumRate"><fmt:message
						key="label.authoring.advance.minimum.reply" /></label> <input
					type="number" name="forum.minimumRate" id="minimumRate"
					value="${forumForm.forum.minimumRate}"
					onchange="validateRatings(true);" class="form-control input-sm"
					min="0" />
			</div>
			<div class="form-group">
				<label for="maximumRate"><fmt:message
						key="label.authoring.advance.maximum.reply" /></label> <input
					type="number" name="forum.maximumRate" id="maximumRate"
					value="${forumForm.forum.maximumRate}"
					onchange="validateRatings(true);" class="form-control input-sm"
					min="0" />
			</div>
		</div>
	</lams:SimplePanel>

	<lams:SimplePanel panelBodyClass="panel-body-sm">
		<fmt:message key="message.posting.limiting" />
		<div class="radio">
			<label for="allowNewTopic1"> <form:radiobutton
					path="forum.allowNewTopic" value="true"
					onclick="allowNewTopic()" id="allowNewTopic1" /> <fmt:message
					key="label.authoring.advance.allow.new.topics" />
			</label>
		</div>

		<div class="radio">
			<label for="allowNewTopic2"> <form:radiobutton
					path="forum.allowNewTopic" value="false"
					onclick="allowNewTopic()" id="allowNewTopic2" /> <fmt:message
					key="label.authoring.advance.number.reply" />
			</label>
		</div>

		<div class="form-inline loffset20">
			<div class="form-group">
				<label for="minimumReply"><fmt:message
						key="label.authoring.advance.minimum.reply" /></label> <input
					type="number" name="forum.minimumReply" id="minimumReply"
					value="${forumForm.forum.minimumReply}"
					onchange="validateReply(true);" class="form-control input-sm"
					min="0" />
			</div>
			<div class="form-group">
				<label for="maximumReply"><fmt:message
						key="label.authoring.advance.maximum.reply" /></label> <input
					type="number" name="forum.maximumReply" id="maximumReply"
					value="${forumForm.forum.maximumReply}"
					onchange="validateReply(true);" class="form-control input-sm"
					min="0" />
			</div>
		</div>
	</lams:SimplePanel>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
	<div class="form-group">
		<label><fmt:message
				key="label.authoring.advanced.send.emails.to" /></label>
		<div class="form-inline">
			<div class="checkbox loffset20">
				<label for="notifyLearnersOnForumPosting"><form:checkbox
						path="forum.notifyLearnersOnForumPosting"
						id="notifyLearnersOnForumPosting" />&nbsp; <fmt:message
						key="label.authoring.advanced.learners" /></label>
			</div>
			<div class="checkbox loffset10">
				<label for="notifyTeachersOnForumPosting"><form:checkbox
						path="forum.notifyTeachersOnForumPosting"
						id="notifyTeachersOnForumPosting" />&nbsp; <fmt:message
						key="label.authoring.advanced.teachers" /></label>
			</div>
		</div>
	</div>

	<div class="checkbox">
		<label for="notifyLearnersOnMarkRelease"><form:checkbox
				path="forum.notifyLearnersOnMarkRelease"
				id="notifyLearnersOnMarkRelease" /> <fmt:message
				key="label.authoring.advanced.notify.mark.release" /></label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${forumForm.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished"><form:checkbox
				path="forum.lockWhenFinished" id="lockWhenFinished" /> <fmt:message
				key="label.authoring.advance.lock.on.finished" /></label>
	</div>

	<div class="checkbox">
		<label for="reflectOn"><form:checkbox
				path="forum.reflectOnActivity" id="reflectOn" /> <fmt:message
				key="advanced.reflectOnActivity" /></label>
	</div>
	<div class="form-group">
		<textarea name="forum.reflectInstructions"
			id="reflectInstructions" class="form-control" rows="3"
			onkeyup="javascript:turnOnReflect()" >${forumForm.forum.reflectInstructions}</textarea>
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
	var limitedMinCharacters = document
			.getElementById("limited-min-characters");
	var minCharacters = document.getElementById("min-characters");
	var limitedMaxCharacters = document
			.getElementById("limited-max-characters");
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
			errors = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='js.error.invalid.number'/></spring:escapeBody>\n";
		} else if (num <= min) {
			errors = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='js.error.min.number'/></spring:escapeBody>";
		}

		if (errors) {
			alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='js.error.title'/></spring:escapeBody>\n" + errors);
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

	function validatePostings(isMinimunPostingDropdownUsed) {
		var minCharacters = document.getElementById("min-characters");
		var minPostings = parseInt(minCharacters.value);
		var maxCharacters = document.getElementById("max-characters");
		var maxPostings = parseInt(maxCharacters.value);
		if ((minPostings > maxPostings) && !(maxPostings == 0)) {
			if (isMinimunPostingDropdownUsed) {
				minCharacters.value = maxCharacters.value;
			} else {
				maxCharacters.value = minCharacters.value;
			}

			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="js.error.validate.posting.number"/></spring:escapeBody>');
		}
	}

	function validateReply(isMinimunReplyDropdownUsed) {
		var minReplyDropDown = document.getElementById("minimumReply");
		var minReplies = parseInt(minReplyDropDown.value);
		var maxReplyDropDown = document.getElementById("maximumReply");
		var maxReplies = parseInt(maxReplyDropDown.value);
		if ((minReplies > maxReplies) && !(maxReplies == 0)) {
			if (isMinimunReplyDropdownUsed) {
				minReplyDropDown.value = maxReplyDropDown.value;
			} else {
				maxReplyDropDown.value = minReplyDropDown.value;
			}

			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="js.error.validate.reply.number"/></spring:escapeBody>');
		}
	}

	function validateRatings(isMinimunRateDropdownUsed) {
		var minRateDropDown = document.getElementById("minimumRate");
		var minRatings = parseInt(minRateDropDown.value);
		var maxRateDropDown = document.getElementById("maximumRate");
		var maxRatings = parseInt(maxRateDropDown.value);
		if ((minRatings > maxRatings) && !(maxRatings == 0)) {
			if (isMinimunRateDropdownUsed) {
				minRateDropDown.value = maxRateDropDown.value;
			} else {
				maxRateDropDown.value = minRateDropDown.value;
			}

			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="js.error.validate.number"/></spring:escapeBody>');
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

