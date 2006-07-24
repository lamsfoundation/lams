<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="heading.general.error" /></title>
		<!-- ********************  CSS ********************** -->
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
	</head>

	<body>
		<div id="page-learner">
			<!--main box 'page'-->

			<h1 class="no-tabs-below">
				<fmt:message key="error.general.1" />
			</h1>
			<div id="header-no-tabs-learner"></div>
			<!--closes header-->

			<div id="content-learner">
				<%-- Error Messages --%>
				<h2>
					<fmt:message key="error.general.2" />
					<fmt:message key="error.general.3" />
				</h2>
				<P></P>
				<p class="warning">
					<c:if test="${not empty param.errorName}">

						<c:out value="${param.errorName}" escapeXml="false" />:
						<c:out value="${param.errorMessage}" escapeXml="false" />
						</BR>
						<a href="#" onclick="showHide()"><span id="showButt"><fmt:message key='msg.show.detail'/></span></a>
						</BR>
						<span id="messageDetail" style="display:none"> 
						<c:if test="${not empty param.errorStack}">
							<c:out value="${param.errorStack}" escapeXml="false" />
						</c:if>
						<c:if test="${empty param.errorStack}">
							<fmt:message key="msg.no.more.detail" />
						</c:if>
						 </span>
					</c:if>
				</p>
				<!--
				<div class="right-buttons">
					<a href="javascript:;" onclick="closeWin();" class="button"> <fmt:message
							key="button.close" /> </a>
				</div>
				-->
			</div>
			<!--closes content-->

			<div id="footer-learner"></div>
			<!--closes footer-->
		</div>
		<!--closes page-->
	</body>
</html>
