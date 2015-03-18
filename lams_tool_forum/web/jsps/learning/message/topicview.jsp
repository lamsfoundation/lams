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

	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$(".button").click(function (e) {
    	e.stopPropagation();
	});
	$(".rating-stars-div").click(function (e) {
    	e.stopPropagation();
	});
	$("#attachments").click(function (e) {
    	e.stopPropagation();
	});

	function createReply(messageUid, url, level) {
		if ( document.getElementById('reply') ) {
			alert('<fmt:message key="message.complete.or.cancel.reply"/>');
		} else {
			// set up the new reply area
			var replyDiv = document.createElement("div");
			replyDiv.id = 'reply';

			if ( level == 0 ) {
				$('#msg'+messageUid).after(replyDiv);
			} else {				
				var parentDiv = document.getElementById('msg'+messageUid);
				parentDiv.appendChild(replyDiv);
			}
			
			$(replyDiv).load(url);
		}
	}

	function createEdit(messageUid, url, level) {
		if ( document.getElementById('edit') ) {
			alert('<fmt:message key="message.complete.or.cancel.edit"/>');
		} else {
			// set up the new edit area
			var parentDiv = document.getElementById('msg'+messageUid);
			var editDiv = document.createElement("div");
			editDiv.id = 'edit';
			parentDiv.appendChild(editDiv);
			$(editDiv).load(url);
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
	<c:when test='${(msgLevel == 1)}'>
		<%-- same test & command appears at bottom of script --%>
		<c:if test='${messageTablename != ""}'>
			</table>
			<script> 
				$("#${messageTablename}").treetable({${tableCommand}});
			</script>	
			</div>
		</c:if>
		<c:set var="messageTablename" value="tree${msgDto.message.uid}"/>
		<div id="thread${msgDto.message.uid}">
		<table id="${messageTablename}">
		<tr data-tt-id="${msgDto.message.uid}"><td>	
	</c:when>
	<c:otherwise>
		<tr data-tt-id="${msgDto.message.uid}" data-tt-parent-id="${msgDto.message.parent.uid}"><td>	
	</c:otherwise>
	</c:choose>
	
	<%@ include file="msgview.jsp"%>
	
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
