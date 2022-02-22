<!DOCTYPE html>

<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<lams:css suffix="jquery.jRating"/>
	<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
	<style media="screen,projection" type="text/css">
	 	.item-panel {
	 		margin: 15px;
	 	}
	 	
	 	.item-content {
	 		padding: 5px;
	 	}
	 	
	 	.item-instructions {
	 		margin-bottom: 15px;
	 		padding-bottom: 10px;
	 		border-bottom: 1px solid #ddd;
	 	}
	 	
	 	.embedded-title {
	 		clear: both;
	 		font-weight: 500;
	 		font-size: larger;
	 	}
	 	
	 	.embedded-description {
	 		padding: 0.5em;
	 	}
	 	
	 	.delete-item-button {
	 		margin-left: 5px;
	 		cursor: pointer;
	 	}
	 	
	 	.commentFrame {
	 		padding: 10px;
	 	}
	</style>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="resource" value="${sessionMap.resource}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:set var="userID"><lams:user property="userID"/></c:set>
	<c:set var="delConfirmMsgKey" value="del.confirmation" scope="request"/>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.validate.js"></script>

	<c:set var="language"><lams:user property="localeLanguage"/></c:set>
	<script src="${lams}includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>
	
	<c:if test="${sessionMap.rateItems}">
		<script>
			var pathToImageFolder = "${lams}images/css/",
				LAMS_URL = '${lams}',
				MAX_RATES = MAX_RATINGS_FOR_ITEM = MIN_RATES = COUNT_RATED_ITEMS = 0, // no restrictions
				COMMENTS_MIN_WORDS_LIMIT = 0, // comments not used,
				COMMENT_TEXTAREA_TIP_LABEL = WARN_COMMENTS_IS_BLANK_LABEL = WARN_MIN_NUMBER_WORDS_LABEL = '',
				AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
				YOUR_RATING_LABEL = 
					'<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
				ALLOW_RERATE = true,
				SESSION_ID = ${toolSessionID}; 
				
		</script>
		<script src="${lams}includes/javascript/rating.js"></script>
		<script src="${lams}includes/javascript/jquery.jRating.js"></script>
	</c:if>
	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<lams:JSImport src="includes/javascript/rsrcembed.js" relative="true" />
	<script>
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);

		let itemsComplete = ${itemsComplete};
	
		$(document).ready(function(){
			cancel();

			// show items only on Group expand
			$('.item-collapse').on('show.bs.collapse', function(){
				let collapse = $(this);
				if (collapse.is(':empty')) {
					let itemUid = collapse.data('item-uid');
					collapse.load("<c:url value="/itemReviewContent.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid="
								  + itemUid);
				}
			});
 		});

 		function submitResourceForm() {
			if ( $(this).valid() ) {
				//copy value from CKEditor to textarea before ajax submit
				$("textarea[id^='instructions']").each(function()  {
					var ckeditorData = CKEDITOR.instances[this.name].getData();
					//skip out empty values
					this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;		
				});
				
				$('.btn-disable-on-submit').prop('disabled', true);
				var formData = new FormData(this);
				showBusy('itemAttachmentArea');
			    $.ajax({ // create an AJAX call...
			        data: formData, 
			        processData: false, // tell jQuery not to process the data
			        contentType: false, // tell jQuery not to set contentType
			        type: $(this).attr('method'), // GET or POST
			        url: $(this).attr('action'), // the file to call
			        success: function (response) {
						$('#addresource').html(response);
			    	},
			    	error: function (jqXHR, textStatus, errorThrown ) {
			    		alert(textStatus+": "+errorThrown);
			    	},
			    	complete: function(response) {
			    		hideBusy('itemAttachmentArea');
			            $('.btn-disable-on-submit').prop('disabled', false);
					}
			    });
			}
			return false;
		}

		function completeItem(itemUid){
			$.ajax({
				dataType: 'text',
				cache: false,
				url:      "<c:url value="/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=" + itemUid,
				error: function(jqXHR, textStatus, errorThrown) {
					alert('Error while marking item as complete.\nStatus: ' + textStatus + '\nError: ' + errorThrown);
				},
				success:  function() {
					itemsComplete++;
					if (itemsComplete >= ${resource.miniViewResourceNumber}) {
						checkNew();
						return;
					}
					
					let heading = $('#heading' + itemUid);
					$('.complete-item-button', heading).remove();
					$('.icon-complete', heading).removeClass('hidden');
				}
			});
		}
		
	    function gotoURL(){
	    	var reqIDVar = new Date();
	   		var gurl = "<c:url value="/learning/addurl.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	   		$.ajaxSetup({ cache: true });
	        $("#addresource").load(gurl, function() {
	        	$("#itemType").val("1");
				$("#mode").val("${mode}");
				$("#sessionMapID").val("${sessionMapID}");
	        });
		}
	    
		function gotoFile(){
		    var reqIDVar = new Date();
		    var gurl = "<c:url value="/learning/addfile.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
		    $.ajaxSetup({ cache: true });
	        $("#addresource").load(gurl, function() {
	        	$("#itemType").val("2");
				$("#mode").val("${mode}");
				$("#sessionMapID").val("${sessionMapID}");
	        });
		}		
		
		function cancel(){
			$('.btn-disable-on-submit').prop('disabled', false);
			$("#addresource").html('');
		}		
		
		function checkNew(){
			document.location.href = '<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}&reqID='
									 + new Date().getTime();				
 		    return false;
		}
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		}
		
		function continueReflect(){
			$('.btn-disable-on-submit').prop('disabled', true);
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function hideItem(itemUid) {
			if(confirm("<fmt:message key='${delConfirmMsgKey}'/>")){
		        $.ajax({
		        	url: '<c:url value="/learning/hideItem.do"/>',
		            data: 'sessionMapID=${sessionMapID}&itemUid=' + itemUid,
		            cache : false,
					async: false,
		            success: function () {
		            	checkNew();
		            }
		       	});
			}
		}
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${resource.title}">
	
		<!--  Warnings -->
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher' and (resource.allowAddFiles or resource.allowAddUrls) }">
			<lams:Alert type="danger" id="warn-lock" close="false">
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
	
		<c:if test="${resource.miniViewResourceNumber > 0}">
			<lams:Alert type="info" id="warn-numResources" close="false">${resource.miniViewNumberStr}</lams:Alert>
		</c:if>

		<lams:errors/>

		<!--  Instructions -->
		<c:out value="${resource.instructions}" escapeXml="false"/>

		<!-- Resources to View -->
		<div class="panel panel-default">
			<div class="panel-heading panel-title">
				<fmt:message key="label.resoruce.to.review" />

				<!--  Panel button bar controlling refresh and adding items -->
				<div class="btn-group pull-right">
					<c:if test="${mode != 'teacher'}">
						<a href="#" onclick="javascript:return checkNew()" type="button" class="btn btn-xs btn-default">
						<i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.check.for.new" /></a>
					</c:if>
					<c:if test="${not finishedLock}">
						<c:choose>
							<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoURL()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.url.input" /></a>
								<a href="#" onclick="javascript:gotoFile()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.file.input" /></a>
							</c:when>
	
							<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoFile()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.file.input" /></a>
							</c:when>
	
							<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoURL()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.url.input" /></a>
							</c:when>
						</c:choose>
					</c:if>
				</div>
				<!--  End panel button bar -->
			</div> 

			<c:forEach var="item" items="${sessionMap.resourceList}">
			    <div class="item-panel panel panel-default" >
			        <div class="panel-heading" id="heading${item.uid}">
			        	<span class="panel-title collapsable-icon-left">
				        	<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${item.uid}" 
									aria-expanded="false" aria-controls="collapse${item.uid}" >
								<c:out value="${item.title}" escapeXml="true"/>
								<c:if test="${!item.createByAuthor && item.createBy != null}">
										(<c:out value="${item.createBy.firstName} ${item.createBy.lastName}" escapeXml="true"/>)
								</c:if>
							</a>
						</span>
						<div class="pull-right">
							<c:choose>
								<c:when test="${item.complete}">
									<i class="fa fa-check-circle icon-complete" style="font-size: 1.5em;color: green;" title='<fmt:message key="label.completed" />'></i>
								</c:when>
								<c:when test="${not finishedLock}">
									<i class="fa fa-check-circle icon-complete hidden" style="font-size: 1.5em;color: green;" title='<fmt:message key="label.completed" />'></i>
									<button type="button" onClick="javascript:completeItem(${item.uid})"
										   class="complete-item-button btn btn-xs btn-default">
										<fmt:message key='label.finish' />
									</button>
								</c:when>
							</c:choose>
							
							
							<c:if test="${not finishedLock && !item.createByAuthor && userID == item.createBy.userId}">
								<i class="fa fa-trash delete-item-button" style="color: red;"
								   title="<fmt:message key="label.delete" />"
								   onclick="hideItem(${item.uid})"></i>
							</c:if>
						</div>
			        </div>
			        
			        <div id="collapse${item.uid}" data-item-uid="${item.uid}" class="item-collapse panel-collapse collapse"
			        	 role="tabpanel" aria-labelledby="heading${item.uid}"></div>
				</div>
			</c:forEach>	
		</div>
		<!--  End Resources to View -->

		<!-- Add a URL/File Form-->
		<div id="addresource">
		</div>

		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="reflectionInstructions">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

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
						<button onclick="return continueReflect()" class="btn btn-sm btn-default voffset5 btn-disable-on-submit">
							<fmt:message key="label.edit" />
						</button>
					</c:if>
				</div>
			</div>
		</c:if>
		<!-- End Reflection -->

		<c:if test="${mode != 'teacher' && sessionMap.hasCompletedMinNumber}">
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button name="FinishButton"
							onclick="return continueReflect()" class="btn btn-default voffset5 pull-right btn-disable-on-submit">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="submit" id="finishButton" class="btn btn-primary btn-disable-on-submit voffset5 pull-right na">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>
</body>
</lams:html>
