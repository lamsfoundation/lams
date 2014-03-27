<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="KALTURA_SERVER"><%=Configuration.get(ConfigurationKeys.KALTURA_SERVER)%></c:set>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="USER_SECRET"><%=Configuration.get(ConfigurationKeys.KALTURA_USER_SECRET)%></c:set>
<c:set var="KCW_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KCW_UI_CONF_ID)%></c:set>
<c:set var="KDP_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KDP_UI_CONF_ID)%></c:set>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="kaltura" value="${sessionMap.kaltura}" />
<c:set var="item" value="${sessionMap.item}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

<link href="<lams:LAMSURL/>css/jquery.jRating.css" rel="stylesheet" type="text/css" media="screen"/>

<style media="screen,projection" type="text/css">
	#player-block {margin-top: 25px; clear: both; display: inline-block;  position: relative; width: 85%;}
	#player-wrap {clear: both; margin-bottom: 20px; background: black; position: absolute; top: 0; bottom: 0; left: 0; right: 0;}
	#dummy {padding-top: 75%;} /* 4:3 aspect ratio. I.e. padding-top:75% gives the box of the dummy element a height that is 75% of the aforementioned width*/
	#player-bottombar {width: 85%;}
	.rating-stars-div {margin-bottom: 10px; min-height: 50px; padding-right: 10px;}
	.rating-stars-caption { text-align: left; padding-left: 20px; }
	.rating-stars-disabled-small {padding: 5px 5px;}
	#accordion { padding: 65px 0 20px;}
	#comments-area {margin-top:10px; margin-bottom: 30px;}
	#comments-table {border-spacing: 10px; margin-bottom: 0; margin-top: 0;}
	.comment-by {font-size: .9166em; color: #8E8E8E; margin-top: 3px;}
	.comment-by-text {color: #8E8E8E;}
	.item-hidden {background: #FFB3B3!important;}

	#player-sidebar {width: 100%; }
	#player-sidebar ul {list-style: none outside none; }
	#player-sidebar ul li {padding: 5px 0 15px 5px; clear: both; margin-bottom: 20px; }
	#player-sidebar ul li a .review-item-link { background: transparent; border-bottom: none; -moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; -webkit-transition: background .15s ease-in-out; position: relative; overflow: hidden; color: #333; zoom: 1;}
	.thumb-wrap {float: left; margin: 0 8px 0 0; position: relative; display: inline-block;	background: transparent; }
	.duration {padding: 0 4px; font-weight: bold; font-size: 11px; moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px; background-color: black; color: white!important; height: 14px; line-height: 14px; opacity: 0.75; display: inline-block; vertical-align: top; zoom: 1; right: 2px; bottom: 2px; position: absolute;}
	.thumb-title {width: auto; font-size: 18px; font-weight: bold; line-height: 18px; max-height: 36px; color: #333; display: block; margin-bottom: 4px; overflow: hidden; cursor: pointer;}
	.thumb-stat {display: block; font-size: 14px; color: #666; line-height: 1.4em; max-height: 1.4em; height: 1.4em; white-space: nowrap; overflow: hidden;}
	.thumb-text {color: #666;}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "<lams:LAMSURL/>images/css/";
</script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jRating.js"></script>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="http://cdnbakmi.kaltura.com/html5/html5lib/v1.6.10.4/mwEmbedLoader.php"></script>

<script type="text/javascript">
	<!--
	//Kaltura settings
	//kaltura server 
	var KALTURA_SERVER = "${KALTURA_SERVER}";
	//your actual parner id
	var PARTNER_ID = "${PARTNER_ID}";
	//your actual subparner id
	var SUB_PARTNER_ID = "${SUB_PARTNER_ID}";
	//your actual user secret
	var USER_SECRET = "${USER_SECRET}";
	//ui_conf_id of Kaltura Contribution Wizard(KCW)
	var KCW_UI_CONF_ID = "${KCW_UI_CONF_ID}";
	//ui_conf_id of Kaltura Dynamic Player(KDP)
	var KDP_UI_CONF_ID = "${KDP_UI_CONF_ID}";
	
	//enable HTML5 support
	mw.setConfig("Kaltura.ServiceUrl" , KALTURA_SERVER );
	mw.setConfig("Kaltura.CdnUrl" , KALTURA_SERVER );
	mw.setConfig("Kaltura.ServiceBase", "/api_v3/index.php?service=");
	mw.setConfig("EmbedPlayer.EnableIframeApi", true );
	mw.setConfig("EmbedPlayer.UseFlashOnAndroid", false );
	mw.setConfig("Kaltura.UseAppleAdaptive", false );
	mw.setConfig("EmbedPlayer.AttributionButton", false );
	mw.setConfig("EmbedPlayer.OverlayControls", false ); 
	
	var params = {
		allowscriptaccess: "always",
		allownetworking: "all",
		allowfullscreen: "true",
		wmode: "opaque"
	};	
	var flashVars = {
		entryId:  "${item.entryId}"
	};	
	swfobject.embedSWF(KALTURA_SERVER + '/kwidget/wid/_' + PARTNER_ID + '/uiconf_id/' + KDP_UI_CONF_ID, "kplayer", "100%", "100%", "9.0.0", "includes/expressInstall.swf", flashVars, params);
	
	function finishActivity(){
		$('#finishButton').prop('disabled', true);
		document.location.href ='<c:url value="/learning.do"/>?dispatch=finishActivity&sessionMapID=${sessionMapID}';
		return false;
	}
	
	function continueReflect(){
		document.location.href='<c:url value="/learning.do"/>?dispatch=newReflection&sessionMapID=${sessionMapID}';
	}
	
	function addNewComment(comment){
		$("#comments-area").load(
			"<c:url value="/learning.do"/>",
			{
				dispatch: "commentItem",
				itemUid: ${item.uid}, 
				comment: comment,
				sessionMapID: "${sessionMapID}",
				reqID: (new Date()).getTime()
			},
			function() {
				$(this).trigger( "create" );
			}
			
		);
	}
	
	$(document).bind('pageinit', function(){
		$(".rating-stars").jRating({
		    phpPath : "<c:url value='/learning.do'/>?dispatch=rateItem&sessionMapID=${sessionMapID}",
		    rateMax : 5,
		    decimalLength : 1,
			onSuccess : function(data, itemUid){
			    $("#averageRating" + itemUid).html(data.averageRating);
			    $("#numberOfVotes" + itemUid).html(data.numberOfVotes);
			},
			onError : function(){
			    jError('Error submitting rating, please retry');
			}
		});
	    $(".rating-stars-disabled").jRating({
	    	rateMax : 5,
	    	isDisabled : true
		});
	    $(".rating-stars-disabled-small").jRating({
	    	rateMax : 5,
	    	type: 'small',
	    	isDisabled : true
		});
	
	});
	
	-->
</script>

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<c:out value="${kaltura.title}" escapeXml="true"/>
	</h1>
</div>

<div data-role="content">

	<p>
		<c:out value="${kaltura.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${not empty sessionMap.submissionDeadline}">
		 <div class="info">
		 	<fmt:message key="authoring.info.teacher.set.restriction" >
		 		<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
		 	</fmt:message>
		 </div>
	</c:if>

	<c:if test="${kaltura.lockOnFinished and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${sessionMap.userFinished}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	
	<div id="player-block">
		<div id="dummy"></div>
	
		<div id="player-wrap">
		    
			<div id="kplayer">
				<object height="100%" width="100%">
					<embed height="100%" type="application/x-shockwave-flash" width="100%" src="#"></embed>
				</object>
			</div>
				
		</div>
		

	</div>
	
	<div id="player-bottombar">
	
		<!--  Rating stars -->
	
		<c:if test="${kaltura.allowRatings && (item.uid != -1)}">
			<%@ include file="/pages/learning/mobile/ratingStars.jsp"%>
		</c:if>
	</div>
	    	
    <%--Comments area----------------------------------------------%>	
    
    <div data-role="collapsible-set" id="accordion">

		<c:if test="${kaltura.allowComments && (item.uid != -1)}">
			<div data-role="collapsible" data-collapsed="false">
				<h3><fmt:message key="label.learning.comments" /></h3>
				    
			    <div id="comments-area">
			    	<%@ include file="/pages/learning/mobile/commentlist.jsp"%>
			    </div>
			</div>
		 </c:if>
		
		<c:if test="${not empty sessionMap.items}">
			<div data-role="collapsible" <c:if test="${!kaltura.allowComments || (item.uid == -1)}">data-collapsed="false"</c:if>>
				<h3><fmt:message key="label.list.of.videos" /></h3>
				
				<div id="player-sidebar">
				    <ul>
				    	<c:forEach var="previewItem" items="${sessionMap.items}" varStatus="status">
				    		<li id="previewItem${previewItem.uid}" <c:if test="${previewItem.hidden}"> class="item-hidden" </c:if>>
				    			<a href="<c:url value='/learning.do'/>?dispatch=viewItem&sessionMapID=${sessionMapID}&itemUid=${previewItem.uid}" class="review-item-link" data-ajax="false">
				    				<span class="thumb-wrap">
								        <img src="http://cdnbakmi.kaltura.com/p/${PARTNER_ID}/sp/${SUB_PARTNER_ID}/thumbnail/entry_id/${previewItem.entryId}/version/100001/width/150!/height/100!" height="100" width="150" alt="Video">
								        <em></em>
								        <span class="duration">
								        	<fmt:formatDate value="${previewItem.durationDate}" pattern="m:ss" />
										</span>
								    </span>
								    <span class="thumb-title"><c:out value="${previewItem.title}" escapeXml="true"/></span>
								    
								    <span class="thumb-stat">
								    	<c:choose>
											<c:when test="${empty previewItem.createdBy}">
												<c:set var="videoAuthor" >
													<fmt:message key="label.uploaded.by.instructor" />
												</c:set>
											</c:when>
											<c:otherwise>
												<c:set var="videoAuthor" >
													<c:out value="${previewItem.createdBy.firstName} ${previewItem.createdBy.lastName}" escapeXml="true"/>
												</c:set>
											</c:otherwise>
										</c:choose>
										
								        <fmt:message key="label.uploaded.by" >
								        	<fmt:param><c:out value="${videoAuthor}" escapeXml="true"/></fmt:param>
								        </fmt:message>
								    </span>
								    
								    <c:if test="${kaltura.allowRatings}">
									    <span class="thumb-stat">
									    	<span style="float: left;"><fmt:message key="label.rating" /></span>
									        <div class="rating-stars-disabled-small" data-average="${previewItem.averageRatingDto.rating}" data-id="${previewItem.uid}"></div>
										</span>
									</c:if>
								    
								    <c:if test="${kaltura.allowComments && (not empty previewItem.groupComments)}">
									    <span class="thumb-stat">
									        <fmt:message key="label.number.of.comments" >
									        	<fmt:param>${fn:length(previewItem.groupComments)}</fmt:param>
									    	</fmt:message>
										</span>
									</c:if>
								</a>
								
				    		</li>
				    	</c:forEach>
					</ul>
				</div>
				
			</div>
		</c:if>
	</div>
    	


	<%--Reflection--------------------------------------------------%>

	<c:if test="${sessionMap.userFinished && sessionMap.reflectOn && !sessionMap.isGroupMonitoring}">
		<div class="small-space-top">
			<h2>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</h2>

			<c:choose>
				<c:when test="${empty sessionMap.reflectEntry}">
					<p>
						<em> 
							<fmt:message key="message.no.reflection.available" />
						</em>
					</p>
				</c:when>
				<c:otherwise>
					<p>
						<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
					</p>
				</c:otherwise>
			</c:choose>

			<c:if test="${mode != 'teacher'}">
				<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
					<fmt:message key="label.edit" />
				</html:button>
			</c:if>
		</div>
	</c:if>
</div>

<div data-role="footer" data-theme="b" class="ui-bar">
	<c:choose>
		<c:when test="${mode != 'teacher'}">
			<span class="ui-finishbtn-right">
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button name="FinishButton" onclick="return continueReflect()" data-icon="arrow-r" data-theme="b">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<a href="#nogo" id="finishButton" onclick="return finishActivity()" data-role="button" data-icon="arrow-r" data-theme="b">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.activityPosition.last}">
				 						<fmt:message key="button.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="button.finish" />
				 					</c:otherwise>
				 				</c:choose>
				 			</span>
						</a>
					</c:otherwise>
				</c:choose>
			</span>
		</c:when>
	
		<c:otherwise>
			<h2>&nbsp;</h2>
		</c:otherwise>
	</c:choose>
</div>
