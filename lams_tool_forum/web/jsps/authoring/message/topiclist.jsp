<%@ include file="/common/taglibs.jsp" %>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<div id="topiclist">
	<table class="table table-striped table-condensed">
		<tr>
			<th colspan="4"><fmt:message key="lable.topic.title.subject"/></th>
		</tr>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:forEach items="${sessionMap.topicList}" var="topic" varStatus="status">
			<tr id="row${status.index}">
				<td>
					<c:set var="viewtopic">
						<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&topicIndex=${status.index}&create=${topic.message.updated.time}
					</c:set> 
						
					<c:out value="${topic.message.subject}" />

					<c:if test="${topic.hasAttachment}">
						<i class="fa fa-paperclip loffset5" title="<fmt:message key='message.label.attachment'/>"></i>
					</c:if>
				</td>
			
				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" titleKey="label.authoring.up" onclick="upTopic(${status.index},'${sessionMapID}')"/>
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.authoring.down" onclick="downTopic(${status.index},'${sessionMapID}')"/>
		 			</c:if>
				</td>
				
				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.edit" />" id="edit${status.index}"
					onclick="editTopic(${status.index},'${sessionMapID}')"></i></td>
					
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
					onclick="deleteTopic(${status.index},'${sessionMapID}')"></i></td>
				
			</tr>
		</c:forEach>
	</table>
</div>
<%-- This script will works when a new topic is submitted in order to refresh "Topic List" panel. --%>
<script type="text/javascript">
	hideMessage();
	var obj = document.getElementById('messageListArea');
	obj.innerHTML= document.getElementById("topiclist").innerHTML;
</script>
