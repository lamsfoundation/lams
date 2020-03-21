<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<c:set var="maxThreadUid" value="0" />
<c:set var="messageTablename" value="" />
<c:set var="indent" value="10" />

<c:set var="show">
	<fmt:message key="label.show.replies" />
</c:set>
<c:set var="hide">
	<fmt:message key="label.hide.replies" />
</c:set>
<c:set var="prompt">
	<fmt:message key="label.showhide.prompt" />
</c:set>
<c:set var="tableCommand">expandable:true,initialState:'expanded',
	expanderTemplate:'<a href=\"#\"><span style="margin-left:20px">${prompt}</span></a>',
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
	$(".msg-footer").click(function (e) {
    	e.stopPropagation();
	});
	$(".attachments").click(function (e) {
    	e.stopPropagation();
	});

	function createReply(messageUid, url, level) {
		if ( document.getElementById('reply') ) {
			alert('<fmt:message key="message.complete.or.cancel.reply"/>');
		} else {
			// set up the new reply area
			var replyDiv = document.createElement("div");
			replyDiv.id = 'reply';
			$('#pb-msg'+messageUid).after(replyDiv);
			$.ajaxSetup({ cache: true });
			$(replyDiv).load(url);
		}
	}

	function createEdit(messageUid, url, level) {
		if ( document.getElementById('edit') ) {
			alert('<fmt:message key="message.complete.or.cancel.edit"/>');
		} else {
			// set up the new edit area
			var editDiv = document.createElement("div");
			editDiv.id = 'edit';
			$('#pb-msg'+messageUid).after(editDiv);
			$.ajaxSetup({ cache: true });
			$(editDiv).load(url);
		}
	}
	
</script>

	<c:forEach var="msgDto" items="${topicThread}">
		<c:set var="msgLevel" value="${msgDto.level}" />
		<c:set var="hidden" value="${msgDto.message.hideFlag}" />
	
		<c:if test='${(msgLevel <= 1)}'>
			<c:set var="maxThreadUid" value="${msgDto.message.uid}" />
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
				 <!--  end thread ${messageTablename} -->	
			</c:if>
			<c:set var="messageTablename" value="tree${msgDto.message.uid}" />
				<!--  start thread  -->
				<div id="thread${msgDto.message.uid}" class="clearfix">
				<table id="${messageTablename}" class="col-12">
					<tr data-tt-id="${msgDto.message.uid}"><td>
			</c:when>
			<c:otherwise>
				<tr data-tt-id="${msgDto.message.uid}" data-tt-parent-id="${msgDto.message.parent.uid}">
					<td>
			</c:otherwise>
		</c:choose> 
		
		<%@ include file="msgview.jsp"%> 
		
		<c:if test='${(msgLevel == 0)}'>
			<div id="insertTopLevelRepliesHere"></div>
		</c:if> 
		
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

	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("time.timeago").timeago();
		});
	</script>
	
	<c:set var="pageSize" value="<%=ForumConstants.DEFAULT_PAGE_SIZE%>" />
	<c:if test='${maxThreadUid > 0 && ! noMorePages}'>
		<div class="text-center">
			<c:set var="more">
				<lams:WebAppURL />learning/viewTopicNext.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}&pageLastId=${maxThreadUid}&size=${pageSize}
			</c:set>
			<a href="<c:out value="${more}"/>" class="btn btn-xs btn-default"><fmt:message key="label.show.more.messages" /></a>
		</div>
	</c:if>	
