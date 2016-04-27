<%--  commentDto, commentUid, msgLevel needs to be in the session elsewhere --%>

	<c:set var="hidden" value="${commentDto.comment.hideFlag}" />
	
	<c:choose>
	<c:when test='${(commentDto.level <= 1)}'>
		<c:set var="indentSize" value="0" />
	</c:when>
	<c:otherwise>
		<c:set var="indentSize" value="${(commentDto.level-1)*indent}" />
	</c:otherwise>
	</c:choose>

	<c:choose>
	<c:when test='${commentDto.comment.uid == commentUid}'>
		<c:set var="highlightClass">highlight</c:set>
	</c:when>
	<c:otherwise>	
		<c:set var="highlightClass"></c:set>
	</c:otherwise> 
	</c:choose>
	
	<div id="msg${commentDto.comment.uid}" class="${highlightClass}" style="margin-left:<c:out value="${indentSize}"/>px;">
		<table id="table${commentDto.comment.uid}" cellspacing="0" class="comment ${highlightClass}" >
			<tr>
				<td>
					<c:if test='${(sessionMap.mode == "teacher") || (not hidden)}'>
						<c:set var="author" value="${commentDto.authorname}" />
					</c:if>
					<c:if test="${empty author}">
						<c:set var="author">&nbsp;</c:set>
					</c:if>
					
					<c:choose>
           			<c:when test="${isSticky}">
						<span class="comment-sticky">
							<i class="fa fa-thumb-tack"></i>
		            </c:when>
        		    <c:otherwise>
						<span>
				            <i class="fa fa-user"></i> 
					</c:otherwise>
					</c:choose>
				
							<span id="author" class="comment-author"><c:out value="${author}" escapeXml="true"/></span>			
							<span id="date" class="comment-date"><lams:Date value="${commentDto.comment.updated}"/>
							<c:if test='${commentDto.comment.created != commentDto.comment.updated}'>
							(<fmt:message key="label.edited"/>)
							</c:if>
							</span>
						</span>
				</td>
			</tr>
			<tr>
				<td>
					<span id="msgBody">
					<c:if
						test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
						<lams:out value="${commentDto.comment.body}" escapeHtml="true" />
					</c:if>
					<c:if test='${hidden}'>
						<i><fmt:message key="label.hidden" /></i>
					</c:if>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<c:if test = '${not sessionMap.readOnly}'>
					<c:set var="replytopic"><lams:LAMSURL />comments/newReplyTopic.do?sessionMapID=${sessionMapID}&parentUid=${commentDto.comment.uid}</c:set>
					<a href="#" onclick="javascript:createReply(${commentDto.comment.uid}, '${replytopic}');" class="comment">Reply</a>
					&middot; 
					</c:if>
					
					<c:if test='${((sessionMap.mode == "teacher") || commentDto.author) && not sessionMap.readOnly }'>
						<c:set var="edittopic"><lams:LAMSURL />comments/editTopic.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&create=${commentDto.comment.created.time}</c:set>
						<a href="#" onclick="javascript:createEdit(${commentDto.comment.uid}, '${edittopic}');" class="comment">Edit</a> 
					&middot; 
					</c:if>

					<c:if test='${sessionMap.mode == "teacher" && not sessionMap.readOnly}'>
					<!--  call the hide action -->
					<c:choose>
						<c:when test="${hidden}">
							<!--  display a show link  -->
							<c:set var="hideURL"><lams:LAMSURL />comments/hide.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&hideFlag=false</c:set>
							<html:link href="javascript:hideEntry(${commentDto.comment.uid}, '${hideURL}');" styleClass="comment"><fmt:message key="label.show" /></html:link>
						</c:when>
						<c:otherwise>
							<!--  display a hide link -->
							<c:set var="hideURL"><lams:LAMSURL />comments/hide.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&hideFlag=true</c:set>
							<html:link href="javascript:hideEntry(${commentDto.comment.uid}, '${hideURL}');" styleClass="comment"><fmt:message key="label.hide" /></html:link>
						</c:otherwise>
					</c:choose>
					&middot; 
					</c:if>

		           <c:if test='${sessionMap.mode == "teacher" && not sessionMap.readOnly && commentDto.comment.commentLevel == 1}'>
  	  	  	  	 	 	<c:set var="makesticky"><lams:LAMSURL />comments/makeSticky.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&create=${commentDto.comment.created.time}&sticky=${!commentDto.comment.sticky}</c:set>
  	  	  	  	 	 	<html:link href="javascript:makeSticky(${commentDto.comment.uid}, '${makesticky}');" styleClass="comment">
  	  	  	  	 	 	<fmt:message key="${commentDto.comment.sticky ? 'label.remove.sticky' : 'label.add.sticky' }"/></html:link> 
  	  	  	  	 	 	 &middot; 
  	  	  	  	  </c:if>
  	  	  	  	 	 	
					<span id="msglikeCount${commentDto.comment.uid}">${commentDto.comment.likeCount}</span> <fmt:message key="label.likes"/>
					<c:if test = '${sessionMap.mode == "learner"}'>
						<span id="msgvote${commentDto.comment.uid}">
						<c:choose>
						<c:when test="${empty commentDto.comment.vote && not sessionMap.readOnly}">
							<span class="fa fa-thumbs-up fa-lg fa-faded" title="<fmt:message key="label.like"/>"
								onclick="javascript:likeEntry(${commentDto.comment.uid});" id="msglikebutton${commentDto.comment.uid}"></span>
							<c:if test='${sessionMap.likeAndDislike}'> 
							&nbsp;<span class="fa fa-thumbs-down fa-lg fa-flip-horizontal fa-faded" title="<fmt:message key="label.dislike"/>"
							onclick="javascript:dislikeEntry(${commentDto.comment.uid});" id="msgdislikebutton${commentDto.comment.uid}"></span>
							</c:if>
						</c:when>
						<c:when test="${commentDto.comment.vote == 1}">
							<span class="fa fa-thumbs-o-up fa-lg" title="<fmt:message key="label.like"/>"
							id="msglikebutton${commentDto.comment.uid}"/>
						</c:when>
						<c:otherwise>
							<c:if test='${sessionMap.likeAndDislike}'> 
							<span class="fa fa-thumbs-o-down fa-lg fa-flip-horizontal" title="<fmt:message key="label.dislike"/>"
							id="msgdislikebutton${commentDto.comment.uid}"></span>
							</c:if>
						</c:otherwise>
						</c:choose>
						</span>
					</c:if>
				</td>
			</tr>
		</table>
	</div>

