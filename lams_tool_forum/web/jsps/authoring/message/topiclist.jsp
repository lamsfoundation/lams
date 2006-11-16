<%@ include file="/common/taglibs.jsp" %>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<div id="topiclist">
	<table class="alternative-color" cellspacing="0">
		<tr>
			<th scope="col" width="50%"><fmt:message key="lable.topic.title.subject"/></th>
			<th scope="col" width="20%"><fmt:message key="lable.topic.title.startedby"/></th>
			<th scope="col" width="30%"><fmt:message key="lable.topic.title.update"/></th>
		</tr>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:forEach items="${sessionMap.topicList}" var="topic" varStatus="status">
			<tr>
				<td>
					
					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif" />" class="space-right float-right">
					</c:if>
					
					<c:set var="viewtopic">
						<html:rewrite page="/authoring/viewTopic.do?sessionMapID=${sessionMapID}&topicIndex=${status.index}&create=${topic.message.updated.time}" />
					</c:set> 
					<html:link href="javascript:showMessage('${viewtopic}')">
						<c:out value="${topic.message.subject}" />
					</html:link>
				
					
				</td>
				<td>
					<c:set var="author" value="${topic.author}"/>
					<c:if test="${empty author}">
						<c:set var="author">
							<fmt:message key="label.default.user.name"/>
						</c:set>
					</c:if>
					${author}
				</td>
				<td>
					<lams:Date value="${topic.message.updated}"/>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
 
	if(window.top != null){
		window.top.hideMessage();
		var obj = window.top.document.getElementById('messageListArea');
		obj.innerHTML= document.getElementById("topiclist").innerHTML;
	}
</script>