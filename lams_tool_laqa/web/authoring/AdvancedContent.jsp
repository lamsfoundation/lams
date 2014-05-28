<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>

<script language="JavaScript" type="text/JavaScript">
	/**
	 * Processes mouse click event on showOtherAnswers ckeckbox
	 */
	function doShowOtherAnswers() {
		document.QaAuthoringForm.usernameVisible.disabled = ! eval(document.QaAuthoringForm.showOtherAnswers.checked); 
		document.QaAuthoringForm.allowRateAnswers.disabled = ! eval(document.QaAuthoringForm.showOtherAnswers.checked);
		if (document.QaAuthoringForm.showOtherAnswers.checked) {
			document.QaAuthoringForm.usernameVisible.checked = true;
		} else {
			document.QaAuthoringForm.usernameVisible.checked = false;
			document.QaAuthoringForm.allowRateAnswers.checked = false;
		}		
	}
	
	function clickSelectLeaderToolOuputHandler() {
		if (document.QaAuthoringForm.useSelectLeaderToolOuput.checked) {
			//uncheck checkboxes
			document.QaAuthoringForm.showOtherAnswers.checked = false;
			document.QaAuthoringForm.usernameVisible.checked = false;
			document.QaAuthoringForm.allowRateAnswers.checked = false;
			//disable checkboxes
			document.QaAuthoringForm.showOtherAnswers.disabled = true;
			document.QaAuthoringForm.usernameVisible.disabled = true;
			document.QaAuthoringForm.allowRateAnswers.disabled = true;
		} else {
			//enable checkboxes
			document.QaAuthoringForm.showOtherAnswers.disabled = false;
		}		
	}
	
</script>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<p>
	<html:checkbox property="useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput" onclick="clickSelectLeaderToolOuputHandler();"
		styleClass="noBorder"/>
	<label for="useSelectLeaderToolOuput">
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="showOtherAnswers" value="1" styleId="showOtherAnswers" onclick="doShowOtherAnswers();"
		styleClass="noBorder" disabled="${formBean.useSelectLeaderToolOuput}"/>
	<label for="showOtherAnswers">
		<fmt:message key="label.learner.answer" />
	</label>
</p>

<p>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<html:checkbox property="usernameVisible" value="1" styleId="usernameVisible" styleClass="noBorder" 
			disabled="${formBean.showOtherAnswers == 0}"/>
	<label for="usernameVisible">
		<fmt:message key="label.show.names" />
	</label>
</p>

<p>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<html:checkbox property="allowRateAnswers" value="1" styleId="allowRateAnswers" styleClass="noBorder" 
			disabled="${formBean.showOtherAnswers == 0}"/>
	<label for="allowRateAnswers">
		<fmt:message key="label.authoring.allow.rate.answers" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="notifyTeachersOnResponseSubmit" value="1" styleClass="noBorder" styleId="notifyTeachersOnResponseSubmit"/>
	<label for="notifyTeachersOnResponseSubmit">
		<fmt:message key="label.notify.teachers.on.response.submit" />
	</label>
</p>

<p>
	<html:checkbox property="reflect" value="1" styleClass="noBorder" styleId="reflect"/>
	<label for="reflect">
		<fmt:message key="label.reflect" />
	</label>
</p>
<p>
	<html:textarea cols="30" rows="3" property="reflectionSubject" styleId="reflectInstructions"></html:textarea>
</p>

<script type="text/javascript">
<!--
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflect");
	function turnOnRefect(){
		if(isEmpty(ra.value)){
		//turn off	
			rao.checked = false;
		}else{
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
//-->
</script>

<p>
	<html:checkbox property="questionsSequenced" value="1" styleId="questionsSequenced"
		styleClass="noBorder"/>
	<label for="questionsSequenced">
		<fmt:message key="radiobox.questionsSequenced" />
	</label>
</p>

<p>
	<html:checkbox property="lockWhenFinished" value="1" styleId="lockWhenFinished"
		styleClass="noBorder"/>
	<label for="lockWhenFinished">
		<fmt:message key="label.lockWhenFinished" />
	</label>
</p>

<p>
	<html:checkbox property="allowRichEditor" value="1" styleId="allowRichEditor"
		styleClass="noBorder"/>
	<label for="allowRichEditor">
		<fmt:message key="label.allowRichEditor" />
	</label>
</p>
