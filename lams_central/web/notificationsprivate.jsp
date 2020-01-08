<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:css/>
<lams:css suffix="main"/>

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript">

	var LAMS_URL = '<lams:LAMSURL/>',
		PRIVATE_NOTIFICATIONS_MESSAGES = '<fmt:message key="label.private.notifications.messages"/>',
		PRIVATE_NOTIFICATIONS_READ = '<fmt:message key="label.private.notifications.read"/>',
		PRIVATE_NOTIFICATIONS_READ_HINT = '<fmt:message key="label.private.notifications.read.hint"/>',
		PRIVATE_NOTIFICATIONS_READ_ALL_HINT = '<fmt:message key="label.private.notifications.read.all.hint" />';

	function markAllPrivateNotificationsAsRead(){
		$('#tablePrivateNotifications tr[id^=subscription-]').each(function(){
			var row = $(this),
				read = $('td', row).last().text();
			if (!read) {
				markPrivateNotificationAsRead(row.attr('id').split('-')[1]);
			}
		});
	}

	function markPrivateNotificationAsRead(subscriptionUid){
		$.ajax({
			cache : false,
			url : LAMS_URL + "notification/markNotificationAsRead.do",
			data : {
				'subscriptionUid' : subscriptionUid
			},
			success : function() {
				// mark the message as read
				$('#tablePrivateNotifications tr#subscription-' + subscriptionUid + ' > td')
					// message cell
					.first().removeClass('notificationsPendingCell')
					// read cell
					.next().html('<i class="fa fa-check"></i>').removeClass('notificationsClickableCell').attr('title', null).off('click');
			}
		});
	}


	$(document).ready(function() {

		var table = $('#tablePrivateNotifications');	
		
		// table header
		headerRow = $('<tr />').appendTo(table);
	
		$('<td />').text(PRIVATE_NOTIFICATIONS_MESSAGES).appendTo(headerRow);
		// click it to mark all notifications as read
		$('<td class="notificationsClickableCell"/>').text(PRIVATE_NOTIFICATIONS_READ)
													 .attr('title', PRIVATE_NOTIFICATIONS_READ_ALL_HINT)
													 .click(markAllPrivateNotificationsAsRead)
													 .appendTo(headerRow);
		$.ajax({
			cache : false,
			url : LAMS_URL + "notification/getNotificationSubscriptions.do",
			dataType : 'json',
			data : {
				'limit'  : 30
			},
			success : function(notifications) {

				if (!notifications) {
					return;
				}
				
				// build notification rows one by one
				$.each(notifications, function(){
					var notification = this,
						row = $('<tr />').attr('id', 'subscription-' + notification.subscriptionUid)
										 .appendTo(table),
						messageCell = $('<td />').appendTo(row),
						readCell = $('<td class="notificationsReadCell" />')
										.appendTo(row);
					// is it a link?
					if (notification.message.indexOf('<a ') === 0) {
						var link = $(notification.message);
						// make it navigable
						messageCell.text(link.text()).addClass('notificationsClickableCell').click(function(){
							if (!readCell.text()) {
							markPrivateNotificationAsRead(notification.subscriptionUid);
							}
							// open in a new tab/window
							window.open(link.attr('href'), '_blank');
						});
					} else {
						messageCell.text(notification.message);
					}
					// was it read already?
					if (notification.pending) {
						messageCell.addClass('notificationsPendingCell');
						readCell.addClass('notificationsClickableCell')
								.attr('title', PRIVATE_NOTIFICATIONS_READ_HINT)
								.html('<i class="fa fa-square-o"></i>')
								.click(function(){
									markPrivateNotificationAsRead(notification.subscriptionUid);
								});
					} else {
						readCell.html('<i class="fa fa-check"></i>');
					}
				});
			}
		});

	});
</script>

<div style="clear: both;"></div>
<div class="container">
	<div class="row vertical-center-row">
		<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
		<div class="panel panel-default">
		<table id="tablePrivateNotifications" class="table table-condensed table-striped">
		</table>			
		</div>
		</div>
	</div>
</div>


