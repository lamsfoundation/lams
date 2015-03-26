<% 
 /**
  * AuthoringRatingCriteria.tag
  *	Author: Andrey Balan
  *	Description: Creates list of rating criterias for authoring page
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="criterias" required="true" rtexprvalue="true" type="java.util.Collection" %>

<%-- Optional attribute --%>
<%@ attribute name="headerLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="addLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="deleteLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="upLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="downLabel" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty headerLabel}">
	<c:set var="headerLabel" value="label.rating.criterias" scope="request"/>
</c:if>
<c:if test="${empty addLabel}">
	<c:set var="addLabel" value="label.add.criteria" scope="request"/>
</c:if>
<c:if test="${empty deleteLabel}">
	<c:set var="deleteLabel" value="label.delete" scope="request"/>
</c:if>
<c:if test="${empty upLabel}">
	<c:set var="upLabel" value="label.move.up" scope="request"/>
</c:if>
<c:if test="${empty downLabel}">
	<c:set var="downLabel" value="label.move.down" scope="request"/>
</c:if>

<!-- begin tab content -->
<script type="text/javascript">
$(document).ready(function() { 
	
	// find max orderId
	<c:set var="maxOrderId" value="0"/>
	<c:forEach var="criteria" items="${criterias}" varStatus="status">
		<c:if test="${criteria.orderId > maxOrderId}">
			<c:set var="maxOrderId" value="${criteria.orderId}"/>
		</c:if>
	</c:forEach>
	var maxOrderId = ${maxOrderId};
	
	function addCriteria() {
		//increase maxOrderId by 1
		maxOrderId++;
		$("#criteria-max-order-id").val(maxOrderId);
		
		jQuery('<tr/>').append(jQuery('<td/>', {
			'class': 'criteria-info',
		    html: '<input type="hidden" name="criteriaOrderId' + maxOrderId + '" value="' + maxOrderId + '">' + 
		    		'<input type="text" name="criteriaTitle' + maxOrderId + '" value="">'
		    		
		})).append(jQuery('<td/>', {
			width: '40px',
			html: '<img class="up-arrow" title="<fmt:message key="${upLabel}"/>">' + 
		    		'<img class="down-arrow-disabled" title="<fmt:message key="${downLabel}"/>">'
		    		
		})).append(jQuery('<td/>', {
			width: '20px',
			html: '<img class="delete-arrow" title="<fmt:message key="${deleteLabel}"/>" />'
		})).appendTo('#criterias-table');
		
		reactivateArrows();
	}
	<c:if test="${empty criterias}">
		addCriteria(); 
		addCriteria();
	</c:if>
	
	function reactivateArrows() {
		$('#criterias-table tr').each(function() {

		    $this = $(this); // cache $(this)
		    
		    if ($this.is(':first-child')) {
		    	$(".up-arrow", $this).addClass("up-arrow-disabled").removeClass("up-arrow");
		        
		    } else {
		    	$(".up-arrow-disabled", $this).addClass("up-arrow").removeClass("up-arrow-disabled");
		    }
		    
		    if ($this.is(':last-child')) {
		    	$(".down-arrow", $this).addClass("down-arrow-disabled").removeClass("down-arrow");
		    	
		    } else {
		    	$(".down-arrow-disabled", $this).addClass("down-arrow").removeClass("down-arrow-disabled");
		    }
		});
	}
	
	//upCriteria
	 $( "body" ).on( "click", ".up-arrow", function() {
		var currentRow = $(this).closest('tr');
		var currentCriteriaTd = $( ".criteria-info", currentRow);
		var currentOrderId = $( "input[name^='criteriaOrderId']", currentCriteriaTd);
		//var textEl1 = $( "input[id^='criteria-title-']", currentRow);
		var prevRow = currentRow.prev();
		var prevCriteriaTd = $( ".criteria-info", prevRow);
		var prevOrderId = $( "input[name^='criteriaOrderId']", prevCriteriaTd);
		//var textEl2 = $( "input[id^='criteria-title-']", currentRow);
		
		//swap orderIds
		var temp = currentOrderId.val();
		currentOrderId.val(prevOrderId.val());
		prevOrderId.val(temp);
		
		//swap elements
		currentCriteriaTd.detach().prependTo(prevRow);
		prevCriteriaTd.detach().prependTo(currentRow);
		
		reactivateArrows();
	 } );
	 
	//downCriteria
	 $( "body" ).on( "click", ".down-arrow", function() {
		var currentRow = $(this).closest('tr');
		var currentCriteriaTd = $( ".criteria-info", currentRow);
		var currentOrderId = $( "input[name^='criteriaOrderId']", currentCriteriaTd);
		//var textEl1 = $( "input[id^='criteria-title-']", currentRow);
		var nextRow = currentRow.next();
		var nextCriteriaTd = $( ".criteria-info", nextRow);
		var nextOrderId = $( "input[name^='criteriaOrderId']", nextCriteriaTd);
		//var textEl2 = $( "input[id^='criteria-title-']", currentRow);
		
		//swap orderIds
		var temp = currentOrderId.val();
		currentOrderId.val(nextOrderId.val());
		nextOrderId.val(temp);
		
		//swap elements
		currentCriteriaTd.detach().prependTo(nextRow);
		nextCriteriaTd.detach().prependTo(currentRow);
		
		reactivateArrows();
	 });
	 
	 //deleteCriteria
	 $( "body" ).on( "click", ".delete-arrow", function() {
		var currentRow = $(this).closest('tr');
		currentRow.remove();
		
		reactivateArrows();
	});
	 
	 //addCriteria
	 $( "body" ).on( "click", "#add-criteria", function() {
		 addCriteria();
	});	 
	 
})
</script>

<div class="rating-criteria-tag">
	<h2 class="spacer-left">
		<fmt:message key="${headerLabel}" />
	</h2>
	<input type="hidden" name="criteriaMaxOrderId" id="criteria-max-order-id" value="${maxOrderId}">

	<table class="alternative-color" cellspacing="0" id="criterias-table">

		<c:forEach var="criteria" items="${criterias}" varStatus="status">
			<tr>
				
				<td class="criteria-info">
					<input type="hidden" name="criteriaOrderId${criteria.orderId}" value="${criteria.orderId}">
					<input type="text" name="criteriaTitle${criteria.orderId}" value="${criteria.title}">
				</td>
				
				<td width="40px">
					<c:if test="${not status.first}">
						<img class="up-arrow" title="<fmt:message key="${upLabel}"/>">
						<c:if test="${status.last}">
							<img class="up-arrow-disabled" title="<fmt:message key="${downLabel}"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img class="down-arrow-disabled" title="<fmt:message key="${upLabel}"/>">
						</c:if>

						<img class="down-arrow" title="<fmt:message key="${downLabel}"/>">
					</c:if>
				</td>
                
				<td width="20px">
					<img class="delete-arrow" title="<fmt:message key="${deleteLabel}"/>" />
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<div>
		<a href="#nogo" class="button-add-item float-right" id="add-criteria">
			<fmt:message key="${addLabel}" /> 
		</a>
	</div>
	
</div>
<!-- end tab content -->