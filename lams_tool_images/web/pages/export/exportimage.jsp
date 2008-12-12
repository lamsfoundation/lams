<c:set var="userContributionList2" value="${sessionList[0]}"/>
<c:set var="userContribution2" value="${userContributionList2[0]}"/>
<c:set var="item" value="${userContribution2.image}"/>
<div>
	<img src="${item.attachmentLocalUrl}"	border="0">
	<h2>
		<c:out value="${item.title}"></c:out>
	</h2>
	
	<h2>
		<c:out value="${item.description}" escapeXml="false"></c:out>
	</h2>
</div>
<br/>

<c:forEach var="userContributionList" items="${sessionList}">
	<c:set var="userContribution" value="${userContributionList[0]}"/>

	<h1><fmt:message key="monitoring.label.group" /> ${userContribution.sessionName} </h1>
	
	<c:choose>
		<c:when test="${imageGallery.allowRank == true}">
			<ul>							
				<li>
					<fmt:message key="label.monitoring.number.rated" />: ${userContribution.numberRatings}
				</li>				
				<li>
					<fmt:message key="label.monitoring.average.rating" />: ${userContribution.averageRating}
				</li>
			</ul>
			<br>
		</c:when>
		<c:when test="${imageGallery.allowVote == true}">
			<ul>
				<li>
					<fmt:message key="label.monitoring.number.votes" />: ${userContribution.numberOfVotesForImage}
				</li>
			</ul>
			<br>						
		</c:when>
	</c:choose>		
	
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
					${userContribution.user.loginName}
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
							
				<c:if test="${imageGallery.allowRank}">								
					<td style="padding-left:0px; text-align:center;">
						${userImageContribution.rating}
					</td>
				</c:if>
										
				<c:if test="${imageGallery.allowCommentImages}">
					<td>
								
						<ul>
							<c:forEach var="comment" items="${userContribution.comments}">
								<li>
									<c:out value="${comment.comment}" escapeXml="false" />
								</li>
							</c:forEach>
						</ul>
									
					</td>
				</c:if>
								
			</tr>
								
						
		</c:forEach>
	</table>

	<p></p>
</c:forEach>

