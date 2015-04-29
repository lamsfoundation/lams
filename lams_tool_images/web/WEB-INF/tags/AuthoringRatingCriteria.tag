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
<%@ attribute name="hasRatingLimits" required="false" rtexprvalue="true" %>
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
	<c:set var="noMaximumLabel" value="label.allow.comments" scope="request"/>
</c:if>
<c:if test="${empty minNumberWordsLabel}">
	<c:set var="minNumberWordsLabel" value="label.minimum.number.words" scope="request"/>
</c:if>

<!-- calculate whether isCommentsAllowed is true -->
<c:set var="isCommentsEnabled" value="false"/>
<c:set var="commentsMinWordsLimit" value="0"/>
<c:forEach var="criteria" items="${criterias}" varStatus="status">
	<c:if test="${criteria.commentsEnabled}">
		<c:set var="isCommentsEnabled" value="true"/>
		<c:set var="commentsMinWordsLimit" value="${criteria.commentsMinWordsLimit}"/>
	</c:if>
</c:forEach>

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
			html: '<div class="up-arrow" title="<fmt:message key="${upLabel}"/>" />' + 
		    		'<div class="down-arrow-disabled" title="<fmt:message key="${downLabel}"/>" />'
		    		
		})).append(jQuery('<td/>', {
			width: '20px',
			html: '<div class="delete-arrow" title="<fmt:message key="${deleteLabel}"/>" />'
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

<div class="rating-criteria-tag">
	<h2>
		<fmt:message key="${headerLabel}" />
	</h2>
	<input type="hidden" name="criteriaMaxOrderId" id="criteria-max-order-id" value="${maxOrderId}">

	<table class="alternative-color" cellspacing="0" id="criterias-table">

		<c:forEach var="criteria" items="${criterias}" varStatus="status">
			<c:if test="${!criteria.commentsEnabled}">
				<tr>
					
					<td class="criteria-info">
						<input type="hidden" name="criteriaOrderId${criteria.orderId}" value="${criteria.orderId}">
						<input type="text" name="criteriaTitle${criteria.orderId}" value="${criteria.title}">
					</td>
					
					<td width="40px">
						<c:if test="${not status.first}">
							<div class="up-arrow" title="<fmt:message key="${upLabel}"/>"></div>
							<c:if test="${status.last}">
								<div class="down-arrow-disabled" title="<fmt:message key="${downLabel}"/>"></div>
							</c:if>
						</c:if>
	
						<c:if test="${not status.last}">
							<c:if test="${status.first}">
								<div class="up-arrow-disabled" title="<fmt:message key="${upLabel}"/>"></div>
							</c:if>
	
							<div class="down-arrow" title="<fmt:message key="${downLabel}"/>"></div>
						</c:if>
					</td>
	                
					<td width="20px">
						<div class="delete-arrow" title="<fmt:message key="${deleteLabel}"/>" ></div>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
	
	<div style="height: 30px;">
		<a href="#nogo" class="button-add-item float-right" id="add-criteria">
			<fmt:message key="${addLabel}" /> 
		</a>
	</div>
	
	<c:if test="${hasRatingLimits}">
		<div>
			<fmt:message key="${minimumLabel}" />
			<html:select property="imageGallery.minimumRates" styleId="minimum-rates" onmouseup="validateRatingLimits(true);">
				<html:option value="0">
					<fmt:message key="${noMinimumLabel}" />
				</html:option>
				<html:option value="1">1</html:option>
				<html:option value="2">2</html:option>
				<html:option value="3">3</html:option>
				<html:option value="4">4</html:option>
				<html:option value="5">5</html:option>
				<html:option value="6">6</html:option>
				<html:option value="7">7</html:option>
				<html:option value="8">8</html:option>
				<html:option value="9">9</html:option>
				<html:option value="10">10</html:option>
			</html:select>
		
			<fmt:message key="${maximumLabel}" />
			<html:select property="imageGallery.maximumRates" styleId="maximum-rates" onmouseup="validateRatingLimits(false);">
				<html:option value="0">
					<fmt:message key="${noMaximumLabel}" />
				</html:option>
				<html:option value="1">1</html:option>
				<html:option value="2">2</html:option>
				<html:option value="3">3</html:option>
				<html:option value="4">4</html:option>
				<html:option value="5">5</html:option>
				<html:option value="6">6</html:option>
				<html:option value="7">7</html:option>
				<html:option value="8">8</html:option>
				<html:option value="9">9</html:option>
				<html:option value="10">10</html:option>
			</html:select>
		</div>
	</c:if>
	
	<div class="space-top">
		<input type="checkbox" name="isCommentsEnabled" value="true" class="noBorder" id="enable-comments"
			<c:if test="${isCommentsEnabled}">checked="checked"</c:if>
		/>
		<label for="enable-comments">
			<fmt:message key="${allowCommentsLabel}" />
		</label>
		
		<div class="small-space-top" >
			<label for="comments-min-words-limit" id="comments-min-words-limit-label"
				<c:if test="${!isCommentsEnabled}">class="gray-color"</c:if>
			>
				<fmt:message key="${minNumberWordsLabel}" >
					<fmt:param> </fmt:param>
				</fmt:message>
			</label>
			<input type="text" name="commentsMinWordsLimit" id="comments-min-words-limit" value="${commentsMinWordsLimit}"/>
		</div>
		
	</div>
	
</div>
<!-- end tab content -->