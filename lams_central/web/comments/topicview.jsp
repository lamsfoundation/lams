<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>

<c:set var="maxThreadUid" value="0"/>
<c:set var="messageTablename" value=""/>
<c:set var="indent" value="30"/>

<c:set var="show"><fmt:message key="label.show" /></c:set>
<c:set var="hide"><fmt:message key="label.hide" /></c:set>
<c:set var="prompt"><fmt:message key="label.showhide.prompt" /></c:set>
<c:set var="tableCommand">expandable:true,initialState:'expanded',
	expanderTemplate:'<a href=\"#\">&nbsp;&nbsp;&nbsp;&nbsp;${prompt}</a>',
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
	$(".comment").click(function (e) {
		e.stopPropagation();
	});
	
	
	function createReply(commentUid, url) {
		if ( document.getElementById('reply') ) {
			alert('<fmt:message key="message.complete.or.cancel.reply"/>');
		} else {
			// set up the new reply area
			var replyDiv = document.createElement("div");
			replyDiv.id = 'reply';

			var parentDiv = document.getElementById('msg'+commentUid);
			parentDiv.appendChild(replyDiv);
			
			$(replyDiv).load(url, function() {
				resizeIframe();
			});
		}
	}
	
	
	function createEdit(commentUid, url) {
		if ( document.getElementById('edit') ) {
			alert('<fmt:message key="message.complete.or.cancel.edit"/>');
		} else {
			// set up the new edit area
			// set up the new reply area
			var editDiv = document.createElement("div");
			editDiv.id = 'edit';

			var parentDiv = document.getElementById('msg'+commentUid);
			parentDiv.appendChild(editDiv);

			$(editDiv).load(url, function() {
				resizeIframe();
			});
		}
	}

	
	function hideEntry(commentUid, url) {
		$.ajax({ // create an AJAX call...
		    type: 'GET', 
		    url: url
		})
	    .done(function (response) {
			reloadThread(response);
	    });
	}

	function updateLike(commentUid, url, incValue) {
		$.ajax({ // create an AJAX call...
		    type: 'GET', 
		    url: url
		})
	    .done(function (response) {
    		var serverCommentUid = response.commentUid;
       		
    		if ( ! commentUid || serverCommentUid != commentUid ) {
				alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
  			} else if ( response.status ) {
  				var currentCount = $('#msglikeCount'+commentUid).html();
  				currentCount = +currentCount + incValue;
	       		$('#msglikeCount'+commentUid).html(currentCount);
			}
		});
	}
		
	function likeEntry(commentUid) {
		updateLike(commentUid,
			'<html:rewrite page="/comments/like.do"/>?sessionMapID=${sessionMapID}&commentUid='+commentUid,
			1);		
		$( '#msglikebutton'+commentUid ).removeClass( 'fa-thumbs-up fa-faded' ).addClass( 'fa-thumbs-o-up' );
		$( '#msglikebutton'+commentUid ).prop( 'onclick', null );
		<c:if test='${sessionMap.likeAndDislike}'> 
		$( '#msgdislikebutton'+commentUid ).css( "display", "none" );
		</c:if>
	}

	<c:if test='${sessionMap.likeAndDislike}'> 
	function dislikeEntry(commentUid) {
		updateLike(commentUid, 
			'<html:rewrite page="/comments/dislike.do"/>?sessionMapID=${sessionMapID}&commentUid='+commentUid,
			 -1);		
		$( '#msgdislikebutton'+commentUid ).removeClass( 'fa-thumbs-down fa-faded' ).addClass( 'fa-thumbs-o-down' );
		$( '#msgdislikebutton'+commentUid ).prop( 'onclick', null );
		$( '#msglikebutton'+commentUid ).css( "display", "none" );
	}
	</c:if>

</script>

<c:forEach var="commentDto" items="${commentThread}">
	<c:set var="msgLevel" value="${commentDto.level}" />
	<c:set var="hidden" value="${commentDto.comment.hideFlag}" />
	
	<c:if test='${(msgLevel <= 1)}'>
		<c:set var="maxThreadUid" value="${commentDto.comment.uid}"/>
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
		<c:set var="messageTablename" value="tree${commentDto.comment.uid}"/>
		<div id="thread${commentDto.comment.uid}">
		<table id="${messageTablename}">
		<tr data-tt-id="${commentDto.comment.uid}"><td>	
	</c:when>
	<c:otherwise>
		<tr data-tt-id="${commentDto.comment.uid}" data-tt-parent-id="${commentDto.comment.parent.uid}"><td>	
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

<c:if test='${maxThreadUid > 0 && ! noMorePages}'>
	<div class="float-right">
	<c:set var="more"><html:rewrite page="/comments/viewTopic.do?pageLastId=${maxThreadUid}&sessionMapID=${sessionMapID}" /></c:set>
	<a href="<c:out value="${more}"/>" class="button"><fmt:message key="label.show.more.messages" /></a>
	</div>
</c:if>
