	<script type="text/javascript" src="${lams}includes/javascript/interact.min.js"></script>
	<lams:JSImport src="includes/javascript/ranking.js" relative="true" />

	<%-- Build up the divs now and the ordering, then include the divs in the body and trigger the ordering in document.ready --%>
	<c:choose>
	<c:when test="${finishedLock}">
		<c:set var="rowdrop"></c:set>
		<c:set var="itemdrop"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="rowdrop">dropzone</c:set>
		<c:set var="itemdrop">draggable</c:set>
	</c:otherwise>
	</c:choose>
	
	<c:set var="itemDivs"></c:set>
	<c:set var="javascriptReady"></c:set>	
	<c:forEach var="ratingDto" items="${criteriaRatings.ratingDtos}">
		<c:set var="itemDivs">${itemDivs} <div id="item${ratingDto.itemId}" class="${itemdrop}">${ratingDto.itemDescription}</div></c:set>
		<c:set var="javascriptReady">${javascriptReady} setRanking('${ratingDto.itemDescription}', ${ratingDto.itemId}, '${ratingDto.userRating}');</c:set>		
	</c:forEach>
	
	<script type="text/javascript">

		$(document).ready(function(){
			${javascriptReady}
			
			testButtons();
		});
		

		function doDrop(event) {
	    	var theTarget = event.target;
	    	var newChild = event.relatedTarget;
	    	
	    	if ( theTarget.id == "unranked" ) {
	    		// move it back to learner list
	    		var learners = document.getElementById("learners");
	    		learners.insertBefore(newChild, learners.firstChild);
	    	} else {
			    if ( ! theTarget.classList.contains('divrankx') ) {
			    	theTarget = theTarget.parentNode;
			    }
			    if ( theTarget.classList.contains('divrankx') ) {
				    var learners = document.getElementById("learners");
				    while (theTarget.firstChild) {
				    	learners.insertBefore(theTarget.firstChild, learners.firstChild);
				    }
			    } 
			    theTarget.appendChild(newChild);
	    	}
	    	removeClassWithHighlight(newChild, 'can-drop');
		    resetXY(newChild);
		    
			testButtons();
		}

		function setRanking(itemDescription, itemId, rank) {
			if ( rank != '' && rank != '0') {
				var item = document.getElementById('item'+itemId);
				if ( item ) {
					var box = document.getElementById('divrank'+rank);
					if ( box ) {
						box.appendChild(item);
					}
				}
			}
		}
			
		function submitEntry(next) {
			hideButtons();
			var editForm = document.getElementById("editForm");
			if ( testCanSubmitEntry(editForm) ) {
				$("#next").val(next);
				editForm.submit();
			} else {
				alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.assign.ranks"><fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param></fmt:message></spring:escapeBody>');
			}
		}

		function testButtons() {
			var editForm = document.getElementById("editForm");
			if ( testCanSubmitEntry(editForm) ) {
				showButtons();
			} else {
				hideButtons();
			}
		}
		
		function testCanSubmitEntry(editForm) {
			var numFilled = 0;
			<c:forEach begin="1" end="${criteriaRatings.ratingCriteria.maxRating}" var="index">
			numFilled = numFilled + processHidden(editForm, 'rank${index}', 'divrank${index}');
			</c:forEach>
			if ( numFilled == ${criteriaRatings.ratingCriteria.maxRating}) {
				return true;
			} else {
				var learners = document.getElementById("learners");
				if ( learners.children.length > 0 ) {
					return false;
				} else {
					// no more learners to rank so allow
					return true;
				}
			}
			return false;
		}
		
		function processHidden(editForm, key, valueDivId) {
			var rankField = document.getElementById(key);
			if ( ! rankField ) {
				rankField = document.createElement('input'); 
				rankField.type = 'hidden';
				rankField.id = key;
				rankField.name = key;
			    editForm.appendChild(rankField);
			}
			var valueDiv = document.getElementById(valueDivId);
			if ( valueDiv && valueDiv.firstChild ) {
				var value = valueDiv.firstChild.id;
				value = value.substring(4);
				if ( value.length > 0 ) {
					rankField.value = value;
					return 1;
				}
			}
			rankField.value='';
			return 0;
		}

		function cancel() {
			document.location.href='<c:url value="/learning/refresh.do?sessionMapID=${sessionMapID}"/>';
		}
		

    </script>

	<form action="<c:url value="/learning/submitRankingHedging.do?"/>" method="get" id="editForm">

		<c:if test="${notcomplete}">
			<lams:Alert type="info"  id="warn-assign-more" close="true">
				<fmt:message key="error.assign.ranks"><fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param></fmt:message>
			</lams:Alert>
		</c:if>
		<span id="instructions"><strong><fmt:message key="label.assign.ranks">
			<fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param>
			</fmt:message></strong>
		</span>

		<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
		<input type="hidden" name="toolContentId" value="${toolContentId}"/>
		<input type="hidden" name="criteriaId" value="${criteriaRatings.ratingCriteria.ratingCriteriaId}"/>
		<input type="hidden" name="next" id="next" value=""/>

		<div class="row mt-2" id="drag-area">
			<div class="col-sm-6">
				<strong><fmt:message key="label.ranked"></fmt:message></strong>
				<c:forEach begin="1" end="${criteriaRatings.ratingCriteria.maxRating}" var="index">
					<div class="row"><div class="col-sm-1 divrankxlabel">${index}:</div><div class="col-sm-5 divrankx ${rowdrop}" id="divrank${index}" ></div></div>
				</c:forEach>
			</div>
			
			<div class="col-sm-6">
				<div class="${rowdrop}" id="unranked"><strong><fmt:message key="label.unranked"></fmt:message></strong></div>
				<div id="learners">
					${itemDivs}
				</div>
			</div>
		</div>
		
	</form>
		
