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

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="640px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}
	
	function removeQuestion(questionIndex)
	{
		document.McAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}

	function removeMonitoringQuestion(questionIndex)
	{
		document.McMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeQuestion');
	}

</script>

<html:hidden property="questionIndex" />
<table cellpadding="0">
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.title.col"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.instructions.col"></fmt:message>
			</div>
			<lams:FCKEditor id="instructions"
				value="${mcGeneralAuthoringDTO.activityInstructions}"
				contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
</table>

<div id="resourceListArea">
	<c:if
		test="${mcGeneralAuthoringDTO.activeModule == 'authoring' || mcGeneralAuthoringDTO.activeModule == 'defineLater'}">
		<%@ include file="/authoring/itemlist.jsp"%>
	</c:if>
	<c:if
		test="${mcGeneralAuthoringDTO.activeModule != 'authoring' && mcGeneralAuthoringDTO.activeModule != 'defineLater'}">
		<%@ include file="/monitoring/itemlist.jsp"%>
	</c:if>
</div>

<p>
	<c:if
		test="${mcGeneralAuthoringDTO.activeModule == 'authoring' || mcGeneralAuthoringDTO.activeModule == 'defineLater'}">
		<a
			href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newQuestionBox&requestType=direct&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&activeModule=${mcGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${mcGeneralAuthoringDTO.defaultContentIdStr}&sln=${mcGeneralAuthoringDTO.sln}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}"/>');"
			class="button-add-item"> <fmt:message
				key="label.add.new.question" /> </a>
	</c:if>
	<c:if
		test="${mcGeneralAuthoringDTO.activeModule != 'authoring' && mcGeneralAuthoringDTO.activeModule != 'defineLater'}">
		<a
			href="javascript:showMessage('<html:rewrite page="/monitoring.do?dispatch=newQuestionBox&requestType=direct&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&activeModule=${mcGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${mcGeneralAuthoringDTO.defaultContentIdStr}"/>');"
			class="button-add-item"> <fmt:message
				key="label.add.new.question" /> </a>

	</c:if>
</p>

<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="messageArea" name="messageArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>

<c:if test="${mcGeneralAuthoringDTO.activeModule != 'authoring'}">
	<p align="right">
		<a href="javascript:submitMethod('submitAllContent')" class="button">
			<fmt:message key="label.save" /> </a>
	</p>
</c:if>
