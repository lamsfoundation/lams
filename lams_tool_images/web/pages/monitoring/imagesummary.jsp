<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="imageGallery" value="${sessionMap.imageGallery}" />		
		
		<%@ include file="/common/header.jsp"%>
		<link href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		
		<script type="text/javascript">
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";
		
			//vars for rating.js
			var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
			YOUR_RATING_LABEL = '',
			IS_DISABLED =  true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			MAX_RATES = 0,
			MIN_RATES = 0,
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '',
			WARN_COMMENTS_IS_BLANK_LABEL = '',
			WARN_MIN_NUMBER_WORDS_LABEL = '';
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	</lams:head>
	
	<body class="stripes">
		<div id="content">

			<html:form action="/monitoring/updateImage" method="post" styleId="imageGalleryItemForm">
				<html:hidden property="imageUid" />		
				<html:hidden property="sessionMapID" value="${sessionMapID}"/>
				
				<div class="field-name space-top">
					<fmt:message key="label.monitoring.imagesummary.image" />
				</div>
				<c:set var="mediumImagePath">
			   		<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false
				</c:set>	
				<c:set var="title">
					<c:out value="${image.title}" escapeXml="true"/>
				</c:set>
				<img src="${mediumImagePath}" alt="${title}" title="${title}"/>
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.title.input" />
				</div>
				<html:text property="title" size="55" tabindex="1" />
				
				<div class="field-name space-top">
	            	<fmt:message key="label.authoring.basic.resource.description.input" />
				</div>
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<div class="small-space-bottom" >
					<lams:CKEditor id="description" value="${formBean.description}" contentFolderID="${sessionMap.contentFolderID}" />
				</div>
			</html:form>			
			<br>
			
			<c:if test="${imageGallery.allowRank}">
				<lams:Rating itemRatingDto="${itemRatingDto}" disabled="true"
						isItemAuthoredByUser="true"
						maxRates="0" 
						countRatedItems="0" />
			</c:if>
			
			<c:if test="${imageGallery.allowVote}">
			
				<c:forEach var="groupSummary" items="${imageSummary}">
					<h1><fmt:message key="monitoring.label.group" /> ${groupSummary[0].sessionName}	</h1>
				
					<ul>
						<li>
							<fmt:message key="label.monitoring.number.votes" />: ${groupSummary[0].numberOfVotesForImage}
						</li>
					</ul>
					<br>
				
					<table cellpadding="0" class="alternative-color" >
				
						<tr>
							<th width="150px">
								<fmt:message key="label.monitoring.imagesummary.user" />
							</th>			
							<th style="padding-left:0px; text-align:center; width: 100px;">
								<fmt:message key="label.monitoring.imagesummary.voted.for.this.image" />
							</th>
						</tr>
				
						<c:forEach var="userImageContribution" items="${groupSummary}">
						
							<tr>
								<td>
									<c:out value="${userImageContribution.user.firstName} ${userImageContribution.user.lastName}" escapeXml="true"/>
								</td>
													
								<td style="padding-left:0px; text-align:center;">
									<c:choose>
										<c:when test="${userImageContribution.votedForThisImage}">
											<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">
										</c:when>
											
										<c:otherwise>
											<img src="<html:rewrite page='/includes/images/dash.gif'/>" border="0">
										</c:otherwise>
									</c:choose>
								</td>
								
							</tr>
						</c:forEach>
					</table>
				
				</c:forEach>
			</c:if>
			<br>
			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="self.parent.tb_remove();" class="button right-buttons space-left">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button right-buttons space-left">
					<fmt:message key="label.monitoring.imagesummary.save" /> 
				</a>
			</lams:ImgButtonWrapper>
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->
		
	</body>
</lams:html>
