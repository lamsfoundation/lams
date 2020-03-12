<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/circle.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL />/includes/font-awesome/css/font-awesome.min.css">
	<style type="text/css">
		#reflections-div {
			padding-bottom: 20px;
		}
		.burning-question-dto {
			padding-bottom: 5px; 
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
		function likeEntry(scratchieItemUid, rowid, burningQuestionUid) {
			
			var jqGrid = $("#burningQuestions" + scratchieItemUid);
			var likeCell = jqGrid.jqGrid('getCell', rowid, 'like');
			var isLike = likeCell.includes( 'fa-thumbs-o-up' );

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
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-thumbs-o-up", "fa-thumbs-up"));
		  				
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
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-thumbs-up", "fa-thumbs-o-up"));
			       		
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
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
				   	colNames:[
						'#',
						'isUserAuthor',
						"<fmt:message key='label.monitoring.summary.user.name' />",
						"<fmt:message key='label.burning.questions' />",
						"<fmt:message key='label.like' />",
						"<fmt:message key='label.count' />"
					],
				   	colModel:[
				   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
				   		{name:'isUserAuthor', width:0, hidden: true},
				   		{name:'groupName', index:'groupName', width:100, title: false},
				   		{name:'burningQuestion', index:'burningQuestion', width:501, edittype: 'textarea', title: false, editoptions:{rows:"8"},
					   		formatter:function(cellvalue, options, rowObject) {
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
										 	"<a href='#questionTitle${i.count}' class='bq-title'><fmt:message key='label.question'/>&nbsp;${i.count})</a>"
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
	  	          	beforeEditRow: function (options, rowid) {
		  	          	alert("aaa");
	  	          	},
	  				inlineEditing: { keys: true, defaultFocusField: "burningQuestion", focusField: "burningQuestion" },
	  				onSelectRow: function (rowid, status, e) {
	  	                var $self = $(this), 
	  	                	savedRow = $self.jqGrid("getGridParam", "savedRow");

	  	                if (savedRow.length > 0 && savedRow[0].id !== rowid) {
	  	                    $self.jqGrid("restoreRow", savedRow[0].id);
	  	                }

	  	                //edit row on its selection, unless "thumbs up" button is pressed
	  	                if (e.target.classList.contains("fa") && e.target.classList.contains("fa-2x") 
	  		  	                && (e.target.classList.contains("fa-thumbs-o-up") || e.target.classList.contains("fa-thumbs-up"))) { 
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
				   				like:'<span class="fa fa-thumbs-up fa-2x"></span>',
				   			</c:when>
					   		<c:when test="${!isUserLeader}">
				   				like:'',
				   			</c:when>
							<c:when test="${burningQuestionDto.userLiked}">
								like:'<span class="fa fa-thumbs-up fa-2x" title="<fmt:message key="label.unlike"/>"' +
										'onclick="javascript:likeEntry(${scratchieItemUid}, ${i.index + 1}, ${burningQuestionDto.burningQuestion.uid});" />',
							</c:when>
							<c:otherwise>
								like:'<span class="fa fa-thumbs-o-up fa-2x" title="<fmt:message key="label.like"/>"' +
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
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'#',
					"<fmt:message key='label.monitoring.summary.user.name' />",
					"<fmt:message key='label.learners.feedback' />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:140},
			   		{name:'feedback', index:'feedback', width:568}
			   	],
			   	caption: "<fmt:message key='label.other.groups' />"
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
				var isExpanded = eval($(this).data("expanded"));
				
				//fire the actual buttons so burning questions can be closed/expanded
				$(".ui-jqgrid-titlebar-close").each(function() {
					if (!isExpanded && $('span', this).hasClass('fa-chevron-circle-down')
							|| isExpanded && $('span', this).hasClass('fa-chevron-circle-up')) {
						this.click();
					}
				});

				//change button label
				var newButtonLabel = isExpanded ? "<fmt:message key='label.expand.all' />" : "<fmt:message key='label.collapse.all' />";
				$(".hidden-xs", $(this)).text(newButtonLabel);

				//change button icon
				if (isExpanded) {
					$(".fa", $(this)).removeClass("fa-minus-square").addClass("fa-plus-circle");
				} else {
					$(".fa", $(this)).removeClass("fa-plus-circle").addClass("fa-minus-square");
				}

				//change button's data-expanded attribute
				$(this).data("expanded", !isExpanded);
			});
		    
		})
	
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function refresh() {
			location.reload();
			return false;
		}
    </script>
</lams:head>

<body class="stripes">

	<c:set var="title">
		${scratchie.title} / <fmt:message key='label.score' />
	</c:set>
	<lams:Page type="learner" title="${title}">

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert id="submissionDeadline">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<lams:errors/>

		<lams:Alert id="score" type="info" close="false">
			<fmt:message key="label.you.ve.got">
				<fmt:param>${score}</fmt:param>
				<fmt:param>${scorePercentage}</fmt:param>
			</fmt:message>
		</lams:Alert>
<!--	
		<div class="row voffset5" >
            <a class="btn btn-sm btn-default pull-right roffset10" href="#" onclick="return refresh();">
                <i class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message key="label.refresh" /></span></a>
		</div>
-->		
		<c:if test="${showResults}">
			<%@ include file="scratchies.jsp"%>
		</c:if>
		
		<!-- Display burningQuestionItemDtos -->
		<c:if test="${sessionMap.isBurningQuestionsEnabled}">
            
            <a class="btn btn-sm btn-default pull-right roffset10" href="#" onclick="return refresh();">
            	<i class="fa fa-refresh"></i> 
            	<span class="hidden-xs">
            		<fmt:message key="label.refresh" />
            	</span>
            </a>

            <a id="toggle-burning-questions-button" class="btn btn-sm btn-default pull-right roffset10" data-expanded="true" href="#nogo">
            	<i class="fa fa-minus-square"></i> 
            	<span class="hidden-xs">
            		<fmt:message key="label.collapse.all" />
            	</span>
            </a>
            
			<div class="voffset5">
				<div class="lead">
					<fmt:message key="label.burning.questions" />
				</div>

				<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
					<c:if test="${not empty burningQuestionItemDto.burningQuestionDtos}">
						<c:set var="scratchieItemUid" value="${empty burningQuestionItemDto.scratchieItem.uid ? 0 : burningQuestionItemDto.scratchieItem.uid}"/>
						<div class="burning-question-dto">
							<table id="burningQuestions${scratchieItemUid}" class="scroll" cellpadding="0" cellspacing="0"></table>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</c:if>

		<!-- Display reflections -->
		<c:if test="${sessionMap.reflectOn}">
			<div class="voffset20">
				<div class="panel panel-default">
					<div class="panel-heading-sm  bg-success">
						<fmt:message key="monitor.summary.td.notebookInstructions" />
					</div>
					<div class="panel-body-sm">
						<div class="panel">
							<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
						</div>
						<c:choose>
							<c:when test="${empty sessionMap.reflectEntry}">
								<p>
									<fmt:message key="message.no.reflection.available" />
								</p>
							</c:when>
							<c:otherwise>
								<div class="panel-body-sm bg-warning">
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</div>
							</c:otherwise>
						</c:choose>
						<c:if test="${(mode != 'teacher') && isUserLeader}">
							<div class="voffset5">
								<button name="finishButton" onclick="return continueReflect()" class="btn btn-sm btn-default">
									<fmt:message key="label.edit" />
								</button>
							</div>
						</c:if>
					</div>
				</div>

				<c:if test="${fn:length(reflections) > 0}">
					<div id="reflections-div">
						<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
				</c:if>
			</div>
		</c:if>

		<!-- Display finish buttons -->
		<c:if test="${mode != 'teacher'}">
			<div class="voffset10 pull-right">
				<a href="#nogo" name="finishButton" id="finishButton" onclick="return finishSession()"
					class="btn btn-primary na">
					<c:choose>
						<c:when test="${sessionMap.isLastActivity}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</a>
			</div>
		</c:if>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
