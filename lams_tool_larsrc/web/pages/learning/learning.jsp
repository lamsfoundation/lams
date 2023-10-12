<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
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
		
<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<lams:PageLearner title="${resource.title}" toolSessionID="${toolSessionID}" >

	<lams:css suffix="jquery.jRating"/>
	<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
	<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />
	<style media="screen,projection" type="text/css">
		.item-card {
			margin-bottom: 20px;
		}
		
		.item-content {
			padding: 5px;
		}
		
		.embedded-title {
			clear: both;
			font-weight: 500;
			font-size: larger;
		}
		
		.embedded-description {
			padding: 0.5em;
		}
		
		.embedded-file {
			text-align: center;
			margin: auto;
		}
		
		.embedded-file img {
			max-width: 800px;
		}
		
		.embedded-file video {
			width: 100%;
		}
		
		.embedded-file embed {
			width: 100%;
			min-height: 500px;
		}
		
		.embedded-content iframe {
			border: 0;
			width: 100%;
			height: 100%;
		}
		
		.delete-item-button {
			margin-left: 5px;
			cursor: pointer;
		}
		
		.commentFrame {
			padding: 10px;
		}
		
		.activity-bottom-buttons {
			clear: both;
		}
		
		#addresource {
		    clear: both;
		}
	</style>

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
				MAX_RATES = MAX_RATINGS_FOR_ITEM = MIN_RATES = COUNT_RATED_ITEMS = 0, // no restrictions
				COMMENTS_MIN_WORDS_LIMIT = 0, // comments not used,
				COMMENT_TEXTAREA_TIP_LABEL = WARN_COMMENTS_IS_BLANK_LABEL = WARN_MIN_NUMBER_WORDS_LABEL = '',
				AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
				YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
				ALLOW_RERATE = true,
				SESSION_ID = ${toolSessionID};
		</script>
		<lams:JSImport src="includes/javascript/rating.js" />
		<script src="${lams}includes/javascript/jquery.jRating.js"></script>
	</c:if>
	<lams:JSImport src="includes/javascript/rsrccommon.js" relative="true" />
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
						if (${resource.miniViewResourceNumber} > 0 && itemsComplete >= ${resource.miniViewResourceNumber}) {
							checkNew();
							return;
						}

						let heading = $('#heading' + itemUid);
						$('.complete-item-button', heading).remove();
						$('.icon-complete', heading).removeClass('d-none');
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

			/**
			 Preview for adding learner URL
			 **/
			// there is no item ID yet, so just use 0
			function iframelyCallback0(response) {
				if (!response || !response.html) {
					$('#addresource #preview-panel').addClass('d-none');
					return;
				}

				if (response.title && $('#addresource  #title').val().trim() == '') {
					$('#title').val(response.title);
				}

				iframelyCallback(0, response);
				$('#addresource #preview-panel').removeClass('d-none');
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
				if(confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='${delConfirmMsgKey}'/></spring:escapeBody>")){
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
	
	<div id="container-main">

		<!--  Warnings -->
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher' and (resource.allowAddFiles or resource.allowAddUrls) }">
			<lams:Alert5 type="danger" id="warn-lock" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<c:if test="${resource.miniViewResourceNumber > 0}">
			<lams:Alert5 type="info" id="warn-numResources" close="false">
				${resource.miniViewNumberStr}
			</lams:Alert5>
		</c:if>

		<lams:errors/>

		<!--  Instructions -->
		<div id="instructions" class="instructions">
			<c:out value="${resource.instructions}" escapeXml="false"/>
		</div>

		<!-- Resources to View -->
		<div class="card-subheader mb-3">
			<fmt:message key="label.resoruce.to.review" />
		</div>

		<c:forEach var="item" items="${sessionMap.resourceList}">
			<div class="item-card card lcard">
				<div class="card-header" id="heading${item.uid}">
			        	<span class="card-title collapsable-icon-left">
				        	<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${item.uid}"
							   aria-expanded="false" aria-controls="collapse${item.uid}" 
							>
								<c:out value="${item.title}" escapeXml="true"/>
								<c:if test="${!item.createByAuthor && item.createBy != null}">
									(<c:out value="${item.createBy.firstName} ${item.createBy.lastName}" escapeXml="true"/>)
								</c:if>
							</button>
						</span>
						
						<div class="float-end">
							<c:choose>
								<c:when test="${item.complete}">
									<i class="fa fa-check-circle text-bg-success icon-complete" style="font-size: 1.1em;color: green;" title='<fmt:message key="label.completed" />'></i>
								</c:when>
								<c:when test="${not finishedLock}">
									<i class="fa fa-check-circle icon-complete d-none" style="font-size: 1.5em;color: green;" title='<fmt:message key="label.completed" />'></i>
									<button type="button" onClick="javascript:completeItem(${item.uid})"
											class="complete-item-button btn btn-sm btn-success no-shadow">
										<i class="fa-solid fa-check" aria-hidden="true"></i>
										<fmt:message key='label.finish' />
									</button>
								</c:when>
							</c:choose>

							<c:if test="${not finishedLock && !item.createByAuthor && userID == item.createBy.userId}">
								<span role="button" class="fa fa-trash delete-item-button" style="color: red;"
								   title="<fmt:message key="label.delete" />"
								   aria-label="<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.delete" /></spring:escapeBody>
								   onclick="hideItem(${item.uid})"></span>
							</c:if>
						</div>
				</div>

				<div id="collapse${item.uid}" data-item-uid="${item.uid}" class="item-collapse card-collapse collapse"
					 role="tabpanel" aria-labelledby="heading${item.uid}"></div>
			</div>
		</c:forEach>

		<!-- Add a URL/File Form-->
		<div id="addresource">
		</div>

		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${mode != 'teacher'}"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>
		
		<div class="activity-bottom-buttons mt-5">
		
			<!-- Finish buttons -->
			<c:if test="${mode != 'teacher' && sessionMap.hasCompletedMinNumber}">
				<c:choose>
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button type="button" name="FinishButton" onclick="return continueReflect()" class="btn btn-primary na btn-disable-on-submit">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="submit" id="finishButton" class="btn btn-primary btn-disable-on-submit na">
								<c:choose>
									<c:when test="${sessionMap.isLastActivity}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</c:if>			

			<!--  Card button bar controlling refresh and adding items -->
			<div class="btn-group mx-2">
				<c:if test="${mode != 'teacher'}">
					<button onclick="javascript:return checkNew()" type="button" class="btn btn-secondary"> 
						<i class="fa fa-refresh"></i> 
						<fmt:message key="label.check.for.new" />
					</button>
				</c:if>
				<c:if test="${not finishedLock}">
					<c:choose>
						<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
							<button onclick="javascript:gotoURL()" type="button" class="btn btn-secondary"> 
								<i class="fa fa-plus"></i>
								<fmt:message key="label.authoring.basic.resource.url.input" />
							</button>
							<button onclick="javascript:gotoFile()" type="button" class="btn btn-secondary"> 
								<i class="fa fa-plus"></i>
								<fmt:message key="label.authoring.basic.resource.file.input" />
							</button>
						</c:when>
	
						<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
							<button onclick="javascript:gotoFile()" type="button" class="btn btn-secondary"> 
								<i class="fa fa-plus"></i>
								<fmt:message key="label.authoring.basic.resource.file.input" />
							</button>
						</c:when>
	
						<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
							<button onclick="javascript:gotoURL()" type="button" class="btn btn-secondary"> 
								<i class="fa fa-plus"></i>
								<fmt:message key="label.authoring.basic.resource.url.input" />
							</button>
						</c:when>
					</c:choose>
				</c:if>
			</div>
		</div>

	</div>
</lams:PageLearner>
