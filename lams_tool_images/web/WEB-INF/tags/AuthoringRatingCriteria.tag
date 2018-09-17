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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="criterias" required="true" rtexprvalue="true" type="java.util.Collection" %>

<%-- Optional attribute --%>
<%@ attribute name="hasRatingLimits" required="false" rtexprvalue="true" %>
<%@ attribute name="formContentPrefix" required="false" rtexprvalue="true" %>
<%@ attribute name="styleId" required="false" rtexprvalue="true" %>
<%@ attribute name="headerLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="addLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="deleteLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="upLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="downLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="minimumLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="maximumLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="noMinimumLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="noMaximumLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="jsWarningLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="allowCommentsLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="minNumberWordsLabel" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty hasRatingLimits}">
	<c:set var="hasRatingLimits" value="false" scope="request"/>
</c:if>
<c:if test="${not empty formContentPrefix}">
	<c:set var="formContentPrefix" value="${formContentPrefix}."/>
</c:if>
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
<c:if test="${empty minimumLabel}">
	<c:set var="minimumLabel" value="label.minimum" scope="request"/>
</c:if>
<c:if test="${empty maximumLabel}">
	<c:set var="maximumLabel" value="label.maximum" scope="request"/>
</c:if>
<c:if test="${empty noMinimumLabel}">
	<c:set var="noMinimumLabel" value="label.no.minimum" scope="request"/>
</c:if>
<c:if test="${empty noMaximumLabel}">
	<c:set var="noMaximumLabel" value="label.no.maximum" scope="request"/>
</c:if>
<c:if test="${empty jsWarningLabel}">
	<c:set var="jsWarningLabel" value="js.warning.max.min.limit" scope="request"/>
</c:if>
<c:if test="${empty allowCommentsLabel}">
	<c:set var="allowCommentsLabel" value="label.allow.comments" scope="request"/>
</c:if>
<c:if test="${empty minNumberWordsLabel}">
	<c:set var="minNumberWordsLabel" value="label.minimum.number.words" scope="request"/>
</c:if>

<!-- calculate whether isCommentsAllowed is true -->
<c:set var="isCommentsEnabled" value="false"/>
<c:set var="commentsMinWordsLimit" value="0"/>
<c:forEach var="criteria" items="${criterias}" varStatus="status">
	<c:if test="${criteria.ratingStyle == 0 && criteria.commentsEnabled}">
		<c:set var="isCommentsEnabled" value="true"/>
		<c:set var="commentsMinWordsLimit" value="${criteria.commentsMinWordsLimit}"/>
	</c:if>
</c:forEach>

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
		    		'<input type="text" name="criteriaTitle' + maxOrderId + '" value="" class="form-control">'
		    		
		})).append(jQuery('<td/>', {
			width: '40px',
			html: '<div class="arrow-up fa fa-long-arrow-up fa-pull-left" title="<fmt:message key="${upLabel}"/>" />' + 
		    		'<div class="arrow-down fa fa-long-arrow-down fa-pull-right" title="<fmt:message key="${downLabel}"/>" />'
		    		
		})).append(jQuery('<td/>', {
			width: '20px',
			html: '<i class="fa fa-times" title="<fmt:message key="${deleteLabel}"/>"></i>'
		})).appendTo('#criterias-table');
		
		reactivateArrows();
	}
	<c:if test="${empty criterias}">
		addCriteria(); 
		addCriteria();
	</c:if>
	reactivateArrows();
	
	function reactivateArrows() {
		$('#criterias-table tr').each(function() {

		    $this = $(this); // cache $(this)
		    
		    if ($this.is(':first-child')) {
		    	$(".arrow-up", $this).removeClass("fa fa-long-arrow-up fa-pull-left");
		    } else {
		    	$(".arrow-up", $this).addClass("fa fa-long-arrow-up fa-pull-left");
		    }
		    
		    if ($this.is(':last-child')) {
		    	$(".arrow-down", $this).removeClass("fa fa-long-arrow-down fa-pull-right");
		    } else {
		    	$(".arrow-down", $this).addClass("fa fa-long-arrow-down fa-pull-right");
		    }
		});
	}
	
	//upCriteria
	 $( "body" ).on( "click", ".fa-long-arrow-up", function() {
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
	 $( "body" ).on( "click", ".fa-long-arrow-down", function() {
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
	 $( "body" ).on( "click", ".fa-times", function() {
		var currentRow = $(this).closest('tr');
		currentRow.remove();
		
		reactivateArrows();
	});
	 
	 //addCriteria
	 $( "body" ).on( "click", "#add-criteria", function() {
		 addCriteria();
	});
	 
 	//spinner
 	var minimumWordsSpinner = $("#comments-min-words-limit").spinner({ 
 		min: 0,
 		disabled: ${!isCommentsEnabled}
 	});
 	$("#enable-comments").click(function() {
 		if ( minimumWordsSpinner.spinner( "option", "disabled" ) ) {
 			minimumWordsSpinner.spinner( "enable" );
 			$("#comments-min-words-limit-label").removeClass("gray-color");
 		} else {
 			minimumWordsSpinner.spinner( "disable" );
 			$("#comments-min-words-limit-label").addClass("gray-color");
 		}
     });
	 
})

	//check minimum is not bigger than maximum
	function validateRatingLimits(isMinimum) {
		var minRateDropDown = document.getElementById("minimum-rates");
		var minLimit = parseInt(minRateDropDown.options[minRateDropDown.selectedIndex].value);
		var maxRateDropDown = document.getElementById("maximum-rates");
		var maxLimit = parseInt(maxRateDropDown.options[maxRateDropDown.selectedIndex].value);

		if ((minLimit > maxLimit) && !(maxLimit == 0)) {
			if (isMinimum) {
				minRateDropDown.selectedIndex = maxRateDropDown.selectedIndex;
			} else {
				maxRateDropDown.selectedIndex = minRateDropDown.selectedIndex;
			}

			alert('<fmt:message key="js.warning.max.min.limit"/>');
		}
	}
</script>

<div class="rating-criteria-tag" style="${styleId}">

	<div class="panel panel-default voffset5">
		<div class="panel-heading panel-title">
			<label><fmt:message key="${headerLabel}" /></label>
		</div>
	
		<div class="form-group roffset10 loffset10">
			<input type="hidden" name="criteriaMaxOrderId" id="criteria-max-order-id" value="${maxOrderId}">
			<table class="table table-condensed table-no-border" id="criterias-table">
		
				<c:forEach var="criteria" items="${criterias}" varStatus="status">
					<c:if test="${criteria.ratingStyle > 0}">
						<tr>
							
							<td class="criteria-info">
								<input type="hidden" name="criteriaOrderId${criteria.orderId}" value="${criteria.orderId}">
								<input type="text" name="criteriaTitle${criteria.orderId}" value="${criteria.title}" class="form-control">
							</td>
							
							<td width="40px">
								<div class="arrow-up" title="<fmt:message key="${upLabel}"/>"></div>
								<div class="arrow-down" title="<fmt:message key="${downLabel}"/>"></div>
							</td>
			                
							<td width="20px">
								<i class="fa fa-times" title="<fmt:message key="${deleteLabel}" />"></i>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
	
			<div style="height: 25px; margin-top: -10px;">
				<a href="#nogo" class="btn btn-default btn-xs pull-right" id="add-criteria">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="${addLabel}" /> 
				</a>
			</div>
	
			<c:if test="${hasRatingLimits}">
				<div>
					<label for="minimum-rates">
						<fmt:message key="${minimumLabel}" />
					</label>
					<form:select path="${formContentPrefix}minimumRates" id="minimum-rates" onmouseup="validateRatingLimits(true);" 
							cssClass="form-control form-control-inline">
						<form:option value="0">
							<fmt:message key="${noMinimumLabel}" />
						</form:option>
						<form:option value="1">1</form:option>
						<form:option value="2">2</form:option>
						<form:option value="3">3</form:option>
						<form:option value="4">4</form:option>
						<form:option value="5">5</form:option>
						<form:option value="6">6</form:option>
						<form:option value="7">7</form:option>
						<form:option value="8">8</form:option>
						<form:option value="9">9</form:option>
						<form:option value="10">10</form:option>
					</form:select>
				
					<label for="maximum-rates">
						<fmt:message key="${maximumLabel}" />
					</label>
					<form:select path="${formContentPrefix}maximumRates" id="maximum-rates" onmouseup="validateRatingLimits(false);" 
							cssClass="form-control form-control-inline">
						<form:option value="0">
							<fmt:message key="${noMaximumLabel}" />
						</form:option>
						<form:option value="1">1</form:option>
						<form:option value="2">2</form:option>
						<form:option value="3">3</form:option>
						<form:option value="4">4</form:option>
						<form:option value="5">5</form:option>
						<form:option value="6">6</form:option>
						<form:option value="7">7</form:option>
						<form:option value="8">8</form:option>
						<form:option value="9">9</form:option>
						<form:option value="10">10</form:option>
					</form:select>
				</div>
			</c:if>
	
			<div class="checkbox">
				<label for="enable-comments">
					<input type="checkbox" name="isCommentsEnabled" value="true" class="noBorder" id="enable-comments"
						<c:if test="${isCommentsEnabled}">checked="checked"</c:if>
					/>
					<fmt:message key="${allowCommentsLabel}" />
				</label>
			</div>
	
			<div>
				<label for="comments-min-words-limit" id="comments-min-words-limit-label"
					<c:if test="${!isCommentsEnabled}">class="gray-color"</c:if>
				>
					<fmt:message key="${minNumberWordsLabel}" >
						<fmt:param> </fmt:param>
					</fmt:message>
				</label>
				<input type="number" name="commentsMinWordsLimit" id="comments-min-words-limit" value="${commentsMinWordsLimit}" min="0"/>
			</div>
		</div>
	</div>
</div>
<!-- end tab content -->