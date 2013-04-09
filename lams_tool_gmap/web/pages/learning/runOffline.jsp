<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		${gmapDTO.title}
	</h1>

	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post" >
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div align="right" class="space-bottom-top">
				<html:link href="#nogo" styleClass="button" styleId="finishButton" 
				onclick="javascript:document.learningForm.submit();return false">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${activityPosition.last}">
		 						<fmt:message key="button.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="button.finish" />
		 					</c:otherwise>
			 			</c:choose>
			 		</span>
				</html:link>
			</div>
		</html:form>
	</c:if>
</div>

