<div align="center">
<script type="text/javascript">
	function popupable(url,name){
	if(window.opener != null || (typeof popFobidden != "undefined"))
		//it is already a popup window, then just load it
//		location.href=url;
		launchPopup(url,name);
	else
		//it is not in popup window, so pops it up
		launchPopup(url,name);
	}
</script>
<div id="topiclist">
	<div id="datatablecontainer">
	<table width="100%" align="CENTER" 	class="form">
		<tr>
			<td valign="MIDDLE"><b>Topic</b></td>
			<td colspan="3" />
		</tr>
		<tr>
			<th scope="col" width="50%" colspan="2"><fmt:message key="lable.topic.title.subject"/></th>
			<th scope="col" width="25%"><fmt:message key="lable.topic.title.startedby"/></th>
			<th scope="col" width="25%"><fmt:message key="lable.topic.title.update"/></th>
		</tr>
		<c:forEach items="${topicList}" var="topic" varStatus="status">
			<tr>
				<td valign="MIDDLE" width="48%">
					<c:set var="viewtopic">
						<html:rewrite page="/authoring/viewTopic.do?topicIndex=${status.index}&create=${topic.message.created.time}" />
					</c:set> 
					<html:link href="javascript:popupable('${viewtopic}','topiclist')">
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
					<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
					<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
				</td>
			</tr>
		</c:forEach>
	</table>
	</div>
</div>
</div>
