<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>

<div class="datatablecontainer">
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
			<c:forEach items="${topicList}" var="topic" >
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
    <td><br><c:out value="${totalMessage}"/></td>
  </tr>  
  <tr>
    <td><fmt:message key="label.monitoring.statistic.average.mark"/></td>
    <td><c:out value="${markAverage}"/></td>
  </tr>
</table>
</div>						
