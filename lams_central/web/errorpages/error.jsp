<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
	<lams:head>
		<title><fmt:message key="heading.general.error" />
		</title>
		<lams:css />
		<script type="text/javascript"
			src="${lams}includes/javascript/prototype.js"></script>

		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
			function showHide(){
				if(Element.visible("messageDetail")){
					$("showButt").innerHTML = "<fmt:message key='msg.show.detail'/>";
					Element.hide("messageDetail");
				}else{
					$("showButt").innerHTML = "<fmt:message key='msg.hide.detail'/>";
					Element.show("messageDetail");
				}
			}
		</script>
	</lams:head>

	<body class="stripes">

		<div id="content">

			<h1>
				<fmt:message key="error.general.1" />
			</h1>

			<%-- Error Messages --%>

			<p>
				<fmt:message key="error.general.2" />
				<fmt:message key="error.general.3" />
			</p>

			<div class="warning space-bottom">
				<c:if test="${not empty param.errorName}">

					<c:out value="${param.errorName}" escapeXml="true" />:
						<c:out value="${param.errorMessage}" escapeXml="true" />
					
					<a href="#" onclick="showHide()"><span id="showButt"><fmt:message
								key='msg.show.detail' />
					</span>
					</a>
					
					<br>
					
					<span id="messageDetail" style="display:none"> <c:if
							test="${not empty param.errorStack}">
							<c:out value="${param.errorStack}" escapeXml="true" />
						</c:if> <c:if test="${empty param.errorStack}">
							<fmt:message key="msg.no.more.detail" />
						</c:if> </span>
				</c:if>
			</div>

		</div>
		<!--closes content-->

		<div id="footer"></div>
		<!--closes footer-->

	</body>
</lams:html>
