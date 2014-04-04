<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
	
<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="ToolContentTopicList" value="${sessionMap.ToolContentTopicList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

<lams:html>

<lams:head>
	<lams:css localLinkPath="../"/>
	<link rel="stylesheet" href="./css/jquery.jRating.css"  type="text/css" />

	<script type="text/javascript"> 
		var pathToImageFolder = "./images/css/";
	</script>
	<script type="text/javascript" src="./javascript/jquery.js"></script>
	<script type="text/javascript" src="./javascript/jquery.jRating.js"></script>
	<script language="JavaScript" type="text/JavaScript">
	  	$(document).ready(function(){
		    $(".rating-stars-disabled, .rating-stars").jRating({
		    	rateMax : 5,
		    	isDisabled : true
			});		    
		 });
	</script>
</lams:head>

<body class="stripes">


	<div id="content">

	<h1>
		<c:out value="${title}" escapeXml="true" />
	</h1>

		<c:forEach var="entry" items="${ToolContentTopicList}">
			<c:set var="sessionName" value="${entry.key}"/>

			<h2>
				${sessionName}
			</h2>

			<c:set var="topicThread" value="${entry.value[0]}"/>
			<c:forEach var="msgDto" items="${topicThread}">
				<c:set var="indentSize" value="${msgDto.level*3}" />
				<c:set var="hidden" value="${msgDto.message.hideFlag}" />
				<div style="margin-left:<c:out value='${indentSize}'/>em;">
					<table cellspacing="0" class="forum">
						<tr>
							<th class="first">
								<c:choose>
									<c:when test='${(mode == "teacher") || (not hidden)}'>
										<b> <c:out value="${msgDto.message.subject}" /> </b>
									</c:when>
									<c:otherwise>
										<fmt:message key="topic.message.subject.hidden" />
									</c:otherwise>
								</c:choose>
							</th>
						</tr>
						<tr>
							<td class="first posted-by">
								<c:if test='${(mode == "teacher") || (not hidden)}'>
									<fmt:message key="lable.topic.subject.by" />
									<c:set var="author" value="${msgDto.author}"/>
									<c:if test="${empty author}">
										<c:set var="author">
											<fmt:message key="label.default.user.name"/>
										</c:set>
									</c:if>
									<c:out value="${author}" escapeXml="true"/>						
											-
									<lams:Date value="${msgDto.message.updated}"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<td>
								<c:if test='${(not hidden) || (hidden && mode == "teacher")}'>
									<c:out value="${msgDto.message.body}" escapeXml="false" />
								</c:if>
								<c:if test='${hidden}'>
									<fmt:message key="topic.message.body.hidden" />
								</c:if>
							</td>
						</tr>

						<c:if test="${not empty msgDto.message.attachments}">
							<tr>
								<td>
									<c:forEach var="file" items="${msgDto.message.attachments}">
										<a href="${msgDto.attachmentLocalUrl}"> <c:out value="${msgDto.attachmentName}" /> </a>
									</c:forEach>
								</td>
							</tr>
						</c:if>
						<%-- display mark for teacher --%>
						<c:if test="${(msgDto.released && msgDto.isAuthor)|| mode=='teacher'}">
							<tr>
								<td>
									<span class="field-name" ><fmt:message key="lable.topic.title.mark"/></span>
									<br>
									<c:choose>
										<c:when test="${empty msgDto.mark}">
											<fmt:message key="message.not.avaliable"/>
										</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${msgDto.mark}"  maxFractionDigits="2"/>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>
									<span class="field-name" ><fmt:message key="lable.topic.title.comment"/></span>
									<br>
									<c:choose>
										<c:when test="${empty msgDto.comment}">
											<fmt:message key="message.not.avaliable"/>
										</c:when>
										<c:otherwise>
											<c:out value="${msgDto.comment}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:if>
						<c:if test="${sessionMap.allowRateMessages}">
							<tr>
								<td>
									<div class="right-buttons">
										<%@ include file="/jsps/learning/ratingStars.jsp"%>
									</div>
								</td>
							</tr>
						</c:if>
					</table>
				</div>
			</c:forEach>

			<c:set var="userDTOSet" value="${entry.value[1]}"/>
			<c:if test="${userDTOSet ne null}">
				<h3><fmt:message key="label.export.reflection" /></h3>
				<c:forEach var="userDTO" items="${userDTOSet}">
					<h4><c:out value="${userDTO.fullName}" escapeXml="true"/></h4>
					<p>
						<lams:out value="${userDTO.reflect}" escapeHtml="true" /> 
					</p>	
				</c:forEach>
			</c:if>
			
		</c:forEach>

	</div>

</body>
</lams:html>
