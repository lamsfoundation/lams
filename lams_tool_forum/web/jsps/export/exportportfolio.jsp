<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>Learner Topics Details</title>
    <html:base/>
    <c:set var="lams"><lams:LAMSURL/></c:set>
  	<link href="<c:out value="${lams}"/>css/aqua.css" rel="stylesheet" type="text/css">
  </head>  
  <body>
  
	<table width="100%"  border="0" cellspacing="3" cellpadding="3">
	<c:forEach items="${report}"  var ="userList" >		
		<c:set var="user" value="${userList.key}"/>
		<c:set var="markList" value="${userList.value}"/>
		<c:set var="first" value="true"/>
		<c:forEach items="${markList}"  var ="topic" >			
			<span><p>			
				<c:if test="${first}">
					<c:set var="first" value="false"/>
				    <tr>
				    	<td colspan="2">
				    	<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> ,
				    	<c:out value="${user.loginName}"/>,
				    	 provides following submisstion:
				    	</td>
				    <tr>
			    </c:if>
			<tr>
				<td valign="MIDDLE" width="48%">
					<c:set var="viewtopic">
						<html:rewrite page="/monitoring/viewTopic.do?messageID=${topic.message.uid}&create=${topic.message.created.time}" />
					</c:set> 
					<html:link href="javascript:launchPopup('${viewtopic}');">
						<c:out value="${topic.message.subject}" />
					</html:link>
				</td>
				<td width="2%">
					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif"/>">
					</c:if>
				</td>
				<td>
					<c:out value="${topic.author}"/>
				</td>
				<td>
					<c:out value="${topic.message.replyNumber}"/>
				</td>
				<td>
					<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
					<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
				</td>
			</tr>
		 <tr>
			<tr>
				<td>Marks:</td>
				<td colspan="3"> 	
						<c:choose>
									<c:when test="${empty topic.message.report.mark}">
										<c:out value="Not Available"/>
									</c:when>
									<c:otherwise>
										<c:out value="${topic.message.report.mark}" escapeXml="false"/>
									</c:otherwise>
								</c:choose>
				</td>
			</tr>			
			<tr>
				<td>Comments:</td>
				<td  colspan="3">
								<c:choose>
									<c:when test="${empty topic.message.report.comment}">
										<c:out value="Not Available"/>								
									</c:when>
									<c:otherwise>
										<c:out value="${topic.message.report.comment}" escapeXml="false"/>
									</c:otherwise>
								</c:choose>
				</td>
			</tr>
			</span>
		</c:forEach>
	</c:forEach>
	</table>						
</body>
</html:html>