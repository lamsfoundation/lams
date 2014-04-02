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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="100%";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "messageArea";		
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

	function removeNomination(questionIndex)
	{
		document.VoteAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeNomination');
	}

	function removeMonitoringNomination(questionIndex)
	{
		document.VoteMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeNomination');
	}
	
	
	function onSelectDataInput(){
	if (document.getElementById("dataFlowNoneOption")!=null){
			if (noneDataFlowSelectedPreviously && !document.getElementById("dataFlowNoneOption").selected){
					document.getElementById("maxInputs").disabled=false;		
						
			}
			else if (!noneDataFlowSelectedPreviously && document.getElementById("dataFlowNoneOption").selected){
					document.getElementById("dataFlowLimitNoneOption").selected=true;
					document.getElementById("maxInputs").disabled=true;		
			}
			noneDataFlowSelectedPreviously = document.getElementById("dataFlowNoneOption").selected;
		}
	}

	function resizeOnMessageFrameLoad(){
		var messageAreaFrame = document.getElementById("messageArea");
		messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
	}

</script>

<html:hidden property="questionIndex" />

<table cellpadding="0">

	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.title.col"></fmt:message>
			</div>
			<html:text property="title" style="width: 100%;"></html:text>
		</td>
	</tr>


	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.instructions.col"></fmt:message>
			</div>
			<lams:CKEditor id="instructions"
				value="${voteGeneralAuthoringDTO.activityInstructions}"
				contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
</table>

<div id="resourceListArea">
	<c:if
		test="${voteGeneralAuthoringDTO.activeModule == 'authoring' || voteGeneralAuthoringDTO.activeModule == 'defineLater'}">
		<%@ include file="/authoring/itemlist.jsp"%>
	</c:if>
	<c:if
		test="${voteGeneralAuthoringDTO.activeModule != 'authoring' && voteGeneralAuthoringDTO.activeModule != 'defineLater'}">
		<%@ include file="/monitoring/itemlist.jsp"%>
	</c:if>
</div>

<p>
	<c:if
		test="${voteGeneralAuthoringDTO.activeModule == 'authoring' || voteGeneralAuthoringDTO.activeModule == 'defineLater'}">
		<a
			href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newNominationBox&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&activeModule=${voteGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${voteGeneralAuthoringDTO.defaultContentIdStr}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}"/>');"
			class="button-add-item"> <fmt:message
				key="label.add.new.nomination" /> </a>
	</c:if>
	<c:if
		test="${voteGeneralAuthoringDTO.activeModule != 'authoring' && voteGeneralAuthoringDTO.activeModule != 'defineLater'}">
		<a
			href="javascript:showMessage('<html:rewrite page="/monitoring.do?dispatch=newNominationBox&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&activeModule=${voteGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${voteGeneralAuthoringDTO.defaultContentIdStr}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}}"/>');"
			class="button-add-item"> <fmt:message
				key="label.add.new.nomination" /> </a>
	</c:if>
	
	<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
		<span style="margin-left: 20px;"><fmt:message key="label.data.flow.choose" /></span>
		<html:select property="assignedDataFlowObject" onchange="javascript:onSelectDataInput();">
			<html:option value="0" styleId="dataFlowNoneOption"><fmt:message key="label.data.flow.none" /></html:option>
			<c:forEach items="${voteGeneralAuthoringDTO.dataFlowObjectNames}" var="dataFlowObject" varStatus="status">
				<html:option value="${status.index+1}">${dataFlowObject}</html:option>
			</c:forEach>
		</html:select>
	</c:if>
</p>

<p>
	<iframe
		onload="javascript:resizeOnMessageFrameLoad()"
		id="messageArea" name="messageArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>

<c:if test="${voteGeneralAuthoringDTO.activeModule != 'authoring'}">
	<p class="align-right">
		<a href="javascript:submitMethod('submitAllContent')" class="button">
			<fmt:message key="label.save" /> </a>
	</p>
</c:if>
