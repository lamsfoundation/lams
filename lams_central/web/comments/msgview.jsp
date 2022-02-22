<%--  commentDto, commentUid, msgLevel needs to be in the session elsewhere --%>

  <c:set var="hidden" value="${commentDto.comment.hideFlag}" />
  <c:set var="anonymous" value="${commentDto.comment.anonymous}" />

  <c:choose>
    <c:when test='${(commentDto.level <= 1)}'>
      <c:set var="indentSize" value="0" />
    </c:when>
    <c:otherwise>
      <c:set var="indentSize" value="${(commentDto.level-1)*indent}" />
    </c:otherwise>
  </c:choose>
  <c:set var="paddingRightSize" value="${(commentDto.level-1)*indent+6}" />

  <c:choose>
    <c:when test='${commentDto.comment.uid == commentUid}'>
      <c:set var="highlightClass">highlight</c:set>
    </c:when>
    <c:otherwise>	
      <c:set var="highlightClass"></c:set>
    </c:otherwise> 
  </c:choose>

  <div class="row">
    <div class="col-xs-12" style="margin-left:${indentSize}px;padding-right:${paddingRightSize}px">
      <div class="panel panel-default ${highlightClass} msg" id="msg${commentDto.comment.uid}">
        <div class="panel-heading">
          <h4 class="panel-title">
          
          	<%-- authors name --%>
			<c:set var="msgAuthor" value="${commentDto.authorname}" />
			<c:if test="${empty msgAuthor}">
				<c:set var="msgAuthor">&nbsp;</c:set>
			</c:if>
			<c:choose>
			<c:when test='${sessionMap.mode == "teacher"}'>
				<c:choose>
				<c:when test="${anonymous}">
					<c:set var="author">${msgAuthor} (<fmt:message key="label.anonymous" />)</c:set>
				</c:when>
				<c:otherwise>
					<c:set var="author">${msgAuthor}</c:set>
				</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
				<c:when test="${hidden}">
					<c:set var="author"></c:set>
				</c:when>
				<c:when test="${not hidden and anonymous}">
					<c:set var="author"><fmt:message key="label.anonymous" /></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="author">${msgAuthor}</c:set>
				</c:otherwise>
				</c:choose>
			</c:otherwise>
			</c:choose>

          	<c:choose>
            <c:when test="${isMonitor and isSticky}">
				<c:set var="textClass" value="text-info"/>
				<c:set var="bgClass" value="bg-info"/>
				<i class="fa fa-thumb-tack ${textClass}"></i>
            </c:when>
            <c:when test="${isMonitor}">
				<c:set var="textClass" value="text-info"/>
				<c:set var="bgClass" value="bg-info"/>
				<i class="fa fa-mortar-board ${textClass}"></i>
            </c:when>
            <c:when test="${isSticky}">
				<c:set var="textClass" value="text-info"/>
				<c:set var="bgClass" value=""/>
				<i class="fa fa-thumb-tack ${textClass}"></i>
            </c:when>
            <c:otherwise>
				<c:set var="textClass" value=""/>
				<c:set var="bgClass" value=""/>
				<i class="fa fa-user"></i> 
			</c:otherwise>
			</c:choose>            

            <span class="${textClass}"><strong><c:out value="${author}" escapeXml="true"/></strong> - 
            <lams:Date value="${commentDto.comment.updated}" timeago="true"/>
            <c:if test='${commentDto.comment.created != commentDto.comment.updated}'>
              | <small>(<fmt:message key="label.edited"/>)</small>
            </c:if>  
            </span>         
          </h4>
        </div>
        <div class="panel-body ${bgClass}" id="pb-msg${commentDto.comment.uid}">
        
        	  <c:if test='${(sessionMap.mode == "teacher") or not (anonymous or hidden)}'>
	      <div class="pull-left roffset10"><lams:Portrait userId="${commentDto.authorUserId}"/></div>
	      </c:if>
	      
          <c:if test='${(sessionMap.mode == "teacher") or (not hidden)}'>
            <lams:out value="${commentDto.comment.body}" escapeHtml="true" />
          </c:if>
          <c:if test='${hidden}'>
            <i><fmt:message key="label.hidden" /></i>
          </c:if>

          <hr class="msg-hr">
          <div class="msg-footer">

            <c:if test = '${not sessionMap.readOnly}'>
              <c:set var="replytopic"><lams:LAMSURL />comments/newReplyTopic.do?sessionMapID=${sessionMapID}&parentUid=${commentDto.comment.uid}</c:set>
              <a href="#nogo" onclick="javascript:createReply(${commentDto.comment.uid}, '${replytopic}');" class="comment">Reply</a>
              &middot; 
            </c:if>

            <c:if test='${((sessionMap.mode == "teacher") || commentDto.author) && not sessionMap.readOnly }'>
              <c:set var="edittopic"><lams:LAMSURL />comments/editTopic.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&create=${commentDto.comment.created.time}</c:set>
              <a href="#nogo" onclick="javascript:createEdit(${commentDto.comment.uid}, '${edittopic}');" class="comment">Edit</a> 
              &middot; 
            </c:if>

            <c:if test='${sessionMap.mode == "teacher" && not sessionMap.readOnly}'>
              <!--  call the hide action -->
              <c:choose>
                <c:when test="${hidden}">
                  <!--  display a show link  -->
                  <c:set var="hideURL"><lams:LAMSURL />comments/hide.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&hideFlag=false</c:set>
                  <a href="javascript:hideEntry(${commentDto.comment.uid}, '${hideURL}');" class="comment"><fmt:message key="label.show" /></a>
                </c:when>
                <c:otherwise>
                  <!--  display a hide link -->
                  <c:set var="hideURL"><lams:LAMSURL />comments/hide.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&hideFlag=true</c:set>
                  <a href="javascript:hideEntry(${commentDto.comment.uid}, '${hideURL}');" class="comment"><fmt:message key="label.hide" /></a>
                </c:otherwise>
              </c:choose>
              &middot; 
            </c:if>

            <c:if test='${sessionMap.mode == "teacher" && not sessionMap.readOnly && commentDto.comment.commentLevel == 1}'>
              <c:set var="makesticky"><lams:LAMSURL />comments/makeSticky.do?sessionMapID=${sessionMapID}&commentUid=${commentDto.comment.uid}&create=${commentDto.comment.created.time}&sticky=${!commentDto.comment.sticky}</c:set>
              <a href="#nogo" onclick="javascript:makeSticky(${commentDto.comment.uid}, '${makesticky}');" class="comment">
              	<fmt:message key="${commentDto.comment.sticky ? 'label.remove.sticky' : 'label.add.sticky' }"/></a> 
              &middot; 
            </c:if>

            <span id="msglikeCount${commentDto.comment.uid}">${commentDto.comment.likeCount}</span> <fmt:message key="label.likes"/>
            <c:if test = '${sessionMap.mode == "learner"}'>
              <span id="msgvote${commentDto.comment.uid}">
                <c:choose>
                  <c:when test="${empty commentDto.comment.vote && not sessionMap.readOnly}">
                    <span class="fa fa-thumbs-up fa-md fa-faded" title="<fmt:message key="label.like"/>"
                          onclick="javascript:likeEntry(${commentDto.comment.uid});" id="msglikebutton${commentDto.comment.uid}"></span>
                    <c:if test='${sessionMap.likeAndDislike}'> 
                      &nbsp;<span class="fa fa-thumbs-down fa-md fa-flip-horizontal fa-faded" title="<fmt:message key="label.dislike"/>"
                                  onclick="javascript:dislikeEntry(${commentDto.comment.uid});" id="msgdislikebutton${commentDto.comment.uid}"></span>
                    </c:if>
                  </c:when>
                  <c:when test="${commentDto.comment.vote == 1}">
                    <span class="fa fa-thumbs-o-up fa-md" title="<fmt:message key="label.like"/>"
                          id="msglikebutton${commentDto.comment.uid}"/>
                  </c:when>
                  <c:otherwise>
                    <c:if test='${sessionMap.likeAndDislike}'> 
                      <span class="fa fa-thumbs-o-down fa-md fa-flip-horizontal" title="<fmt:message key="label.dislike"/>"
                            id="msgdislikebutton${commentDto.comment.uid}"></span>
                    </c:if>
                  </c:otherwise>
                </c:choose>
              </span>
            </c:if>          

          </div>          

        </div>
  
      </div>

    </div>

  </div>


