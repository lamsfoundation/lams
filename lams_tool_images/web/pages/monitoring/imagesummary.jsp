<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
		
		<script type="text/javascript">

			function deleteComment(commentUid) {
				var	deletionConfirmed = confirm("<fmt:message key="warning.msg.monitoring.do.you.want.to.delete"></fmt:message>");

				if (deletionConfirmed) {
					window.location.href = "<c:url value='/monitoring/removeComment.do'/>?sessionMapID=${sessionMapID}&commentUid=" + commentUid;
				}
			}
		</script>
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
			
			<c:forEach var="groupSummary" items="${imageSummary}">
				<h1><fmt:message key="monitoring.label.group" /> ${groupSummary[0].sessionName}	</h1>
				
				<c:choose>
					<c:when test="${imageGallery.allowRank == true}">
						<ul>							
							<li>
								<fmt:message key="label.monitoring.number.rated" />: ${groupSummary[0].numberRatings}
							</li>				
							<li>
								<fmt:message key="label.monitoring.average.rating" />: ${groupSummary[0].averageRating}
							</li>
						</ul>
						<br>
					</c:when>
					<c:when test="${imageGallery.allowVote == true}">
						<ul>
							<li>
								<fmt:message key="label.monitoring.number.votes" />: ${groupSummary[0].numberOfVotesForImage}
							</li>
						</ul>
						<br>						
					</c:when>
				</c:choose>			
				
				<table cellpadding="0" class="alternative-color" >
			
					<tr>
						<th width="150px">
							<fmt:message key="label.monitoring.imagesummary.user" />
						</th>
						<c:if test="${imageGallery.allowVote}">				
							<th style="padding-left:0px; text-align:center; width: 100px;">
								<fmt:message key="label.monitoring.imagesummary.voted.for.this.image" />
							</th>
						</c:if>
						<c:if test="${imageGallery.allowRank}">					
							<th style="padding-left:0px; text-align:center; width: 100px;">
								<fmt:message key="label.monitoring.imagesummary.rating" />
							</th>
						</c:if>						
						<c:if test="${imageGallery.allowCommentImages}">
							<th >
								<fmt:message key="label.monitoring.imagesummary.comments" />
							</th>
						</c:if>			
					</tr>
				
				
					<c:forEach var="userImageContribution" items="${groupSummary}">
					
						<tr>
							<td>
								<c:out value="${userImageContribution.user.loginName}" escapeXml="true"/>
							</td>
							
							<c:if test="${imageGallery.allowVote}">								
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
							</c:if>
							
							<c:if test="${imageGallery.allowRank}">								
								<td style="padding-left:0px; text-align:center;">
									${userImageContribution.rating}
								</td>
							</c:if>
										
							<c:if test="${imageGallery.allowCommentImages}">
								<td>
								
									<ul>
										<c:forEach var="comment" items="${userImageContribution.comments}">
											<li>
												<c:out value="${comment.comment}" escapeXml="false" />
												
												<c:set var="editCommentUrl" >
													<c:url value='/monitoring/editComment.do'/>?sessionMapID=${sessionMapID}&commentUid=${comment.uid}&TB_iframe=true&height=300&width=300
												</c:set>		
												<a href="${editCommentUrl}" class="thickbox" style="margin-left: 20px;"> 
													<img src="<html:rewrite page='/includes/images/edit.gif'/>" 
															title="<fmt:message key="label.authoring.basic.resource.edit" />" style="border-style: none;"/>
												</a>
		
												<a href="#" style="margin-left: 15px;" id="removeCommentLink" onclick="deleteComment(${comment.uid});" >
													<img src="<html:rewrite page='/includes/images/cross.gif'/>" 												 
															title="<fmt:message key="label.authoring.basic.resource.delete" />" style="border-style: none;"/>												
												</a>
											</li>
										</c:forEach>
									</ul>
									
								</td>
							</c:if>
								
						</tr>
					</c:forEach>
				</table>
			
			</c:forEach>
			<br>
			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="self.parent.tb_remove();" class="button right-buttons space-left"><fmt:message
						key="label.cancel" /> </a>
				<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button right-buttons space-left"><fmt:message
						key="label.monitoring.imagesummary.save" /> </a>
			</lams:ImgButtonWrapper>
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->
		
	</body>
</lams:html>
