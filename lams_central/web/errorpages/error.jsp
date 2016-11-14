<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="showErrorStack1"><lams:Configuration key='<%= ConfigurationKeys.ERROR_STACK_TRACE %>'/></c:set>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="heading.general.error" /></title>
	<lams:css />
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}
		function showHide() {
			if (Element.visible("messageDetail")) {
				$("showButt").innerHTML = "<fmt:message key='msg.show.detail'/>";
				Element.hide("messageDetail");
			} else {
				$("showButt").innerHTML = "<fmt:message key='msg.hide.detail'/>";
				Element.show("messageDetail");
			}
		}
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
