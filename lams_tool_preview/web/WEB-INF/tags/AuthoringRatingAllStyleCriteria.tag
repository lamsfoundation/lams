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

<style>
	#criterias-table-body .rubrics-table {
		width: 100%;
	}
	
	#criterias-table-body .rubrics-table .rubrics-columns-part td {
		padding: 0 3px 20px 3px;
		text-align: right;
		vertical-align: top;
	}
	
	#criterias-table-body .rubrics-table .rubrics-rows-part th {
		border-top: 1px solid #ddd;
		padding-top: 10px;
	}
	
	#criterias-table-body .rubrics-table .rubrics-row td {
		padding: 10px 3px 0 3px;
		vertical-align: top;
	}
	
	#criterias-table-body .rubrics-table .rubrics-row-title {
		border-right: 1px solid #ddd;
	}
	
	#criterias-table-body .rubrics-table th {
		text-align: center;
	}
	
	#criterias-table-body .rubrics-table textarea {
		width: 100%;
		resize: vertical;
	}
	
	#criterias-table-body .rubrics-table .rubrics-delete-column-button {
		margin-right: 4px;
		display: block;
	}
</style>

<script type="text/javascript">
	var minimumWordsSpinnerArray  = new Array();
	var maxOrderId;
	var criteriaArrows = '<div class="arrow-up" title="<fmt:message key="${upLabel}"/>" />' + 
	 					 '<div class="arrow-down" title="<fmt:message key="${downLabel}"/>" />';
	  
	var newGroupId = -1; // groups existing in DB have ID > 0; new, unsaved ones have ID < 0
	const MAX_RUBRICS_COLUMNS = 5;
	
	$(document).ready(function() { 
	
		// find max orderId & generate up javascript calls to set up the initial values 
		<c:set var="maxOrderId" value="0"/>
		<c:forEach var="criteria" items="${criterias}" varStatus="status">
			<c:if test="${criteria.orderId > maxOrderId}">
				<c:set var="maxOrderId" value="${criteria.orderId}"/>
			</c:if>
			<c:set var="escapedTitle"><c:out value="${criteria.title}" escapeXml="true"/></c:set>
 			addRow('${criteria.orderId}', '${criteria.ratingStyle}', '${escapedTitle}', '${criteria.maxRating}', 
 					${criteria.commentsEnabled}, '${criteria.commentsMinWordsLimit}', '${criteria.minimumRates}', '${criteria.maximumRates}', '${criteria.ratingCriteriaGroupId}' );
		</c:forEach>
		maxOrderId = ${maxOrderId};
		if ( maxOrderId == 0 ) {
			addCriteria(); // always start with one criteria
		}
		reactivateArrows();
	
		//upCriteria
		 $( "body" ).on( "click", ".arrow-up", function() {
			var currentRow = $(this).closest('tr');
			var currentOrderId = $( "input[name^='criteriaOrderId']", currentRow);

			var prevRow = currentRow.prev();
			var prevOrderId = $( "input[name^='criteriaOrderId']", prevRow);

			currentOrderId.each(function(){
				var val = +$(this).val();
				$(this).val(val - prevOrderId.length);
			});		
			prevOrderId.each(function(){
				var val = +$(this).val();
				$(this).val(val + currentOrderId.length);
			});		
			
			//swap elements
			currentRow.insertBefore(prevRow);
			
			reactivateArrows();
		 } );
		 
		//downCriteria
		 $( "body" ).on( "click", ".arrow-down", function() {
				var currentRow = $(this).closest('tr');
				var currentOrderId = $( "input[name^='criteriaOrderId']", currentRow);

				var nextRow = currentRow.next();
				var nextOrderId = $( "input[name^='criteriaOrderId']", nextRow);

				currentOrderId.each(function(){
					var val = +$(this).val();
					$(this).val(val + nextOrderId.length);
				});		
				nextOrderId.each(function(){
					var val = +$(this).val();
					$(this).val(val + currentOrderId.length);
				});		
				
				//swap elements
				currentRow.insertAfter(nextRow);
				
				reactivateArrows();
		 });
		 
		 //deleteCriteria
		 $( "body" ).on( "click", ".delete-criteria", function() {
			var currentRow = $(this).closest('tr');
			var rubricsTable = currentRow.closest('.rubrics-table');
			
			currentRow.remove();
			if (rubricsTable.length > 0 && $('*[name^="criteriaTitle"]', rubricsTable).length == 0) {
				rubricsTable.closest('tr').remove();
			}
			
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
	
	function addRow(orderId, style, title, maxRating, justifyOrComment, commentMinWordsLimit, minimumRates, maximumRates, groupId) {
		var row = jQuery('<tr/>');
		var inputField = '<input type="text" class="form-control" name="criteriaTitle' + orderId + '" value="'+title+'">';
		var hiddenInputs = '<input type="hidden" name="ratingStyle' + orderId + '" value="' + style + '">' 
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
			    html: '<div class="voffset5"><fmt:message key="${styleComment}" />:&nbsp;</div>'+ inputField + hiddenInputs + ratingLimitsStr
			})).appendTo('#criterias-table-body');	
			
			activateSpinner(orderId, true);
			
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
			    html: '<div class="voffset5"><fmt:message key="${styleStar}" />:&nbsp;</div>'+inputField + hiddenInputs + ratingLimitsStr
			})).appendTo('#criterias-table-body');
				
			activateSpinner(orderId, justifyOrComment);
			
		} else if ( style == 2 ) { 
			var rankingStr = '<div class="voffset5"><label for="maxRating' + orderId + '"><fmt:message key="${rankLabel}"/></label>&nbsp;'
			  + generateSelect('maxRating' + orderId, null, null, orderId, maxRating);
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleRanking}" />:&nbsp;</div>'+ inputField + hiddenInputs + rankingStr 
			})).appendTo('#criterias-table-body');
	
		} else if ( style == 3) {
			row.append(jQuery('<td/>', {
				'class': 'criteria-info',
			    html: '<div class="voffset5"><fmt:message key="${styleHedging}" />:&nbsp;</div>' + inputField + hiddenInputs
				 + '<div class="voffset5"><label for="maxRating'+ orderId + '"><fmt:message key="${hedgeRankLabel}"/>&nbsp;</label>'
			     + '<input type="text" name="maxRating' + orderId + '" id="maxRating' + orderId + '" value="'+maxRating+'" size="4">&nbsp;'
				 + generateSpinner(orderId, justifyOrComment, '<fmt:message key="${justifyLabel}"/>', '<fmt:message key="${minNumberWordsLabel}"><fmt:param> </fmt:param></fmt:message>', commentMinWordsLimit)
			})).appendTo('#criterias-table-body');	
			
			activateSpinner(orderId, justifyOrComment);
			$("#maxRating" + orderId).spinner({ min: 1 });
			
		}  else if (style == 4) {
			if (!groupId) {
				// get a new group ID
				groupId = newGroupId;
				newGroupId--;
			}

			var rubricsTable = $('.rubrics-table[groupId="' + groupId + '"]');

			if (rubricsTable.length == 0 || rubricsTable.attr('groupId') != groupId) {
				// this is the first row of a rubrics table, so need to create the table and buttons etc.
				rubricsTable = addRubricsTable(groupId);
			}

			// input field for rubrics is a textarea, not regular text input
			inputField = '<textarea class="form-control" name="criteriaTitle' + orderId + '" value="' + title + '"></textarea>';
			// this criterion is grouped, so we need to store which group it belongs to
			hiddenInputs += '<input type="hidden" name="groupId' + orderId + '" value="' + groupId + '">';

			// each rubrics row is a separate criterion
			row.addClass('rubrics-row').appendTo(rubricsTable);
			$('<td />').addClass('rubrics-row-title').append(inputField + hiddenInputs).appendTo(row);

			// build row structure: row header, value for each column and cell with buttons
			var rubricsColumns = $('.rubrics-column', rubricsTable).length;
			for (var i = 1; i <= rubricsColumns; i++) {
				var cell = $('<td/>').appendTo(row);
				$('<textarea />').addClass('form-control rubrics-cell').attr('name', 'rubrics' + orderId + 'cell' + i).appendTo(cell);
			}
			row.append('<td />');
		}

		if (style != 4) {
			// for rubrics there are just arrows to move whole group, not for each row
			row.append(jQuery('<td/>', {
				width: '40px',
				html: criteriaArrows
			}));
		}

		// it deletes either whole criterion or a rubrics row (which is also a criterion)
		row.append($('<td/>', {
			width: '20px',
			html: '<i class="fa fa-times delete-criteria" title="<fmt:message key="${deleteLabel}"/>"></i>'
		}));
	}


	function addRubricsTable(groupId) {
		var row = $('<tr />'),
			rubricsColumns = MAX_RUBRICS_COLUMNS;

		row.appendTo('#criterias-table-body');

		// this cell holds whole rubrics table
		var cell = $('<td />', {
			'class': 'criteria-info',
			// arrows to move whole rubrics table
		    html: '<div class="voffset5"><fmt:message key="label.rating.style.rubrics" />:<div class="pull-right">' + criteriaArrows + '</div></div>'
		}).attr('colspan', '3').appendTo(row);

		var rubricsTable = $("<table />").addClass('table-striped table-condensed rubrics-table').attr('groupId', groupId).appendTo(cell)
										 .on('mouseup', 'textarea', function(){
				// there is no "resize" event for textarea, so we use mouseup to keep all boxes in the row the same height
				var height = $(this).height();
				$(this).closest('tr').find('textarea').height(height);
			}),
			addColumnButton = $('<button />').attr('type', 'button').addClass('btn btn-default pull-right voffset20 rubrics-add-column-button')
										   	 .text('<fmt:message key="label.rating.rubrics.column.add" />').click(function(){
						var columns = $('.rubrics-columns-part .rubrics-column', rubricsTable);
						// no more than X columns allowed
						if (columns.length >= MAX_RUBRICS_COLUMNS) {
							return;
						}

						// always add to the end
						var lastColumn = columns.last(),
							columnOrderId = +lastColumn.attr('name').split('column')[1],
							cell = $('<td />').insertAfter(lastColumn.parent());

						// add delete button
						$('<i />').addClass('fa fa-times rubrics-delete-column-button')
								  .attr('title', '<fmt:message key="${deleteLabel}"/>').appendTo(cell).click(function(){
							deleteRubricsColumn($(this));
						});

						// add column header input
						$('<textarea />').addClass('form-control rubrics-column')
										 .attr('name', 'rubrics' + groupId + 'column' + (columnOrderId + 1)).appendTo(cell);

						// go through each row and add a new column at the end
						$('textarea[name$="cell' + columnOrderId + '"]', rubricsTable).each(function() {
							var orderId = $(this).attr('name').split('cell')[0].replace('rubrics', ''),
								cell = $('<td/>');
							$('<textarea />').addClass('form-control rubrics-cell')
											 .attr('name', 'rubrics' + orderId + 'cell' + (columnOrderId + 1)).appendTo(cell);
							cell.insertAfter($(this).closest('td'));
						});

						// if delete column buttons were hidden, now show them
						$('.rubrics-delete-column-button', rubricsTable).show();

						// do not allow more than X columns
						if (columns.length == MAX_RUBRICS_COLUMNS - 1) {
							$('.rubrics-add-column-button', rubricsTable.parent()).hide();
						}
					}).appendTo(cell);

		if (rubricsColumns >=  MAX_RUBRICS_COLUMNS) {
			addColumnButton.hide();
		}
		
		$('<button />').attr('type', 'button').addClass('btn btn-default pull-right voffset20 roffset10')
					   .text('<fmt:message key="label.rating.rubrics.row.add" />').click(function(){
			// this will add a row to the existing rubrics table with the given group ID
			maxOrderId++;
			$("#criteria-max-order-id").val(maxOrderId);
			addRow(maxOrderId, 4, '', '', false, 1, 0, 0, groupId);
			reactivateArrows();
		}).appendTo(cell);

		// start adding content to rubrics table
		row = $('<tr />').appendTo(rubricsTable);
		// empty first cell, for row headers below
		row.append('<th />');

		$('<th />').attr('colspan', rubricsColumns)
				   .text('<fmt:message key="label.rating.rubrics.column.headers" />')
				   .appendTo(row);
		// empty cell for action buttons
		$('<th />').attr('colspan', 2).appendTo(row);

		// add column headers
		row = $('<tr />').addClass('rubrics-columns-part').appendTo(rubricsTable);
		// empty first cell, for row headers below
		row.append('<td />');
		
		for (var i = 1; i <= rubricsColumns; i++) {
			cell = $('<td />').appendTo(row);
			$('<i />').addClass('fa fa-times rubrics-delete-column-button')
					  .attr('title', '<fmt:message key="${deleteLabel}"/>').appendTo(cell).click(function(){
				deleteRubricsColumn($(this));
			});
			
			$('<textarea />').addClass('form-control rubrics-column').text(i)
							 .attr('name', 'rubrics' + groupId + 'column' + i).appendTo(cell);
		}
		// empty cell for action buttons
		$('<td />').attr('colspan', 2).appendTo(row);

		// description of the rows part
		row = $('<tr />').addClass('rubrics-rows-part').appendTo(rubricsTable);
		$('<th />').addClass('rubrics-row-title').text('<fmt:message key="label.rating.rubrics.row.headers" />').appendTo(row);
		$('<th />').attr('colspan', rubricsColumns)
				   .text('<fmt:message key="label.rating.rubrics.column.content" />')
				   .appendTo(row);
		$('<td />').attr('colspan', 2).appendTo(row);

		return rubricsTable;
	}

	function deleteRubricsColumn(button) {
		var rubricsTable = button.closest('.rubrics-table'),
			// extract this column's order ID
			columnOrderId = +button.siblings('.rubrics-column').attr('name').split('column')[1];

		// delete the column header
		button.closest('td').remove();

		// shift order IDs of column headers to the right
		$('.rubrics-column', rubricsTable).each(function(){
			var name = $(this).attr('name').split('column'),
				orderId = +name[1];
			if (orderId > columnOrderId) {
				$(this).attr('name', name[0] + 'column' + (orderId - 1));
			}
		});

		// go through each row, delete column description and shift order IDs of columns to the right
		$('.rubrics-cell[name$="cell' + columnOrderId + '"]', rubricsTable).parent('td').remove();
		$('.rubrics-cell', rubricsTable).each(function(){
			var name = $(this).attr('name').split('cell'),
				orderId = +name[1];
			if (orderId > columnOrderId) {
				$(this).attr('name', name[0] + 'cell' + (orderId - 1));
			}
		});

		// must not remove last column
		var columns = $('.rubrics-columns-part .rubrics-column', rubricsTable);
		if (columns.length == 1) {
			$('.rubrics-delete-column-button', rubricsTable).hide();
		}
		$('.rubrics-add-column-button', rubricsTable.parent()).show();
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
	
			<div class="form-inline">
				<table class="table table-striped table-condensed" id="criterias-table">	
					<tbody id="criterias-table-body">
						<!--  populated by javascript -->
					</tbody>
				</table>
			</div>
		</div>

		<div class="form-group voffset20">
 			<select id="ratingStyle" class="form-control form-control-inline loffset10">
				<option selected="selected" value="1"><fmt:message key="${styleStar}" /></option>
				<option value="2"><fmt:message key="${styleRanking}" /></option>
				<option value="3"><fmt:message key="${styleHedging}" /></option>
				<option value="0"><fmt:message key="${styleComment}" /></option>
				<option value="4"><fmt:message key="label.rating.style.rubrics" /></option>
			</select>

			<a href="#nogo" class="btn btn-default btn-sm loffset10" id="add-criteria">
				<i class="fa fa-plus"></i>&nbsp;<fmt:message key="${addLabel}" /> 
			</a>
		</div>
		
</div>
<!-- end rating -->