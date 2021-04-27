<% 
 /**
  * AuthoringRatingSimple.tag
  *	Author: Andrey Balan
  *	Description: Creates list of rating criterias for authoring page, only support stars.
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
<%@ attribute name="allLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="justifyLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="rankLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="hedgeRankLabel" required="false" rtexprvalue="true" %>

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
<c:if test="${empty allLabel}">
	<c:set var="allLabel" value="label.rating.all" scope="request"/>
</c:if>
<c:if test="${empty justifyLabel}">
	<c:set var="justifyLabel" value="label.rating.author.justify" scope="request"/>
</c:if>
<c:if test="${empty rankLabel}">
	<c:set var="rankLabel" value="label.rating.rank" scope="request"/>
</c:if>
<c:if test="${empty hedgeRankLabel}">
	<c:set var="hedgeRankLabel" value="label.rating.max.hedge" scope="request"/>
</c:if>

<c:if test="${empty styleStar}">
	<c:set var="styleStar" value="label.rating.style.star" scope="request"/>
</c:if>
<c:if test="${empty styleRanking}">
	<c:set var="styleRanking" value="label.rating.style.ranking" scope="request"/>
</c:if>
<c:if test="${empty styleHedging}">
	<c:set var="styleHedging" value="label.rating.style.hedging" scope="request"/>
</c:if>
<c:if test="${empty styleComment}">
	<c:set var="styleComment" value="label.rating.style.comment" scope="request"/>
</c:if>


<script type="text/javascript">
	var minimumWordsSpinnerArray  = new Array();
	var maxOrderId;
	
	$(document).ready(function() { 
	
		// find max orderId & generate up javascript calls to set up the initial values 
		<c:set var="maxOrderId" value="0"/>
		<c:forEach var="criteria" items="${criterias}" varStatus="status">
			<c:if test="${criteria.orderId > maxOrderId}">
				<c:set var="maxOrderId" value="${criteria.orderId}"/>
			</c:if>
			<c:set var="escapedTitle"><c:out value="${criteria.title}" escapeXml="true"/></c:set>
 			addRow('${criteria.orderId}', '${criteria.ratingStyle}', '${escapedTitle}', '${criteria.maxRating}', 
 					${criteria.commentsEnabled}, '${criteria.commentsMinWordsLimit}', '${criteria.minimumRates}', '${criteria.maximumRates}' );
		</c:forEach>
		maxOrderId = ${maxOrderId};
		if ( maxOrderId == 0 ) {
			addCriteria(); // always start with one criteria
		}
		reactivateArrows();
	
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
	  	 
	})

	function addCriteria() {
		var styleDropDown = document.getElementById("ratingStyle");
		var style = styleDropDown.options[styleDropDown.selectedIndex].value;
		maxOrderId++;
		$("#criteria-max-order-id").val(maxOrderId);
		addRow(maxOrderId, style, '', '', false, 1, 0, 0);
		reactivateArrows();
	}
	
	function generateSelect(id, validate, zeroDescription, orderId, currentValue) {		
		var str = '<select name="' + id + '" id="'+id+'" class="form-control form-control-inline"';
		if ( validate ) {
			str += ' onchange="validateRatingLimits('+validate+','+orderId+');" '
		} 
		str	+= '> <option value="-1" '+ ( -1 == currentValue ? ' selected="selected"' : '' ) + '><fmt:message key="${allLabel}"/></option>';
		if ( zeroDescription ) {
			str += '<option value="0" '+ ( 0 == currentValue ? ' selected="selected"' : '' ) + '>'+zeroDescription+'</option>';
		}
		for (var i = 1; i < 11; i++) {
			str += '<option value="'+i+'"' + (i == currentValue ? ' selected="selected"' : '' ) + '>'+i+'</option>';
		}
		str += '</select>';
		return str;
	}

	function generateManadatorySpinner(orderId, spinnerLabel, commentMinWordsLimitData) {
		var enableCommentsId = 'enableComments' + orderId;
		var spinnerId = 'commentsMinWordsLimit' + orderId;
		var spinnerlabelId = 'commentsMinWordsLimitLabel' + orderId;
		return '<div class="form-group loffset10 voffset5" id="commentsMinWordsLimitDiv'+orderId
			+ '"><input type="text" name="' + spinnerId + '" id="' + spinnerId +'" value="'+commentMinWordsLimitData+'" size="4"/>'
			+ '<label for="'+spinnerId+'" id="'+spinnerlabelId+'"'
			+ '>&nbsp;' + spinnerLabel + '</label></div>';
	}

	function generateSpinner(orderId, justifyOrComment, enableCommentsLabel, spinnerLabel, commentMinWordsLimitData) {
		var enableCommentsId = 'enableComments' + orderId;
		var spinnerId = 'commentsMinWordsLimit' + orderId;
		var spinnerlabelId = 'commentsMinWordsLimitLabel' + orderId;
		return '<div class="checkbox voffset5"><label for="'+enableCommentsId+'">'
			+ '<input type="checkbox" name="'+enableCommentsId+'" value="true" class="noBorder" id="' + enableCommentsId + '"' + ( justifyOrComment ? ' checked' : '') 	+ '/>&nbsp;'
			+ enableCommentsLabel + '</label></div>'
			+ '<div class="form-group loffset10 voffset5" id="commentsMinWordsLimitDiv'+orderId+'" ' +(justifyOrComment ? '' : 'style="visibility:hidden"') 
			+ '><input type="text" name="' + spinnerId + '" id="' + spinnerId +'" value="'+commentMinWordsLimitData+'" size="4"/>'
			+ '<label for="'+spinnerId+'" id="'+spinnerlabelId+'"'
			+ '>&nbsp;' + spinnerLabel + '</label></div>';
	}

	function activateSpinner(orderId, justifyOrComment) {
		var enableCommentsId = 'enableComments' + orderId;
		var spinnerId = 'commentsMinWordsLimit' + orderId;
		var spinnerlabelId = 'commentsMinWordsLimitLabel' + orderId;
		minimumWordsSpinnerArray[orderId] = $('#'+spinnerId).spinner({ 
				min: 0,
			 	disabled: !justifyOrComment
			 });
	 	$('#'+enableCommentsId).click(function() {
	 		var id = this.id.substring(14);
	 		var minimumWordsSpinner = minimumWordsSpinnerArray[id];
	 		if ( minimumWordsSpinner.spinner( "option", "disabled" ) ) {
	 			minimumWordsSpinner.spinner( "enable" );
	 			$('#commentsMinWordsLimitDiv'+orderId).css("visibility", "visible");
	 		} else {
	 			minimumWordsSpinner.spinner( "disable" );
	 			$('#commentsMinWordsLimitDiv'+orderId).css("visibility", "hidden");
	 		}
	     });
	}
	
	function addRow(orderId, style, title, maxRating, justifyOrComment, commentMinWordsLimit, minimumRates, maximumRates) {
		var row = jQuery('<tr/>');
		var inputField = '<div class="form-inline"><input type="text" class="form-control" name="criteriaTitle' + orderId + '" value="'+title+'">'
		  + '<input type="hidden" name="ratingStyle' + orderId + '" value="' + style + '">' 
		  + '<input type="hidden" name="criteriaOrderId' + orderId + '" value="' + orderId + '">';

		if ( style == 0 ) {
			var ratingLimitsStr = '';
			if ( '${hasRatingLimits}' == 'true' ) {
				ratingLimitsStr = '<div class="voffset5"><label for="minimumRates"><fmt:message key="${minimumLabel}" /></label>&nbsp;'
				+ generateSelect('minimumRates' + orderId, 'true', '<fmt:message key="${noMinimumLabel}"/>', orderId, minimumRates)
	  			+ '<label for="maximumRates" class="loffset10"><fmt:message key="${maximumLabel}" /></label>&nbsp;'
				+ generateSelect('maximumRates' + orderId, 'false', '<fmt:message key="${noMaximumLabel}"/>', orderId, maximumRates)
				+ '<BR/>'
			 	+ generateManadatorySpinner(orderId, '<fmt:message key="${minNumberWordsLabel}"><fmt:param> </fmt:param></fmt:message>', commentMinWordsLimit);
			}
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleComment}" />:&nbsp;</div>'+inputField+ratingLimitsStr
			})); 
	
		} else if ( style == 1 ) {
			var ratingLimitsStr = '';
			if ( '${hasRatingLimits}' == 'true' ) {
				ratingLimitsStr = '<div class="voffset5"><label for="minimumRates"><fmt:message key="${minimumLabel}" /></label>&nbsp;'
				+ generateSelect('minimumRates' + orderId, 'true', '<fmt:message key="${noMinimumLabel}"/>', orderId, minimumRates)
	  			+ '<label for="maximumRates" class="loffset10"><fmt:message key="${maximumLabel}" /></label>&nbsp;'
				+ generateSelect('maximumRates' + orderId, 'false', '<fmt:message key="${noMaximumLabel}"/>', orderId, maximumRates)
				+ '<BR/>'
			 	+ generateSpinner(orderId, justifyOrComment, '<fmt:message key="${allowCommentsLabel}" />', '<fmt:message key="${minNumberWordsLabel}"><fmt:param> </fmt:param></fmt:message>', commentMinWordsLimit);
			}
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleStar}" />:&nbsp;</div>'+inputField+ratingLimitsStr
			})); 
	
		} else if ( style == 2 ) { 
			var rankingStr = '<div class="voffset5"><label for="maxRating' + orderId + '"><fmt:message key="${rankLabel}"/></label>&nbsp;'
			  + generateSelect('maxRating' + orderId, null, null, orderId, maxRating);
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleRanking}" />:&nbsp;</div>'+ inputField + rankingStr 
			}));
	
		} else if ( style == 3) {
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleHedging}" />:&nbsp;</div>' +inputField
				 + '<div class="voffset5"><label for="maxRating'+ orderId + '"><fmt:message key="${hedgeRankLabel}"/>&nbsp;</label>'
			     + '<input type="text" name="maxRating' + orderId + '" id="maxRating' + orderId + '" value="'+maxRating+'" size="4">&nbsp;'
				 + generateSpinner(orderId, justifyOrComment, '<fmt:message key="${justifyLabel}"/>', '<fmt:message key="${minNumberWordsLabel}"><fmt:param> </fmt:param></fmt:message>', commentMinWordsLimit)
			}));
			
		} 
	
		row.append(jQuery('<td/>', {
			width: '40px',
			html: '<div class="arrow-up fa fa-long-arrow-up fa-pull-left" title="<fmt:message key="${upLabel}"/>" />' + 
		    		'<div class="voffset10"><div class="arrow-down fa fa-long-arrow-down fa-pull-right" title="<fmt:message key="${downLabel}"/>" />'
		    		
		})).append(jQuery('<td/>', {
			width: '20px',
			html: '<i class="fa fa-times" title="<fmt:message key="${deleteLabel}"/>"></i>'
		})).appendTo('#criterias-table-body');	
		
		// cannot activate the spinners until after the fields have been created by the appendTo above.
		if ( style == 0 ) {
			activateSpinner(orderId, true);
		} else if ( style == 1) {
			activateSpinner(orderId, justifyOrComment);
		} else if ( style == 3 ) {
			activateSpinner(orderId, justifyOrComment);
			$("#maxRating" + orderId).spinner({ min: 1 });
		}
	}
	 
	function reactivateArrows() {
		$('#criterias-table-body tr').each(function() {
	
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

	//check minimum is not bigger than maximum
	function validateRatingLimits(isMinimum, orderId) {
		var minRateDropDown = document.getElementById("minimumRates" + orderId);
		var minLimit = parseInt(minRateDropDown.options[minRateDropDown.selectedIndex].value);
		var maxRateDropDown = document.getElementById("maximumRates" + orderId);
		var maxLimit = parseInt(maxRateDropDown.options[maxRateDropDown.selectedIndex].value);

		// RANK ALL
		if ( minLimit == -1 || maxLimit == -1 ) {
			if ( isMinimum ) {
				if ( minLimit == -1 ) {
					maxRateDropDown.selectedIndex = 0;
				} else {
					maxRateDropDown.selectedIndex = minRateDropDown.selectedIndex;
				}
			} else {
				if ( maxLimit == -1 ) {
					minRateDropDown.selectedIndex = 0;
				} else {
					minRateDropDown.selectedIndex = maxRateDropDown.selectedIndex;
				}
			}
		}

		// OTHERWISE MIN <= MAX
		else if ((minLimit > maxLimit) && !(maxLimit == 0)) {
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

		<input type="hidden" name="criteriaMaxOrderId" id="criteria-max-order-id" value="${maxOrderId}">

		<div class="panel panel-default voffset5">
			<div class="panel-heading panel-title">
				<label><fmt:message key="${headerLabel}" /></label>
			</div>
	
			<table class="table table-striped table-condensed" id="criterias-table">	
			<tbody id="criterias-table-body">
				<!--  populated by javascript -->
			</tbody>
			</table>
		</div>

		<div class="form-group voffset20">
 			<select id="ratingStyle" class="form-control form-control-inline loffset10">
				<option selected="selected" value="1"><fmt:message key="${styleStar}" /></option>
				<option value="2"><fmt:message key="${styleRanking}" /></option>
				<option value="3"><fmt:message key="${styleHedging}" /></option>
				<option value="0"><fmt:message key="${styleComment}" /></option>
			</select>

			<a href="#nogo" class="btn btn-default btn-sm loffset10" id="add-criteria">
				<i class="fa fa-plus"></i>&nbsp;<fmt:message key="${addLabel}" /> 
			</a>
		</div>
		
</div>
<!-- end rating -->