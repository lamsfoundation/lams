	<script type="text/javascript" src="${lams}includes/javascript/interact.min.js"></script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/ranking.js"></script>

	<%-- Build up the divs now and the ordering, then include the divs in the body and trigger the ordering in document.ready --%>
	<c:choose>
	<c:when test="${finishedLock}">
		<c:set var="rowdrop"></c:set>
		<c:set var="itemdrop"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="rowdrop">dropzone</c:set>
		<c:set var="itemdrop">item draggable</c:set>
	</c:otherwise>
	</c:choose>
	
	<c:set var="itemDivs"></c:set>
	<c:set var="javascriptReady"></c:set>	
	<c:forEach var="ratingDto" items="${criteriaRatings.ratingDtos}">
		<c:set var="itemDivs">${itemDivs} <div id="item${ratingDto.itemId}" class="${itemdrop}">${ratingDto.itemDescription}</div></c:set>
		<c:if test="${ratingDto.userRating > 0}">
			<c:set var="javascriptReady">${javascriptReady} rankedArray[${ratingDto.userRating}]='item${ratingDto.itemId}';</c:set>		
		</c:if>
	</c:forEach>
	
	<script type="text/javascript">

		var learners;
		
		$(document).ready(function(){
			
			var rankedArray = new Array();
			${javascriptReady}
			var numRankings = rankedArray.length;
			if ( numRankings > 0 ) { 
				var i;
				var rankedDiv = document.getElementById('ranked');
				for (i = 1; i < numRankings; i++) {
					var newChild = document.getElementById(rankedArray[i]);
					if ( newChild ) {
						newChild.classList.add('${rowdrop}');
						rankedDiv.appendChild(newChild);
					}
				}
				rankedDiv.appendChild(document.getElementById('lastEntry'));
			}
			
			learners = document.getElementById('learners');
			updateAddLabel();
			testButtons();

		});
		

		function testButtons() {
			if ( learners.children.length == 0 ) {
				showButtons();
			} else {
				hideButtons();
			}
		}
		
		function updateAddLabel() {
			if ( learners.children == undefined || learners.children.length == 0  ) {
		    	$('#lastEntry').html('<fmt:message key="label.alllearnersadded"/>');
			} else {
				$('#lastEntry').html('<fmt:message key="label.addlearnerhere"/>');
			}
		}
			
		function doDrop(event) {
	    	var theTarget = event.target;
	    	var newChild = event.relatedTarget;
	    	if ( theTarget.id == "unranked" ) {
	    		// move it back to learner list
			    newChild.classList.remove('${rowdrop}');
	    		learners.insertBefore(newChild, learners.firstChild);
	    	} else {
		    	// put it before the item that is the target.
		    	var parent = theTarget.parentNode;
		    	parent.insertBefore(newChild, theTarget);
		    }
	    	removeClassWithHighlight(newChild, 'can-drop');
	    	addClassWithHighlight(newChild, '${rowdrop}');
		    resetXY(newChild);
		    updateAddLabel();
		    
		    testButtons();
		}

		function submitEntry(next){
			hideButtons();

			if ( learners.children.length > 0 ) {
				alert('<fmt:message key="error.assign.rankAll"/>');
				return false;
			}

			var editForm = document.getElementById('editForm');
			var children = document.getElementById('ranked').children;
			for (var i = 0; i < children.length; i++) {
				var child = children[i];
				if ( child.classList.contains('item') ) {
					processHidden(editForm, child, i+1)
				}
			}
			$("#next").val(next);
			editForm.submit();
		}
		
		function processHidden(editForm, item, rank) {
			var rankField = document.getElementById('rank'+rank);
			if ( ! rankField ) {
				rankField = document.createElement('input'); 
				rankField.type = 'hidden';
				rankField.name = 'rank'+rank;
			    editForm.appendChild(rankField);
			}
			rankField.value = item.id.substring(4);
		}
		
		function cancel() {
			document.location.href='<c:url value="/learning/refresh.do?sessionMapID=${sessionMapID}"/>';
		}

    </script>

	<form action="<c:url value="/learning/submitRankingHedging.do?"/>" method="get" id="editForm">

		<c:if test="${notcomplete}">
			<span class="alert"><fmt:message key="error.assign.rankAll"/></span><br/>
		</c:if>
		<span id="instructions"><strong><fmt:message key="label.assign.rankAll">
			</fmt:message></strong>
		</span>

		<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
		<input type="hidden" name="toolContentId" value="${toolContentId}"/>
		<input type="hidden" name="criteriaId" value="${criteriaRatings.ratingCriteria.ratingCriteriaId}"/>
		<input type="hidden" name="next" id="next" value=""/>

		<div class="row voffset10">
			<div class="col-md-6">
				<strong><fmt:message key="label.ranked"></fmt:message></strong>
				<div id="ranked"><div id="lastEntry" class="${rowdrop}" style="padding-top:10px;padding-bottom:10px"><fmt:message key="label.addlearnerhere"></fmt:message></div></div>
			</div>
			
			<div class="col-md-6">
				<div  class="${rowdrop}" id="unranked"><strong><fmt:message key="label.unranked"></fmt:message></strong></div>
				<div id="learners">
					${itemDivs}
				</div>
			</div>
		</div>
	
		</form>
		
