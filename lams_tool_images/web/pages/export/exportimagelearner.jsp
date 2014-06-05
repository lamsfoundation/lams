<fmt:message key="label.export.images" />

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
						<c:out value="${image.title}" escapeXml="true"/>
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
						<c:out value="${image.description}" escapeXml="false"/>
					</h2>
				</td>
			</tr>	
		</table>	
		<br>
		
		<c:choose>
			<c:when test="${imageGallery.allowRank == true}">
				<div>
					<c:forEach var="i" begin="1" end="5">
						<input class="star" type="radio" name="rating${image.uid}" value="${i}"
							title = "<fmt:message key='label.learning.average.rating'/> ${userContribution.averageRating}"
							<c:if test="${userContribution.rating == i}"> checked = 'checked' </c:if>
						/>
					</c:forEach>
				</div>
				<p>
					<span id="numberRatings">${userContribution.numberRatings}</span>
					<fmt:message key="label.learning.ratings" />
				</p>
			</c:when>
			
			<c:when test="${imageGallery.allowVote == true}">
				<fmt:message key="label.export.voted.for.this.image" />: ${userContribution.votedForThisImage}
				<br>						
			</c:when>
		</c:choose>
		
		<br>
		<div>	
			<img src="${image.attachmentLocalUrl}"	border="0">	
		</div>
		<br>
		
		<c:forEach var="comment" items="${userContribution.comments}">
			<div>
				<table cellspacing="0" class="forum" style="width: 50%;">
					<tr >
						<th >
							<fmt:message key="label.learning.by" />
							<c:set var="author" value="${comment.createBy.firstName} ${comment.createBy.lastName}" />
							<c:out value="${author}" escapeXml="true"/>
									-				
							<lams:Date value="${comment.createDate}" />
						</th>
					</tr>
					
					<tr>
						<td>
							<c:out value="${comment.comment}" escapeXml="false" />
						</td>
					</tr>
						
				</table>
			</div>
		</c:forEach>
		
		<br>
		<c:if test="${! status.last}"><hr></c:if> 
		
	</c:forEach>
</div>		
