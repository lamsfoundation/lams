<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="imageGallery" value="${sessionMap.imageGallery}"/>
<c:set var="sessionList" value="${sessionMap.exportImageList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

<lams:html>
	<lams:head>
		<title><fmt:message key="export.title" /></title>
		<c:set var="lams">
			<lams:LAMSURL />
		</c:set>
		
		<script type="text/javascript" src="./javascript/jquery.js" ></script>
	 	<script type="text/javascript" src="./javascript/jquery.rating.1.1.js"></script>
	 	<link rel="stylesheet" type="text/css" href="./css/jquery.rating.css"/>
	 	
	 	<script type="text/javascript">
			$(document).ready(function(){
		 		$('input[type=radio].star').rating({
					readOnly: true
				});
			});
		</script>
	
		<lams:css localLinkPath="../"/>
	</lams:head>
	
	<body class="stripes">
	
		<div id="content">
	
			<h1><c:out value="${title}" escapeXml="true"/> </h1>
			
			<p><c:out value="${instructions}" escapeXml="false"/> </p>
	
			<c:choose>
				<c:when test="${not empty sessionList}">
				
					<table border="0" cellspacing="3" width="98%">
						<c:forEach var="imageList" items="${sessionList}">
							<tr>
								<td>
									<c:choose>
										<c:when test="${mode == 'learner'}">
											<%@ include file="exportimagelearner.jsp"%>
										</c:when>
										
										<c:when test="${mode == 'teacher'}">
											<c:if test="${not empty imageList}">
												<%@ include file="exportimageteacher.jsp"%>
											</c:if>
										</c:when>
									</c:choose>								
								</td>
							</tr>
						</c:forEach>
					</table>
					<%-- Display reflection entries --%>
					<c:if test="${sessionMap.reflectOn}">
						<h3>
							<fmt:message key="label.export.reflection" />
						</h3>
						<c:forEach var="reflectDTOList" items="${sessionMap.reflectList}">
							<c:forEach var="reflectDTO" items="${reflectDTOList.value}">
								<h4>
									<c:out value="${reflectDTO.fullName}" escapeHtml="true" />
								</h4>
								<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
							</c:forEach>
						</c:forEach>
					</c:if>					
					
				</c:when>
				<c:otherwise>
				
					<div>
						<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
					</div>
					
				</c:otherwise>
			</c:choose>				
			
		</div>  <!--closes content-->
	
		<div id="footer">
		</div><!--closes footer-->
	
	</body>
</lams:html>
