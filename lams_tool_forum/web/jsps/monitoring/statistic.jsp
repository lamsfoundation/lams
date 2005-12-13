<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>

<div class="datatablecontainer">
<c:forEach var="element" items="${topicList}">
	<c:set var="toolSessionID" value="${element.key}"/>
	<c:set var="sessionTopicList" value="${element.value}"/>
	<c:forEach var="totalMsg" items="${totalMessage}">
		<c:if test="${totalMsg.key eq toolSessionID}">
			<c:set var="sessionTotalMessage" value="${totalMsg.value}"/>
		</c:if>
	</c:forEach>
	<c:forEach var="avaMark" items="${markAverage}">
		<c:if test="${avaMark.key eq toolSessionID}">
			<c:set var="sessionMarkAverage" value="${avaMark.value}"/>
		</c:if>
	</c:forEach>
	
		Session ID: <c:out value="${toolSessionID}"/>
	
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	
	  <tr>
		  <td colspan="2">
			<div id="datatablecontainer">
			<table width="100%" align="CENTER" 	class="form">
				<tr>
					<th scope="col" width="50%"><fmt:message key="lable.topic.title.subject"/></th>
					<th scope="col" width="25%"><fmt:message key="lable.topic.title.message.number"/></th>
					<th scope="col" width="25%"><fmt:message key="lable.topic.title.average.mark"/></th>
				</tr>
				<c:forEach items="${sessionTopicList}" var="topic" >
					<tr>
						<td valign="MIDDLE" width="48%">
							<c:set var="viewtopic">
								<html:rewrite page="/monitoring/viewTopic.do?messageID=${topic.message.uid}&create=${topic.message.created.time}" />
							</c:set> 
							<html:link href="javascript:launchPopup('${viewtopic}');">
								<c:out value="${topic.message.subject}" />
							</html:link>
						</td>
						<td>
							<c:out value="${topic.message.replyNumber+1}"/>
						</td>
						<td>
							<c:out value="${topic.mark}"/>
						</td>
					</tr>
				</c:forEach>
			</table>
			</div>  
		  </td>
	  </tr>
	
	  <tr>
	    <td><br><fmt:message key="lable.monitoring.statistic.total.message"/></td>
	    <td><br><c:out value="${sessionTotalMessage}"/></td>
	  </tr>  
	  <tr>
	    <td><fmt:message key="label.monitoring.statistic.average.mark"/></td>
	    <td><c:out value="${sessionMarkAverage}"/></td>
	  </tr>
	</table>
</c:forEach>
</div>						
