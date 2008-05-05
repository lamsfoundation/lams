<%@ include file="/common/taglibs.jsp" %>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<div id="topiclist">
	<table class="alternative-color" cellspacing="0">
		<tr>
			<th scope="col" width="100%"><fmt:message key="lable.topic.title.subject"/></th>
		</tr>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:forEach items="${sessionMap.topicList}" var="topic" varStatus="status">
			<tr>
				<td>
					<c:set var="viewtopic">
						<html:rewrite page="/authoring/viewTopic.do?sessionMapID=${sessionMapID}&topicIndex=${status.index}&create=${topic.message.updated.time}" />
					</c:set> 
					<html:link href="javascript:showMessage('${viewtopic}')">
						<c:out value="${topic.message.subject}" />
					</html:link>
					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif" />">
					</c:if>
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