<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="showErrorStack1"><lams:Configuration key='<%= ConfigurationKeys.ERROR_STACK_TRACE %>'/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="heading.general.error" /></title>
	<lams:css />
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}
		<c:if test="${showErrorStack1}">
			function showHide() {
				var messageDetail = document.getElementById('messageDetail'),
					showButt = document.getElementById('showButt');
				if (messageDetail.style.display == 'none') {
					showButt.innerHTML = "<fmt:message key='msg.hide.detail'/>";
					messageDetail.style.display = 'inline';

				} else {
					showButt.innerHTML = "<fmt:message key='msg.show.detail'/>";
					messageDetail.style.display = 'none';
				}
			}
		</c:if>
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="error.general.1" />
	</c:set>
	<lams:Page type="admin" title="${title}">
		<%-- Error Messages --%>

		<h4>
			<fmt:message key="error.general.2" />
		</h4>

		<div>
			<fmt:message key="error.general.3" />
		</div>

		<div class="voffset10">
		<c:if test="${showErrorStack1}">
			<lams:Alert id="errors" type="danger" close="false">
			
				<c:if test="${not empty param.errorName}">

					<c:out value="${param.errorName}" escapeXml="true" />:
					<c:out value="${param.errorMessage}" escapeXml="true" />



					<div id="showButton" class="voffset5">
						<a href="#" onclick="showHide()" class="btn btn-sm btn-primary"><span id="showButt"><fmt:message
									key='msg.show.detail' /> </span> </a>
					</div>


					<span id="messageDetail" style="display: none"> <c:if test="${not empty param.errorStack}">
								<c:out value="${param.errorStack}" escapeXml="true" />
							</c:if> <c:if test="${empty param.errorStack}">
							<fmt:message key="msg.no.more.detail" />
						</c:if>
					</span>
					</c:if>
					
		
			</lams:Alert>
			</c:if>
		</div>
		

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>