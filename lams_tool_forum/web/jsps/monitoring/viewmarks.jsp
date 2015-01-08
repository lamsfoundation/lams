<%@ include file="/common/taglibs.jsp"%>
<style media="screen,projection" type="text/css">
	.field-name {
		padding-bottom: 0px;
	}
</style>
<script type="text/javascript">
	function closeAndRefreshParentMonitoringWindow() {
		refreshParentMonitoringWindow();
		window.close();
	}
</script>

<p>
	<c:out value="${user.lastName}" /> <c:out value="${user.firstName}" />
						
	<fmt:message key="monitoring.user.post.topic" />
</p>


<table cellpadding="0">
	
	<c:forEach items="${messages}" var="topic">
		<tr>
			<td colspan="2">
				<c:set var="viewtopic">
					<html:rewrite page="/monitoring/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
				</c:set>
				<html:link href="javascript:launchPopup('${viewtopic}','viewtopic');">
					<c:out value="${topic.message.subject}" />
				</html:link>
					
				<c:if test="${topic.hasAttachment}">
					<img src="<html:rewrite page="/images/paperclip.gif"/>">
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<lams:Date  value="${topic.message.updated}"/>
			</td>
		</tr>
			
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="label.number.of.replies" />
				:
			</td>
			<td colspan="3">
				${topic.message.replyNumber}
			</td>
		</tr>
	
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="lable.topic.title.mark" />
				:
			</td>
			<td>
				<c:choose>
					<c:when test="${empty topic.message.report.mark}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<fmt:formatNumber value="${topic.message.report.mark}"  maxFractionDigits="2"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
			
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="lable.topic.title.comment" />
				:
			</td>
			<td>
				<c:choose>
					<c:when test="${empty topic.message.report.comment}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<c:out value="${topic.message.report.comment}" escapeXml="false" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
			
		<tr>
			<td>
			</td>
			<td style="padding:10px 0px 20px;">
				<html:form action="/monitoring/editMark" method="post">
					<html:hidden property="sessionMapID" value="${sessionMapID}"/>
					<html:hidden property="topicID" value="${topic.message.uid}"/>
					<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="button" />
				</html:form>
			</td>
		</tr>
			
	</c:forEach>
</table>

<div style="padding:0 15px 25px;">
	<a href="javascript:closeAndRefreshParentMonitoringWindow()" class="button float-right">
		<fmt:message key="button.close"/>
	</a>
</div>
