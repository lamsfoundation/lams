<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<c:set var="title"><fmt:message key="label.countdown.timer"></fmt:message></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
<title><c:out value="${title}"/></title>

<lams:css/>
<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
<link rel="stylesheet" type="text/css" href="css/timer.css" />

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/nprogress.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/fullscreen.js"></script>
<script>
	
	var playSound = true;
	var haveClickedReset = false;
	var isCounting = false;
	var audioElement = null;
	var fullscreenEnabled = false;
	
	$(document).ready(function(){
		$(".setup").show();
		$(".timer").hide();
		$("#resume").hide();

		// progress bar
		NProgress.configure({
			showSpinner: false,
			trickle: false,
			parent: '#progressBarDiv',
			minimum: 0.02
		});
		
		setupFullScreenEvents();
	});

	function toggleBell() {
		playSound = ! playSound;
		updateBellButtons();
	}
	
	function updateBellButtons() {
		if ( playSound ) {
			$("#bellOn").show();
			$("#bellOff").hide();
		} else {
			$("#bellOff").show();
			$("#bellOn").hide();
		}
	}
	
 	function playAlert() {
		if ( playSound && ! haveClickedReset ) {
			// must do load every time or iPad will only play it once
			audioElement.load(); 
			audioElement.play();
		}
		if ( haveClickedReset ) {
			haveClickedReset = false;
		}
	} 
	
	function getNumber(value){
		var number = Number(value);
		if ( number == NaN || ! number > 0)
			return 0;
		return number;
	}
	
	function toSeconds(hours, minutes, seconds) {
		return hours * 60 * 60 + minutes * 60 + seconds;
	}
	
	function start() {
		if ( isTimeValid() ) {
			// recreate it each time or iPad will not play the sound if you run the timer again
		    audioElement = document.createElement('audio');
		    if (audioElement.canPlayType('audio/mpeg;')) {
			    audioElement.setAttribute('src', '${lams}includes/sounds/276607__mickleness__ringtone-foofaraw.mp3');
		    } else {
			    audioElement.setAttribute('src', '${lams}includes/sounds/276607__mickleness__ringtone-foofaraw.ogg');
		    }

		    isCounting = true;
			haveClickedReset = false;
			var startNumberOfSeconds = toSeconds(getNumber($("#hours").val()), getNumber($("#minutes").val()), getNumber($("#seconds").val()));
			if ( startNumberOfSeconds > 0 ) {
				$(".setup").hide();
				$(".timer").show();
				$("#pause").show();
				$("#resume").hide();
				updateBellButtons(); 
				NProgress.start();
				$('#countdownTarget').countdown({
					until: startNumberOfSeconds,  
					format: 'hMS', 
					description: '',
					onTick: function(periods) {
 						//check for 30 seconds
						if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
							$('#countdownTarget').addClass("text-danger");
						}		
 						// update progress bar
 						var ratioLeft = 1 - toSeconds(periods[4],periods[5],periods[6])/startNumberOfSeconds
						NProgress.set(ratioLeft);
 					},
					expiryText: '<span class="text-danger countdown-row-replacement-text"><fmt:message key="label.time.is.expired" /></span>',
					onExpiry: function(periods) {
						isCounting = false;
						if ( playSound ) {
							audioElement.addEventListener('ended', playAlert, false);
						    audioElement.play();
						}
						NProgress.done();
					}
				});
			}
		}
	}
	
	function isTimeValid() {
		var valid = true;
		$("#hours").val($("#hours").val().replace(/[^\d]+/g,''))
		if (  $("#hours").val() == "" || $("#hours").val() > 12 ) {
			$("#hoursDiv").addClass("has-error");
			valid = false;
		} else {
			$("#hoursDiv").removeClass("has-error");
		}
		$("#minutes").val($("#minutes").val().replace(/[^\d]+/g,''))
		if ( $("#minutes").val() == "" || $("#minutes").val() > 59 ) {
			$("#minutesDiv").addClass("has-error");
			valid = false;
		} else {
			$("#minutesDiv").removeClass("has-error");
		}
		$("#seconds").val($("#seconds").val().replace(/[^\d]+/g,''))
		if (  $("#seconds").val() == "" || $("#seconds").val() > 59 ) {
			$("#secondsDiv").addClass("has-error");
			valid = false;
		} else {
			$("#secondsDiv").removeClass("text-danger");
		}
		return valid;
	}

	function pause() { 
		if ( isCounting ) {
		    $('#countdownTarget').countdown('pause'); 
			$("#pause").hide();
			$("#resume").show();
		} else {
			reset();
		}
	}
	 
	function resume() { 
	    $('#countdownTarget').countdown('resume'); 
		$("#pause").show();
		$("#resume").hide();
	} 

	function reset() { 
		haveClickedReset = true;
		$(".setup").show();
		$(".timer").hide();
		$('#countdownTarget').removeClass("text-danger");
	    $('#countdownTarget').countdown('destroy'); 
	}
	 
</script>
						
</lams:head>

<body class="stripes">

	<lams:Page title="${title}" type="monitoring">
	<div class="full-screen-content-div">
	<div class="full-screen-flex-div">

	<div class="panel panel-default full-screen-main-div">
	<div class="panel-body">
	<div id="enterTimeDiv" class="setup">
		<span><fmt:message key='label.enter.countdown.time' /></span>
		<form class="form-inline voffset20">
		<div id="hoursDiv" class="form-group">
    			<label for="hours"><fmt:message key="label.hours"/></label>
			<input type="number" class="form-control" id="hours" name="hours" size="3" min="0" max="12" step="1" value="0" required>
		</div>
  		<div id="minutesDiv" class="form-group loffset10">
    			<label for="minutes"><fmt:message key="label.minutes"/></label>
			<input type="number" class="form-control" id="minutes" name="minutes" size="3" min="0" max="59" step="1" value="0" required>
		</div>
  		<div id="secondsDiv" class="form-group loffset10">
    			<label for="seconds"><fmt:message key="label.seconds"/></label>
			<input type="number" class="form-control" id="seconds" name="seconds" size="3" min="0" max="59" step="1" value="0" required>
		</div>
		</form>
	</div>
	
	<div id="timerDiv" class="timer">
		<fmt:message key='label.countdown.time.left' />
	 	<div id="countdownTarget"></div>
	</div>
	
	</div>
	</div>	 

	<div id="progressBarDiv"></div>
	
	 <div id="buttonsDiv">
		<a href="#" class="btn btn-default btn-primary fixed-button-width setup" onclick="javascript:start()"><fmt:message key="label.start"/></a>
		<a href="#" class="btn btn-default btn-primary fixed-button-width timer" id="pause" onclick="javascript:pause()"><fmt:message key="label.stop"/></a>
		<a href="#" class="btn btn-default btn-primary fixed-button-width timer" id="resume" onclick="javascript:resume()" ><fmt:message key="label.resume"/></a>
		<a href="#" class="btn btn-default loffset10 fixed-button-width timer" id="reset" onclick="javascript:reset()"><fmt:message key="label.reset"/></a>

        <a href="#" class="btn btn-default loffset10 fixed-button-width pull-right full-screen-launch-button" id="expand" onclick="javascript:launchIntoFullscreen(this)"><i class="fa fa-arrows-alt" aria-hidden="true"></i></a> 
        <a href="#" class="btn btn-default loffset10 fixed-button-width pull-right full-screen-exit-button" id="shrink" onclick="javascript:exitFullscreen()" style="display: none;"><i class="fa fa-compress" aria-hidden="true"></i></a> 
		<a href="#" class="btn btn-default loffset10 fixed-button-width pull-right" id="bellOn" onclick="javascript:toggleBell()"><i class="fa fa-volume-up" aria-hidden="true"></i></a>
		<a href="#" class="btn btn-default loffset10 fixed-button-width pull-right" id="bellOff" onclick="javascript:toggleBell()" style="padding-top: 0px; padding-bottom: 0px; display: none;">
        <span class="fa fa-stack"><i class="fa fa-volume-up" aria-hidden="true"></i><i class="fa fa-ban fa-stack-2x text-danger" aria-hidden="true"></i>
		</span></a>
	</div>

	</div>
	</div>	
	</lams:Page>
</body>
</lams:html>
