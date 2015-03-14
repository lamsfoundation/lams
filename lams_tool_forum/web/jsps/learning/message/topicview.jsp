<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-28 --%>
<c:set var="maxThreadUid" value="0"/>
<c:set var="messageTablename" value=""/>
<c:set var="indent" value="30"/>

<c:set var="show"><fmt:message key="label.show.replies" /></c:set>
<c:set var="hide"><fmt:message key="label.hide.replies" /></c:set>
<c:set var="prompt"><fmt:message key="label.showhide.prompt" /></c:set>
<c:set var="tableCommand">expandable:true,initialState:'expanded',
	expanderTemplate:'<a href=\"#\">&nbsp;&nbsp;&nbsp;&nbsp;${prompt}</a><',
	stringCollapse:'${hide}',stringExpand:'${show}',
	clickableNodeNames:true,indent:${indent},
	onNodeInitialized:function() {
		if (this.level() >= 2) {
			this.collapse();
		}
	}
 </c:set>

<script type="text/javascript">
function inspect(obj, maxLevels, level)
{
  var str = '', type, msg;

    // Start Input Validations
    // Don't touch, we start iterating at level zero
    if(level == null)  level = 0;

    // At least you want to show the first level
    if(maxLevels == null) maxLevels = 1;
    if(maxLevels < 1)     
        return '<font color="red">Error: Levels number must be > 0</font>';

    // We start with a non null object
    if(obj == null)
    return '<font color="red">Error: Object <b>NULL</b></font>';
    // End Input Validations

    // Each Iteration must be indented
    str += '<ul>';

    // Start iterations for all objects in obj
    for(property in obj)
    {
      try
      {
          // Show "property" and "type property"
          type =  typeof(obj[property]);
          str += '<li>(' + type + ') ' + property + 
                 ( (obj[property]==null)?(': <b>null</b>'):('')) + '</li>';

          // We keep iterating if this property is an Object, non null
          // and we are inside the required number of levels
          if((type == 'object') && (obj[property] != null) && (level+1 < maxLevels))
          str += inspect(obj[property], maxLevels, level+1);
      }
      catch(err)
      {
        // Is there some properties in obj we can't access? Print it red.
        if(typeof(err) == 'string') msg = err;
        else if(err.message)        msg = err.message;
        else if(err.description)    msg = err.description;
        else                        msg = 'Unknown';

        str += '<li><font color="red">(Error) ' + property + ': ' + msg +'</font></li>';
      }
    }

      // Close indent
      str += '</ul>';

    return str;
}
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$(".button").click(function (e) {
    	e.stopPropagation();
	});
	
	function createReply(messageUid, url) {
		if ( document.getElementById('reply') ) {
// I18N
			alert("Please complete or cancel the current reply before starting a new reply.");
		} else {
			// set up the new reply area
			var parentDiv = document.getElementById('msg'+messageUid);
			var replyDiv = document.createElement("div");
			replyDiv.id = 'reply';
			$(replyDiv).addClass('replydiv');
			parentDiv.appendChild(replyDiv);
			$(replyDiv).load(url);
		}
	}



</script>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="msgLevel" value="${msgDto.level}" />
	<c:set var="hidden" value="${msgDto.message.hideFlag}" />
	
	<c:if test='${(msgLevel <= 1)}'>
		<c:set var="maxThreadUid" value="${msgDto.message.uid}"/>
	</c:if>

	<c:choose>
	<c:when test='${(msgLevel == 0)}'>
		<c:set var="indentSize" value="0" />
	</c:when>
	<c:when test='${(msgLevel <= 1)}'>
		<%-- same test & command appears at bottom of script --%>
		<c:if test='${messageTablename != ""}'>
			</table>
			<script> 
				$("#${messageTablename}").treetable({${tableCommand}});
			</script>	
			</div>
		</c:if>
		<c:set var="messageTablename" value="tree${msgDto.message.uid}"/>
		<c:set var="indentSize" value="0" />
		<div id="thread${msgDto.message.uid}"/>
		<table id="${messageTablename}">
		<tr data-tt-id="${msgDto.message.uid}"><td>	
	</c:when>
	<c:otherwise>
	<c:set var="indentSize" value="${(msgDto.level-1)*indent}" />
		<tr data-tt-id="${msgDto.message.uid}" data-tt-parent-id="${msgDto.message.parent.uid}"><td>	
	</c:otherwise>
	</c:choose>
	
	<c:choose>
	<c:when test='${msgDto.message.uid == messageUid}'>
	<div id="msg${msgDto.message.uid}" style="margin-left:<c:out value="${indentSize}"/>px;" class="highlight" >
	</c:when>
	<c:otherwise>	
	<div id="msg${msgDto.message.uid}" style="margin-left:<c:out value="${indentSize}"/>px;" >
	</c:otherwise>
	</c:choose>
		<table cellspacing="0" class="forum">
			<tr>
				<th id="subject">
					<c:choose>
						<c:when test='${(sessionMap.mode == "teacher") || (not hidden)}'>
							<c:out value="${msgDto.message.subject}" />
						</c:when>
						<c:otherwise>
							<fmt:message key="topic.message.subject.hidden" />
						</c:otherwise>
					</c:choose>
				</th>
			</tr>
			<tr>
				<td class="posted-by">
					<c:if test='${(sessionMap.mode == "teacher") || (not hidden)}'>
						<fmt:message key="lable.topic.subject.by" />
						<c:set var="author" value="${msgDto.author}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<span id="author"><c:out value="${author}" escapeXml="true"/></span>						
								-
						<span id="date"><lams:Date value="${msgDto.message.updated}" /></span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>
					<span id="msgBody">
					<c:if
						test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
						<c:out value="${msgDto.message.body}" escapeXml="false" />
					</c:if>
					<c:if test='${hidden}'>
						<i><fmt:message key="topic.message.body.hidden" /> </i>
					</c:if>
					</span>
				</td>
			</tr>

			<c:if test="${not empty msgDto.message.attachments}">
				<tr>
					<td>
						<div id="attachments">
						<img src="<html:rewrite page="/images/paperclip.gif"/>" class="space-left float-left">
						<c:if
							test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'> 
							<c:forEach var="file" items="${msgDto.message.attachments}">
								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<c:out value="${file.fileName}" /> </a>
							</c:forEach>
						</c:if>
						<c:if test='${hidden}'>
							<fmt:message key="topic.message.attachment.hidden" />
						</c:if>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if
				test="${((msgDto.released && msgDto.isAuthor) || sessionMap.mode=='teacher') && (not empty msgDto.mark)}">
				<tr>
					<td>
						<span class="field-name"><fmt:message
								key="lable.topic.title.mark" />
						</span>
						<BR>
						<fmt:formatNumber value="${msgDto.mark}"  maxFractionDigits="2"/>
					</td>
				</tr>
				<tr>
					<td>
						<span class="field-name"><fmt:message
								key="lable.topic.title.comment" />
						</span>
						<BR>
						<c:choose>
							<c:when test="${empty msgDto.comment}">
								<fmt:message key="message.not.avaliable" />
							</c:when>
							<c:otherwise>
								<c:out value="${msgDto.comment}" escapeXml="false" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
			<tr>
				<td>
					<div class="right-buttons">
						<!--  Rating stars -->
						<%@ include file="/jsps/learning/ratingStars.jsp"%>
					
						<!--  Hide/Unhide Button -->
						<c:if test='${sessionMap.mode == "teacher"}'>
							<c:set var="updateMark">
								<html:rewrite
									page="/monitoring/editMark.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&updateMode=viewForum&hideReflection=${sessionMap.hideReflection}" />
							</c:set>
							<html:link href="${updateMark}" styleClass="button">
								<fmt:message key="lable.topic.title.mark" />
							</html:link>
							
							<!--  call the hide action -->
							<c:choose>
								<c:when test="${hidden}">
									<!--  display a show link  -->
									<c:set var="hidetopic">
										<html:rewrite
											page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=false" />
									</c:set>
									<html:link href="${hidetopic}" styleClass="button">
										<fmt:message key="label.show" />
									</html:link>
								</c:when>
								<c:otherwise>
									<!--  display a hide link -->
									<c:set var="hidetopic">
										<html:rewrite
											page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=true" />
									</c:set>
									<html:link href="${hidetopic}" styleClass="button">
										<fmt:message key="label.hide" />
									</html:link>
								</c:otherwise>
							</c:choose>
						</c:if>

						<!--  Edit Button -->
						<c:if test="${not hidden}">
							<c:if
								test='${(sessionMap.mode == "teacher") || (msgDto.isAuthor && not sessionMap.finishedLock && sessionMap.allowEdit && (empty msgDto.mark))}'>
								<c:set var="edittopic">
									<html:rewrite
										page="/learning/editTopic.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&rootUid=${sessinoMap.rootUid}&create=${msgDto.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
								</c:set>
								<html:link href="${edittopic}" styleClass="button" styleId="editButton">
									<fmt:message key="label.edit" />
								</html:link>
							</c:if>
					
							<!--  Reply Button -->
							<c:if
								test="${(not sessionMap.finishedLock) && (not noMorePosts)}">
								<c:set var="replytopic">
									<html:rewrite
										page="/learning/newReplyTopic.do?sessionMapID=${sessionMapID}&parentID=${msgDto.message.uid}&rootUid=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}" />
								</c:set>
								<a href="#${msgDto.message.uid}" onClick="javascript:createReply(${msgDto.message.uid},'${replytopic}')" class="button replybutton"><fmt:message key="label.reply" /></a>
							</c:if>
						</c:if>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<c:if test='${(msgLevel >= 1)}'>
		</td></tr>	
	</c:if>

</c:forEach>

	<c:if test='${messageTablename != ""}'>
		</table>
		<script>
			$("#${messageTablename}").treetable({${tableCommand}});
		</script>	
		</div>
	</c:if>
	
<c:set var="pageSize" value="<%= ForumConstants.DEFAULT_PAGE_SIZE %>"/>
<c:if test='${maxThreadUid > 0 && ! noMorePages}'>
	<div class="float-right">
	<c:set var="more"><html:rewrite page="/learning/viewTopicNext.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}&pageLastId=${maxThreadUid}&size=${pageSize}" /></c:set>
	<a href="<c:out value="${more}"/>" class="button"><fmt:message key="label.show.more.messages" /></a>
	</div>
</c:if>
