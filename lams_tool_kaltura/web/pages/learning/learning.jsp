<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>  
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	</lams:head>
	<body class="stripes">
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
		<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		
		<style media="screen,projection" type="text/css">
			
			#container{width: 100%; position: relative; display: inline-block; margin-top: 25px;}
			#player-block {  clear: both; position: absolute; top: 0; bottom: 0; left: 0; right: 0;}
			#dummy {padding-top: 82%; /* aspect ratio */}
			#player-wrap {width: 60%; height: 100%; clear: both; margin-bottom: 20px; background: black;}
			#player-bottombar {height:50px; margin-top: 15px; margin-bottom: 10px;}
			
			#comment-textarea textarea {
				margin-bottom: 4px;
			}
			.rating-stars-div {min-height: 50px; padding-right: 0;}
			.rating-stars-caption { text-align: left; padding-left: 30px; }
			.rating-stars-disabled-small {padding: 2px 5px;}
			#add-new-item {float: left; margin-top: 2px; margin-bottom: 20px;}
			.comment-by {font-size: .9166em; color: #8E8E8E; margin-top: 3px;}
			.comment-by-text {color: #8E8E8E;}
			.item-hidden {background: #FFB3B3!important;}
		
			#player-sidebar { height: 100%; margin-top: 25px;}
			#player-sidebar ul {overflow: auto; clear: both; position: relative; list-style: none outside none; margin-left: 15px; margin-right: 0; min-width: 242px; height: 100%;}
			#player-sidebar ul li:hover {background-color:#D0D0D0;cursor:pointer}
			#player-sidebar ul li:hover .jRatingColor, #player-sidebar ul li:hover .jStar, .item-hidden .jRatingColor, .item-hidden .jStar {opacity: 0.3;}
			#player-sidebar ul li { overflow: hidden; padding: 5px 0 5px 5px; clear: both; margin-bottom: 10px; list-style: none outside none;}
			#player-sidebar ul li a .review-item-link { background: transparent; border-bottom: none; -moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; -webkit-transition: background .15s ease-in-out; position: relative; overflow: hidden; color: #333; zoom: 1;}
			.thumb-wrap {float: left; margin: 0 8px 0 0; position: relative; display: inline-block;	background: transparent; }
			.duration {padding: 0 4px; font-weight: bold; font-size: 11px; moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px; background-color: black; color: white!important; height: 14px; line-height: 14px; opacity: 0.75; display: inline-block; vertical-align: top; zoom: 1; right: 2px; bottom: 2px; position: absolute;}
			.thumb-title {width: auto; font-size: 12px; font-weight: bold; line-height: 15px; max-height: 30px; color: #333; display: block; margin-bottom: 4px; overflow: hidden; cursor: pointer;}
			.thumb-stat {display: block; font-size: .9166em; color: #666; line-height: 1.4em; max-height: 1.4em; height: 1.4em; white-space: nowrap; overflow: hidden;}
			.thumb-text {color: #666;}
		</style>	
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/instantedit.js"></script>
		<script type="text/javascript">
			//var for jquery.jRating.js
			var pathToImageFolder = "<lams:LAMSURL/>images/css/";
			//var for instantedit.js
			var urlBase = '<c:url value="/monitoring.do"/>';
		</script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
		<script type="text/javascript" src="http://cdnbakmi.kaltura.com/html5/html5lib/v1.6.10.4/mwEmbedLoader.php"></script>
		
		<script type="text/javascript">
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
				document.location.href ='<c:url value="/learning/finishActivity.do"/>?sessionMapID=${sessionMapID}';
				return false;
			}
		
			function continueReflect(){
				document.location.href='<c:url value="/learning/newReflection.do"/>?sessionMapID=${sessionMapID}';
			}
			
			function addNewComment(comment){
				$("#comments-area").load(
					"<c:url value="/learning/commentItem.do"/>",
					{
						itemUid: ${item.uid}, 
						comment: comment,
						sessionMapID: "${sessionMapID}"
					}
				);
			}
			
			function hideItem(itemUid, toHide){
				$.post(
					"<c:url value="/monitoring/setItemVisibility.do"/>",
					{
						itemUid: itemUid, 
						isHiding: toHide
					},
					function(data) {
						
						//replace item-hide span
						var itemHideText = '<span class="pull-right text-right item-hide-management">';
						if (toHide) {
							$("#previewItem" + itemUid).addClass("item-hidden");
							itemHideText += '<div class="thumb-text"><fmt:message key="label.video.is.hidden" /></div>' +
												'<a href="#nogo" onclick="return hideItem(' + itemUid + ', false);">' +
													'<fmt:message key="label.unhide" />' +
												'</a>';
						} else {
							$("#previewItem" + itemUid).removeClass("item-hidden");
							itemHideText += '<a href="#nogo" onclick="return hideItem(' + itemUid + ', true);"><fmt:message key="label.hide" /></a>';
						}
						itemHideText += '</span>';
						$("#previewItem" + itemUid + " .item-hide-management").replaceWith(itemHideText);
					}
				);
				
				return false;
			}
			
			function hideComment(commentUid, toHide){
				$.post(
					"<c:url value="/monitoring/setCommentVisibility.do"/>",
					{
						commentUid: commentUid, 
						isHiding: toHide
					},
					function(data) {
						
						//replace comment-hide span
						var commentHideText = '<div class="float-right comment-hide-management">';
						if (toHide) {
							$("#comment" + commentUid).addClass("item-hidden");
							commentHideText += '<span class="comment-by-text"><fmt:message key="label.comment.is.hidden" /></span>' +
												'<a href="#nogo" onclick="return hideComment(' + commentUid + ', false);">' +
													'<fmt:message key="label.unhide" />' +
												'</a>';
						} else {
							$("#comment" + commentUid).removeClass("item-hidden");
							commentHideText += '<a href="#nogo" onclick="return hideComment(' + commentUid + ', true);"><fmt:message key="label.hide" /></a>';
						}
						commentHideText += '</div>';
						$("#comment" + commentUid + " .comment-hide-management").replaceWith(commentHideText);
					}
				);
				
				return false;
			}
		
			$(document).ready(function(){
				$(".rating-stars").jRating({
				    phpPath : "<c:url value='/learning/rateItem.do'/>?sessionMapID=${sessionMapID}",
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
			    
			    //edit mark 
				setVarsForm("dispatch=markItem&itemUid=${item.uid}");
				$('#editItemMark').click(function() {
					editBox(document.getElementById( "itemMark" ));
				});
		
			});
		
		</script>
		
		<lams:Page type="learner" title="${kaltura.title}">
		
			<div class="panel">
				<c:out value="${kaltura.instructions}" escapeXml="false"/>
			</div>
		
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<lams:Alert id="submission-deadline" type="info" close="true">
				 	<fmt:message key="authoring.info.teacher.set.restriction" >
				 		<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				 	</fmt:message>
				</lams:Alert>
			</c:if>
		
			<c:if test="${kaltura.lockOnFinished and mode == 'learner'}">
				<lams:Alert type="danger" id="lock-on-finish" close="false">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</lams:Alert>
			</c:if>
			
			<div class="col-xs-12 col-sm-7">
					
				<div id="container">	
					<div id="dummy"></div>
			
					<div id="player-block" >
					    
						<div id="kplayer">
							<object height="100%" width="100%">
								<embed height="100%" type="application/x-shockwave-flash" width="100%" src="#"></embed>
							</object>
						</div>
							
					</div>
				</div>
					
				<div id="player-bottombar">
					
					<%--"Check for new" and "Add new image" buttons---------------%>
				
					<div id="add-new-item">
						<c:if test="${sessionMap.isAllowUpload && (not finishedLock)}">
							<a href="<c:url value='/pages/learning/uploaditem.jsp'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" class="btn btn-sm btn-default thickbox">  
								<fmt:message key="label.learning.add.new.image" />
							</a>
						</c:if>
						
						<c:if test="${sessionMap.isGroupMonitoring}">
							<fmt:message key="label.mark" />
							 
							<c:choose>
								<c:when test="${item.mark == null}">
									<c:set var="itemMark" value="0"/>	
								</c:when>
								<c:otherwise>
									<c:set var="itemMark" value="${item.mark}"/>			
								</c:otherwise>
							</c:choose>
							<span id="itemMark">${itemMark}</span>
							
							<sup><a href="javascript:;" id="editItemMark"><fmt:message key="label.mark.edit" /></a></sup>
						</c:if>
					</div>
				
					<!--  Rating stars -->
				
					<c:if test="${kaltura.allowRatings && (item.uid != -1)}">
						<%@ include file="/pages/learning/ratingStars.jsp"%>
					</c:if>
				</div>
			    	
			    <%--Comments area----------------------------------------------%>	
			    	
			    <c:if test="${kaltura.allowComments && (item.uid != -1)}">
				    <div id="comments-area" class="clearfix">
				    	<%@ include file="/pages/learning/commentlist.jsp"%>
				    </div>
			    </c:if>
					    
			</div>
				
			<div id="player-sidebar" class="col-xs-12 col-sm-5">
				<ul>
					<c:forEach var="previewItem" items="${sessionMap.items}" varStatus="status">
						<li id="previewItem${previewItem.uid}" <c:if test="${previewItem.hidden}"> class="item-hidden" </c:if>>
							<a href="<c:url value='/learning/viewItem.do'/>?sessionMapID=${sessionMapID}&itemUid=${previewItem.uid}" class="review-item-link">
								<span class="thumb-wrap">
									<img src="http://cdnbakmi.kaltura.com/p/${PARTNER_ID}/sp/${SUB_PARTNER_ID}/thumbnail/entry_id/${previewItem.entryId}/width/100!/height/68!" height="68" width="100" alt="Video">
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
								    	<span class="pull-left"><fmt:message key="label.rating" /></span>
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
									
							<c:if test="${sessionMap.isGroupMonitoring}">
							    <span class="pull-right text-right item-hide-management">
								   	<c:choose>
										<c:when test="${previewItem.hidden}">
											<div class="thumb-text">
												<fmt:message key="label.video.is.hidden" />
											</div>
													
											<a href="#nogo" onclick="return hideItem(${previewItem.uid}, false);">
								       			<fmt:message key="label.unhide" />
							    			</a>
										</c:when>
										<c:otherwise>
											<a href="#nogo" onclick="return hideItem(${previewItem.uid}, true);">
								       			<fmt:message key="label.hide" />
								   			</a>
										</c:otherwise>
									</c:choose>
								</span>							
							</c:if>
									
					   	</li>
				 	</c:forEach>
				</ul>
			</div>	
		
			<%--Reflection--------------------------------------------------%>
		
			<c:if test="${sessionMap.userFinished && sessionMap.reflectOn && !sessionMap.isGroupMonitoring}">
				<div class="col-xs-12 col-sm-12 ">
					<div class="panel panel-default voffset10">
						<div class="panel-heading panel-title">
							<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
						</div>
			
						<div class="panel-body">
							<c:choose>
								<c:when test="${empty sessionMap.reflectEntry}">
									<em> 
										<fmt:message key="message.no.reflection.available" />
									</em>
								</c:when>
								<c:otherwise>
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</c:otherwise>
							</c:choose>
						</div>
			
						<c:if test="${mode != 'teacher'}">
							<button name="FinishButton" onclick="return continueReflect()" class="btn btn-default pull-left">
								<fmt:message key="label.edit" />
							</button>
						</c:if>
					</div>
				</div>
			</c:if>
			
			<%--Bottom buttons-------------------------------------------%>
		
			<c:if test="${mode != 'teacher'}">
				<div class="voffset10 pull-right" style="clear:both;">
					<c:choose>
						<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
							<button name="FinishButton" onclick="return continueReflect()" class="btn btn-primary" >
								<fmt:message key="label.continue" />
							</button>
						</c:when>
						<c:otherwise>
							<a href="#nogo" name="FinishButton" id="finishButton"	onclick="return finishActivity()" class="btn btn-primary pull-right" >
								<span class="na">
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
				</div>
			</c:if>
			
		</lams:Page>		
	</body>
</lams:html>



