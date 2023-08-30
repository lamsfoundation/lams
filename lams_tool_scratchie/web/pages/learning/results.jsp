<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<c:set var="title">
	${scratchie.title} / <fmt:message key='label.score' />
</c:set>
<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}">
	
	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/circle.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.custom.css" rel="stylesheet">
	<style type="text/css">
		#reflections-div {
			padding-bottom: 20px;
		}
	    
	    /* when item is editable - show pencil icon on hover */
	    .burning-question-text:hover +span+ i, /* when link is hovered select i */
		.burning-question-text + span:hover+ i, /* when space after link is hovered select i */
		.burning-question-text + span + i:hover { /* when icon is hovered select i */
		  visibility: visible;
		}
		.burning-question-text +span+ i { /* in all other case hide it */
		  visibility: hidden;
		}
		
		/* hide edit button background */
		div.btn.ui-inline-edit {
			background-color:rgba(0, 0, 0, 0) !important;
		}
		
		/* make cell borders less prominent */
		.ui-jqgrid .ui-jqgrid-bdiv tr.ui-row-ltr>td {
    		border-right-style: dotted;
		}
		.ui-jqgrid tr.jqfoot>td, .ui-jqgrid tr.jqgroup>td, .ui-jqgrid tr.jqgrow>td, .ui-jqgrid tr.ui-subgrid>td, .ui-jqgrid tr.ui-subtblcell>td {
	    	border-bottom-style: dotted;
		}
		
		/* links to burning questions */
		.scroll-down-to-bq {
			overflow:auto; 
			margin-top: -20px;
		}
		
		.item-score {
			font-weight: bold;
		}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);
		
		function likeEntry(scratchieItemUid, rowid, burningQuestionUid) {
			
			var jqGrid = $("#burningQuestions" + scratchieItemUid);
			var likeCell = jqGrid.jqGrid('getCell', rowid, 'like');
			var isLike = likeCell.includes( 'fa-regular' );

			if (isLike) {
				$.ajax({
				    url: '<c:url value="/learning/like.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {	       		
		    		if ( response.added ) {
		    			//update 'count' column
		  				var currentCount = eval(jqGrid.jqGrid('getCell', rowid, 'count'));
		  				jqGrid.jqGrid('setCell', rowid, 'count', currentCount + 1);
		  				
		  				//update 'like' column
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-regular", "fa-solid"));
		  				
					} else {
						alert("Error");
					}
				});
				
			} else {
				$.ajax({
				    url: '<c:url value="/learning/removeLike.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {
			    	if ( response.added ) {
			    		//update 'count' column
		  				var currentCount = eval(jqGrid.jqGrid('getCell', rowid, 'count'));
		  				jqGrid.jqGrid('setCell', rowid, 'count', currentCount - 1);
		  				
		  				//update 'like' column
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-solid", "fa-regular"));
			       		
		  			} else {
						alert("Error");
					}
				});
			}

		}
	
		$(document).ready(function(){
			
			<!-- Display burningQuestionItemDtos -->
			<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
				<c:set var="scratchieItem" value="${burningQuestionItemDto.scratchieItem}"/>
				<c:set var="scratchieItemUid" value="${scratchieItem.uid == null ? 0 : scratchieItem.uid}"/>

				jQuery("#burningQuestions${scratchieItemUid}").jqGrid({
					datatype: "local",
					rowNum: 10000,
					height: 'auto',
					autowidth: true,
					shrinkToFit: false,
					guiStyle: "bootstrap4",
					iconSet: 'fontAwesomeSolid',
				   	colNames:[
						'#',
						'isUserAuthor',
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.user.name' /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.burning.questions' /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.like' /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.count' /></spring:escapeBody>"
					],
				   	colModel:[
				   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
				   		{name:'isUserAuthor', width:0, hidden: true},
				   		{name:'groupName', index:'groupName', width:100, title: false},
				   		{name:'burningQuestion', index:'burningQuestion', width:501, edittype: 'textarea', title: false, editoptions:{rows:"8"},
					   		formatter:function(cellvalue, options, rowObject, event) {
					   			if (event == "edit") {
					   				cellvalue = cellvalue.replace(/\n/g, '\n<br>');
					   			}
					   			var item = $(this).jqGrid("getLocalRow", options.rowId);

					   			//when item is editable - show pencil icon on hover
								return ${isUserLeader} && eval(item.isUserAuthor) ? 
								   		"<span class='burning-question-text'>" +cellvalue + "</span><span>&nbsp;</span><i class='fa fa-pencil'></i>" 
								   		: cellvalue;
			   				},
			   				unformat:function(cellvalue, options, rowObject) {
			   					var text = $('<div>' + cellvalue + '</div>').text();
								return text.trim();
			   				},
				   			editable: function (options) {
				   	            var item = $(this).jqGrid("getLocalRow", options.rowid);
				   	            return ${isUserLeader} && eval(item.isUserAuthor);
				   	        }
						},
				   		{name:'like', index:'like', width:60, align: "center", 
					   		formatter:function(cellvalue, options, rowObject) {
								return cellvalue;
			   				}
						},
				   		{name:'count', index:'count', width:50, align:"right", title: false}
				   	],
                    caption: <c:choose>
								<%-- General burning question --%>
								<c:when test="${scratchieItemUid == 0}">"<c:out value='${scratchieItem.qbQuestion.name}' />"
								</c:when>
								<c:otherwise>
									<%-- Regular burning question --%>
									<c:choose>
										<%-- If we hide titles, we just display a link for "Question 1)" --%>
										<c:when test="${sessionMap.hideTitles}">
										 	"<a href='#questionTitle${i.count}' class='bq-title'><spring:escapeBody javaScriptEscape='true'><fmt:message key='label.question'/></spring:escapeBody>&nbsp;${i.count})</a>"
										</c:when>
										<%-- If we show titles, we display question number and then a link with question title --%>
										<c:otherwise>
											"${i.count}) <a href='#questionTitle${i.count}' class='bq-title'><c:out value='${scratchieItem.qbQuestion.name}' /></a>"
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose> 
							+ " <span class='small'>[${fn:length(burningQuestionItemDto.burningQuestionDtos)}]</span>",
                    editurl: '<c:url value="/learning/editBurningQuestion.do"/>?sessionId=${toolSessionID}&itemUid=${scratchieItemUid}',
	  				inlineEditing: { keys: true, defaultFocusField: "burningQuestion", focusField: "burningQuestion" },
	  				onSelectRow: function (rowid, status, e) {
	  	                var $self = $(this), 
	  	                	savedRow = $self.jqGrid("getGridParam", "savedRow");

	  	                if (savedRow.length > 0 && savedRow[0].id !== rowid) {
	  	                    $self.jqGrid("restoreRow", savedRow[0].id);
	  	                }

	  	                //edit row on its selection, unless "thumbs up" button is pressed
	  	                if (e.target.classList.contains("fa-thumbs-up") && e.target.classList.contains("fa-lg") 
	  		  	                && (e.target.classList.contains("fa-regular") || e.target.classList.contains("fa-solid"))) { 
	  	                	return;
		  	            } else {
		  	            	$self.jqGrid("editRow", rowid, { focusField: "burningQuestion" });

		  	            	//Modify event handler to save on blur
		  	            	var gridId = "#burningQuestions${scratchieItemUid}";
		  	              	$("textarea[id^='"+rowid+"_burningQuestion']", gridId).bind('blur',function(){
		  	                	$(gridId).saveRow(rowid);
		  	              	});
			  	        }
	  	            },
	  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
	  					var itemUid = jQuery("#list${summary.sessionId}").getCell(rowid, 'userId');
	  					return {itemUid:itemUid};
	  				}
				});
				
			    <c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}" varStatus="i">			    
			    		jQuery("#burningQuestions${scratchieItemUid}").addRowData(${i.index + 1}, {
			   			id:"${i.index + 1}",
			   			isUserAuthor:"${burningQuestionDto.userAuthor}",
			   	     	groupName:"${burningQuestionDto.sessionName}",
				   	    burningQuestion:"${burningQuestionDto.escapedBurningQuestion}",
				   	 	<c:choose>
				   			<c:when test="${!isUserLeader && burningQuestionDto.userLiked}">
				   				like:'<span class="fa-solid fa-thumbs-up fa-lg"></span>',
				   			</c:when>
					   		<c:when test="${!isUserLeader}">
				   				like:'',
				   			</c:when>
							<c:when test="${burningQuestionDto.userLiked}">
								like:'<span class="fa-solid fa-thumbs-up fa-lg" role="button" title="<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.unlike'/></spring:escapeBody>" aria-label="<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.unlike'/></spring:escapeBody>"' +
										'onclick="javascript:likeEntry(${scratchieItemUid}, ${i.index + 1}, ${burningQuestionDto.burningQuestion.uid});" />',
							</c:when>
							<c:otherwise>
								like:'<span class="fa-regular fa-thumbs-up fa-lg" role="button" title="<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.like'/></spring:escapeBody>" aria-label="<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.like'/></spring:escapeBody>"' +
										'onclick="javascript:likeEntry(${scratchieItemUid}, ${i.index + 1}, ${burningQuestionDto.burningQuestion.uid});" />',
							</c:otherwise>
						</c:choose>
				   	 	count:'${burningQuestionDto.likeCount}'
			   	   	});
		        </c:forEach>

		        jQuery("#burningQuestions${scratchieItemUid}").jqGrid('sortGrid','groupName', false, 'asc');
	        </c:forEach>
			
			<!-- Display reflection entries -->
			jQuery("#reflections").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				guiStyle: "bootstrap4",
				iconSet: 'fontAwesomeSolid',
			   	colNames:[
				   	'#',
					"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.user.name' /></spring:escapeBody>",
					"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.learners.feedback' /></spring:escapeBody>"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:140},
			   		{name:'feedback', index:'feedback', width:568}
			   	],
			   	caption: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.other.groups' /></spring:escapeBody>"
			});
		    <c:forEach var="reflectDTO" items="${reflections}" varStatus="i">
		    		jQuery("#reflections").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${reflectDTO.groupName}",
			   	    feedback:"<lams:out value='${reflectDTO.reflection}' escapeHtml='true' />"
		   	   	});
		    </c:forEach>
		    
			//jqgrid autowidth (http://stackoverflow.com/a/1610197)
			$(window).bind('resize', function() {
				var grid;
			    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
			    	grid.each(function(index) {
			        	var gridId = $(this).attr('id');
			        	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
			        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
			    	});
			    }
			});
			
			// trigger the resize when the window first opens so that the grid uses all the space available.
			setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

			//hide links to burning questions that were not created by the user
			$(".scroll-down-to-bq a").each(function()  {
				var itemUid = $(this).data("item-uid");
				if ( $( "#burningQuestions" + itemUid ).length == 0) {
					$(this).parent().hide();
				}
			});

			//handler for expand/collapse all button
			$("#toggle-burning-questions-button").click(function() {
				var isExpanded = eval($(this).data("bs-expanded"));
				
				//fire the actual buttons so burning questions can be closed/expanded
				$(".ui-jqgrid-titlebar-close").each(function() {
					if (!isExpanded && $('span', this).hasClass('fa-chevron-circle-down')
							|| isExpanded && $('span', this).hasClass('fa-chevron-circle-up')) {
						this.click();
					}
				});

				//change button label
				var newButtonLabel = isExpanded ? "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.expand.all' /></spring:escapeBody>" : "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.collapse.all' /></spring:escapeBody>";
				$(".d-none.d-sm-block", $(this)).text(newButtonLabel);

				//change button icon
				if (isExpanded) {
					$(".fa", $(this)).removeClass("fa-square-minus").addClass("fa-circle-plus");
				} else {
					$(".fa", $(this)).removeClass("fa-circle-plus").addClass("fa-square-minus");
				}

				//change button's data-bs-expanded attribute
				$(this).data("bs-expanded", !isExpanded);
			});
		    
		})
	
		function finishSession() {
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function refreshToBurningQuestions() {
			// setting href does not navigate if url contains #, we still need a reload
			location.hash = '#burning-questions-container';
			location.reload();
			return false;
		}
    </script>

	<div class="container-lg">
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert5 id="submissionDeadline">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>

		<lams:errors5/>

		<lams:Alert5 id="score" type="info" close="false">
			<fmt:message key="label.you.ve.got">
				<fmt:param>${score}</fmt:param>
				<fmt:param>${scorePercentage}</fmt:param>
			</fmt:message>
		</lams:Alert5>

		<c:if test="${showResults}">
			<%@ include file="scratchies.jsp"%>
		</c:if>
		
		<!-- Display burningQuestionItemDtos -->
		<c:if test="${sessionMap.isBurningQuestionsEnabled and not empty burningQuestionItemDtos}">
            <div class="d-flex justify-content-end" id="burning-questions-buttons">
	            <button type="button" class="btn btn-sm btn-secondary me-2 d-flex align-items-center" onclick="return refreshToBurningQuestions()">
	            	<i class="fa-solid fa-refresh me-1"></i> 
	            	<span class="d-none d-sm-block">
	            		<fmt:message key="label.refresh" />
	            	</span>
	            </button>
	
	            <button type="button" id="toggle-burning-questions-button" class="btn btn-sm btn-secondary me-2 " data-bs-expanded="true" >
	            	<span class="d-none d-sm-block">
	            		<fmt:message key="label.collapse.all" />
	            	</span>
	            </button>
            </div>
            
			<div id="burning-questions-container">
				<div class="lead mb-2">
					<fmt:message key="label.burning.questions" />
				</div>

				<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
					<c:if test="${not empty burningQuestionItemDto.burningQuestionDtos}">
						<c:set var="scratchieItemUid" value="${empty burningQuestionItemDto.scratchieItem.uid ? 0 : burningQuestionItemDto.scratchieItem.uid}"/>
						<div class="burning-question-dto mb-2">
							<table id="burningQuestions${scratchieItemUid}" class="scroll" cellpadding="0" cellspacing="0"></table>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</c:if>

		<!-- Display reflections -->
		<c:if test="${sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${(mode != 'teacher') && isUserLeader}"
				isReflectionsJqGridEnabled="${fn:length(reflections) > 0}" />
		</c:if>

		<!-- Display finish buttons -->
		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<button name="finishButton" id="finishButton" class="btn btn-primary btn-disable-on-submit na" type="button">
					<c:choose>
						<c:when test="${sessionMap.isLastActivity}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</button>
			</div>
		</c:if>
		
	</div>
</lams:PageLearner>