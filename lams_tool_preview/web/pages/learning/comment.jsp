	<link rel="stylesheet" href="${lams}css/jquery.jRating.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>/includes/css/learning.css'/>">
	<style media="screen,projection" type="text/css">
		#no-users-info {display: none;}
	</style>

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";

		//vars for rating.js
		var MAX_RATES = 0, // only applies to stars
		MIN_RATES = 0, // only applies to stars
		MAX_RATINGS_FOR_ITEM = 0, // only applies to stars
		COUNT_RATED_ITEMS = 0, // only applies to stars
		LAMS_URL = '${lams}',
		COMMENTS_MIN_WORDS_LIMIT = ${criteriaRatings.ratingCriteria.commentsMinWordsLimit},
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${criteriaRatings.ratingCriteria.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script> 	
	
	<script type="text/javascript">
	
	$(document).ready(function(){
		
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
		    widthFixed: true,
		    sortInitialOrder: 'desc',
		    headerTemplate : '{content} {icon}',
		    widgets: ['uitheme', 'zebra'],
	        headers: { 
	            1: { 
	                sorter: false 
	            }, 
	            2: {
	                sorter: false 
	            } 
	        }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
			    container: $(this).find(".ts-pager"),
			    output: '{startRow} to {endRow} ({totalRows})',
				cssPageDisplay: '.pagedisplay',
				cssPageSize: '.pagesize',
				cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/learning/getUsers.do'/>?page={page}&size={size}&{sortList:column}&sessionMapID=${sessionMapID}&toolContentId=${peerreview.contentId}&toolSessionId=${toolSessionId}&criteriaId=${criteriaRatings.ratingCriteria.ratingCriteriaId}&userId=<lams:user property='userID' />",
				ajaxProcessing: function (data) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};

			    		if (data.rows.length == 0) {
			    			$(".tablesorter,.pager").hide();
			    			$("#no-users-info").show();
			    		}
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							var itemId = userData["itemId"];
							var isDisabled = "${finishedLock}";
							
							rows += '<tr>';

							rows += '<td class="username" width="20%"><span>';
							rows += userData["itemDescription"];
							rows += '</span>'

							rows += '</td>';
							
							rows += '<td class="comment" id="comments-area-' + itemId + '" width="80%">';
								
							var commentPostedByUser = userData["comment"];
									
							if (commentPostedByUser != "") {
								rows += '<div class="rating-comment">';
								rows += 	commentPostedByUser;
								rows += '</div>';
										
							} else if (!isDisabled) {
								rows += '<div id="add-comment-area-' + itemId + '">';	
								rows += '<div class="no-gutter">';
								rows += '';
								rows += '<div class="col-xs-12 col-sm-11 ">';										
								rows +=		'<textarea name="comment-textarea-'+itemId+'" rows="4" id="comment-textarea-'+ itemId +'" onfocus="if(this.value==this.defaultValue)this.value=\'\';" onblur="if(this.value==\'\')this.value=this.defaultValue;" class="form-control"><fmt:message key="label.comment.textarea.tip"/></textarea>';
								rows += '</div>';
								rows += 	'<div class="button add-comment add-comment-new col-xs-12 col-sm-1" data-item-id="'+ itemId +'" data-comment-criteria-id="${criteriaRatings.ratingCriteria.ratingCriteriaId}">';
								rows += 	'</div>';
									
								rows += '';
								rows += '';
								rows += '</div>';											
							}
								
							rows += '</td>';
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			    	}
				}
			})
			
			// bind to pager events
			.bind('pagerInitialized pagerComplete', function(event, options){
				initializeJRating();
			});

		});
	 });
	

	function submitEntry(next){
		// ratings already saved, just save any unsaved comments.
		hideButtons();
		$("#next").val(next);
		$('textarea').each(function() {
			if (this.value==this.defaultValue)
				this.value="";
		});
		$("#editForm").submit();
	}

    </script>

	<form action="<c:url value="/learning/submitComments.do?"/>" method="get" id="editForm">
		<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
		<input type="hidden" name="toolContentId" value="${toolContentId}"/>
		<input type="hidden" name="criteriaId" value="${criteriaRatings.ratingCriteria.ratingCriteriaId}"/>
		<input type="hidden" name="next" id="next" value=""/>		

		<lams:TSTable numColumns="2">
			<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" > 
				<fmt:message key="label.user.name" />
			</th>
			<th class="comment"> 
				<fmt:message key="label.comment" />
			</th>
		</lams:TSTable>

	</form>
								
	<div id="no-users-info">
		<fmt:message key="label.no.users" />
	</div>
