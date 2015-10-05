<c:set var="userContributionList2" value="${imageList[0]}"/>
<c:set var="userContribution2" value="${userContributionList2[0]}"/>

<h1>
	<fmt:message key="monitoring.label.group" /> ${userContribution2.sessionName} 
</h1>

<div style="margin-left: 20px; margin-top: 20px;">

	<c:forEach var="userContributionList" items="${imageList}" varStatus="status">
		<c:set var="userContribution" value="${userContributionList[0]}"/>
		<c:set var="image" value="${userContribution.image}"/>

		<table>
			<tr>
				<td 
				<c:if test="${image.createByAuthor || (image.createBy == null)}">colspan="2"</c:if>
				 >
					<h2>
						<c:out value="${image.title}"></c:out>
					</h2>
				</td>
				<c:if test="${!image.createByAuthor && image.createBy != null}">
					<td style="align:left;">	
						<h2>
							<fmt:message key="label.export.uploaded.by" />:
							<c:out value="${image.createBy.firstName} ${image.createBy.lastName}" escapeXml="true"/>
						</h2>
					</td>
				</c:if>
				
			</tr>		
			<tr>
				<td colspan="2">	
					<h2>
						<c:out value="${image.description}" escapeXml="false"></c:out>
					</h2>
				</td>
			</tr>	
		</table>	
		<br>

		<c:choose>
			<c:when test="${imageGallery.allowRank == true}">
				<div>
					<c:forEach var="i" begin="1" end="5">
						<input class="star" type="radio" name="rating${image.uid}${userContribution2.sessionName}" value="${i}"
							title = "<fmt:message key='label.learning.average.rating'/> ${userContribution.averageRating}"
							<c:if test="${(userContribution.averageRating - userContribution.averageRating % 1) == i}"> checked = 'checked' </c:if>
						/>
					</c:forEach>
				</div>
				<p>
					<span id="numberRatings">${userContribution.numberRatings}</span>
					<fmt:message key="label.learning.ratings" />
				</p>
			</c:when>
			
			<c:when test="${imageGallery.allowVote == true}">
				<fmt:message key="label.monitoring.number.votes" />: ${userContribution.numberOfVotesForImage}
				<br>						
			</c:when>
		</c:choose>	
		
		<br>
		<div>	
			<img src="${image.attachmentLocalUrl}"	border="0">	
		</div>
		<br>
		
		
		<table cellpadding="0" class="alternative-color" >
			<tr>
				<th width="100px">
					<fmt:message key="label.monitoring.imagesummary.user" />
				</th>
				<c:if test="${imageGallery.allowVote}">				
					<th style="padding-left:0px; text-align:center; width: 100px;">
						<fmt:message key="label.monitoring.imagesummary.voted.for.this.image" />
					</th>
				</c:if>
				<c:if test="${imageGallery.allowRank}">					
					<th width="50px">
						<fmt:message key="label.monitoring.imagesummary.rating" />
					</th>
				</c:if>						
				<c:if test="${imageGallery.allowCommentImages}">
					<th >
						<fmt:message key="label.monitoring.imagesummary.comments" />
					</th>
				</c:if>			
			</tr>
						
			<c:forEach var="userContribution" items="${userContributionList}">
							
				<tr>
					<td>
						<c:out value="${userContribution.user.firstName} ${userContribution.user.lastName}" escapeXml="true" />
					</td>
							
					<c:if test="${imageGallery.allowVote}">								
						<td style="padding-left:0px; text-align:center;">
							<c:choose>
								<c:when test="${userContribution.votedForThisImage}">
									<img src="../images/tick.gif"	border="0">
								</c:when>
													
								<c:otherwise>
									<img src="../images/dash.gif" border="0">
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>
										
				</tr>
										
								
			</c:forEach>
		</table>
		
		<p></p>
	</c:forEach>
</div>		