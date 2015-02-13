<%@ include file="/common/taglibs.jsp" %>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<div id="topiclist">
	<table class="alternative-color" cellspacing="0">
		<tr>
			<th colspan="4"><fmt:message key="lable.topic.title.subject"/></th>
		</tr>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:forEach items="${sessionMap.topicList}" var="topic" varStatus="status">
			<tr id="row${status.index}">
				<td>
					<c:set var="viewtopic">
						<html:rewrite page="/authoring/viewTopic.do?sessionMapID=${sessionMapID}&topicIndex=${status.index}&create=${topic.message.updated.time}" />
					</c:set> 
						
					<c:out value="${topic.message.subject}" />

					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif" />">
					</c:if>
				</td>
				
				<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upTopic(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.up"/>">
						</c:if>

						<img src="<html:rewrite page='/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.down"/>"
							onclick="downTopic(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px">
					<img src="${tool}images/edit.gif"
						title="<fmt:message key="label.edit" />" id="edit${status.index}" 
						onclick="editTopic(${status.index},'${sessionMapID}')" />
                </td>
                
				<td width="20px">
					<img src="${tool}images/delete.gif"
						title="<fmt:message key="label.delete" />" id="delete${status.index}" 
						onclick="deleteTopic(${status.index},'${sessionMapID}')" />
				</td>				
			</tr>
		</c:forEach>
	</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('messageListArea');
		obj.innerHTML= document.getElementById("topiclist").innerHTML;
	}
</script>
