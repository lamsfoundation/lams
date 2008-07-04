<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>

<script lang="javascript">
	function showMessage(url) {
		var area=document.getElementById("resourceInputArea");
		if(area != null){
			area.style.width="650px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "resourceInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("resourceInputArea");
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
	
	function editItem(idx, sessionMapID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
</script>


<%-- Overall TaskList information  --%>

<div class="info space-bottom" align="right" style="position:relative; right:40px; top:15px;">
	<fmt:message key="label.monitoring.summary.lock.when.finished" />
	<c:choose>
		<c:when test="${spreadsheet.lockWhenFinished}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>
	 						
	<fmt:message key="label.monitoring.summary.individual.spreadsheets" /> 
	<c:choose>
		<c:when test="${spreadsheet.learnerAllowedToSave}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>	
						
	<fmt:message key="label.monitoring.summary.marking.enabled" />
	<c:choose>
		<c:when test="${spreadsheet.markingEnabled}"> 
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>	
						
	<fmt:message key="label.monitoring.summary.notebook.reflection" />
	<c:choose>
		<c:when test="${spreadsheet.reflectOnActivity}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>
</div>

<%-- Summary list  --%>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:forEach var="summary" items="${summaryList}" varStatus="firstGroup">
	<h1><fmt:message key="monitoring.label.group" /> ${summary.sessionName}	</h1>
	<h2 style="color:black; margin-left: 20px;"><fmt:message key="label.monitoring.summary.overall.summary" />	</h2>
	<table cellpadding="0" class="alternative-color" >

		<tr>
			<th width="60%" align="left">
				<fmt:message key="label.monitoring.summary.learner" />
			</th>
			<c:if test="${spreadsheet.markingEnabled}">			
				<th width="40px" align="center">
					<fmt:message key="label.monitoring.summary.marked" />
				</th>
				<th width="20px" align="left">
				</th>
			</c:if>
		</tr>

		<c:forEach var="user" items="${summary.users}" varStatus="userStatus">
			<tr>
				<td>
					<c:choose>
						<c:when test="${spreadsheet.learnerAllowedToSave}">
							<c:set var="reviewItem">
								<c:url value="/reviewItem.do?userUid=${user.uid}"/>
							</c:set>
							<html:link href="javascript:launchPopup('${reviewItem}')">
								${user.loginName}
							</html:link>
						</c:when>
						<c:otherwise>
							${user.loginName}
						</c:otherwise>
					</c:choose>					
				</td>
				
				<c:if test="${spreadsheet.markingEnabled}">					
					<td align="center">
						<c:choose>
							<c:when test="${(user.userEditedSpreadsheet != null) && (user.userEditedSpreadsheet.mark != null)}">
								<img src="<lams:LAMSURL/>/images/tick.gif" alt="tick" border="0"/>
							</c:when>
								
							<c:otherwise>
								<img src="<lams:LAMSURL/>/images/cross.gif" alt="cross" border="0"/>
							</c:otherwise>
						</c:choose>
					</td>
					
					<td align="left">
						<c:set var="url2" value="<html:rewrite page='/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}'/>" />
						<html:link
							href="javascript:showMessage(url2);"
							property="Mark" styleClass="button">
							<fmt:message key="label.monitoring.summary.mark.button" />
						</html:link>
					</td>
				</c:if>				
			</tr>
		</c:forEach>
		
	</table>
	<c:if test="${spreadsheet.markingEnabled}">	
		<div style="position:relative; left:30px; ">
			<html:link href="javascript:viewAllMarks(${sessionDto.sessionID});"
				property="viewAllMarks" styleClass="button">
				<fmt:message key="label.monitoring.summary.viewAllMarks.button" />
			</html:link>
			<html:link href="javascript:releaseMarks(${sessionDto.sessionID});"
				property="releaseMarks" styleClass="button">
				<fmt:message key="label.monitoring.summary.releaseMarks.button" />
			</html:link>
			<html:link href="javascript:downloadMarks(${sessionDto.sessionID});"
				property="downloadMarks" styleClass="button">
				<fmt:message key="label.monitoring.summary.downloadMarks.button" />
			</html:link>
		</div>
	</c:if>
	
	<%-- Reflection list  --%>
	
	<c:if test="${sessionMap.spreadsheet.reflectOnActivity}">
	
		<h2 style="color:black; margin-left: 20px; " ><fmt:message key="label.monitoring.summary.title.reflection"/>	</h2>
		<table cellpadding="0"  class="alternative-color"  >

			<tr>
				<th>
					<fmt:message key="label.monitoring.summary.user"/>
				</th>
				<th>
					<fmt:message key="label.monitoring.summary.reflection"/>
				</th>
			</tr>				
						
			<c:forEach var="user" items="${summary.users}">
				<tr>
					<td>
						${user.loginName}
					</td>
					<td >
						<c:set var="viewReflection">
							<c:url value="/monitoring/viewReflection.do?userUid=${user.uid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
					</td>
				</tr>
			</c:forEach>
						
		</table>
	</c:if>
	<br>
	
	
</c:forEach>


<c:if test="${spreadsheet.markingEnabled}">	

	
	<p>
		<iframe
			onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
			id="marksInputArea" name="marksInputArea"
			style="width:0px;height:0px;border:0px;display:none" frameborder="no"
			scrolling="no">
		</iframe>
	</p>
</c:if>
