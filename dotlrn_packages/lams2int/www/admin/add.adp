<master>
<property name="title">@title@</property>
<property name="context">@context@</property>

<link type="text/css" rel="stylesheet" href="scripts/calendar.css">

<script language="javascript" type="text/javascript">
<!--
	
	var authorWin = null;
	
	function trim(str){
		return str.replace(/^\s+|\s+$/, '');
	}

	function switchSubmitButton(){
		if(document.getElementById("scheduled").checked){
			document.getElementById("save1").disabled = false;
			document.getElementById("save2").disabled = true;
		}else{
			document.getElementById("save1").disabled = true;
			document.getElementById("save2").disabled = false;
		}
	}


	function previewSequence(){
	    sequenceObj = document.getElementById("sequence_id");
	    if(sequenceObj.value == ""){
	        alert('You have to select a sequence to proceed.');
	        return false;
	    }

	   url = 'preview?sequence_id=' + document.getElementById("sequence_id").value;
	   window.open(url,'LAMS-Preview','height=600,width=800,resizable');

	}

	function nextStep(){
	    sequenceObj = document.getElementById("sequence_id");
	    if(sequenceObj.value == ""){
	        alert('You have to select a sequence to proceed.');
	        return false;
	    }
	    document.getElementById("step1").style.display="none";
	    document.getElementById("step2").style.display="block";
	}
	
	function openAuthor(url,name,options,fullscreen) {
		url = url + "&requestSrc=" + escape("Moodle");
		url = url + "&notifyCloseURL=" + escape(window.location.href);
		if(authorWin && !authorWin.closed){
			authorWin.focus();
		}else{
			authorWin = window.open(url,name,options);
			if (fullscreen) {
				authorWin.moveTo(0,0);
				authorWin.resizeTo(screen.availWidth,screen.availHeight);
			}
			authorWin.focus();
		}
		return false;
	}
	
	function prevStep(){
			document.getElementById("step1").style.display="block";
			document.getElementById("step2").style.display="none";
	}
	
	function validate(){
	    if(trim(document.getElementById("name").value).length==0){
	        alert('You have to specify the activity name to proceed.');
	        return false;
	    }
	    return true;
	}
	
	function validate2(){
		var d = new Date();
		d.setFullYear(document.getElementById("selYear1").value,document.getElementById("selMonth1").selectedIndex,document.getElementById("selDay1").selectedIndex+1);
		var h = document.getElementById("hour").selectedIndex+1;
		if(document.getElementById("ap").selectedIndex == 0){
			if(h==12){
				h = 0;
			}
		}else{
			if(h!=12){
				h = h + 12;
			}
		}
		d.setHours(h);
		d.setMinutes(document.getElementById("minute").selectedIndex);
		var now = new Date();
		now.setMinutes(now.getMinutes()+1);
		if(d<=now){
			alert('You have to schedule the lesson at some time point in the future!');
			return false;
		}else{
			document.getElementById("start_date").value = formatDate(d,"dd/M/yyyy h:mm a");
		}
		return validate();
	}
	
	function refreshSequences(){
		document.location.reload();
	}
	
	function selectSequence(obj){
		document.getElementById("sequence_id").value = obj;
	}
//-->
</script>

<form name="form" method="post" action="add-2" onSubmit="disableSumbit(this);">
<input type="hidden" id="lesson_id" name="lesson_id" value=""/>
<input type="hidden" name="create_sequence_url" id="create_sequence_url"/>
<input type="hidden" id="sequence_id" name="sequence_id" value=""/>
<input type="hidden" id="start_date" name="start_date"/>



<script type="text/javascript" src="scripts/yahoo.js"></script>
<script type="text/javascript" src="scripts/event.js" ></script>
<script type="text/javascript" src="scripts/dom.js" ></script>
<script type="text/javascript" src="scripts/calendar.js"></script>

<div id="step1" align="center">
<table cellpadding="5">
        <tr>    
                <td align="left">
                        <b>Sequences</b><br />
                        <p>The directory structure below contains the sequences you can create a lesson for.<br />Select one and click on the next button to continue.</p>
                </td>
        </tr>   
        <tr>
        <td align="center">
                <table width="60%" align="center">
                        <tr>
                                <td align="left"> 

					<script type="text/javascript" src="scripts/tree.js"></script>
					<script type="text/javascript" src="scripts/tree_tpl.js"></script>
					<script type="text/javascript" language="javascript">
					<!--
						var TREE_ITEMS = [ @sequence_list;noquote@ ];
				                var tree = new tree(TREE_ITEMS, TREE_TPL);
					//-->
                                        </script>

                                </td>
                        </tr>
                </table>        
        </td>
</tr>

<tr>
        <td align="center">
                <input type="button" id="create" name="create" value="Open LAMS Author" onClick="window.open('@lams_server_url@/LoginRequest?uid=@username@&method=author&ts=@datetime@&sid=@server_id@&hash=@hashValue@&courseid=@course_id@&country=AU&lang=EN&requestSrc=@requestSrc@&notifyCloseURL=@notifyCloseURL@','LAMS_Author','height=600,width=800,resizable');">
                <input type="button" id="preview" name="preview" value="Preview" onClick="previewSequence();">
                <input type="button" id="refresh" name="refresh" value="Refresh" onclick="javascript:refreshSequences();"/>
                <input type="submit" id="cancel" name=cancel value="Cancel" />
                <input type="button" id="next" name="next" value="Next" onClick="javascript:nextStep();"/>
        </td>
</tr>       
</table>

<hr>
</div>

<div id="step2" align="center" style="display:none">

<table cellpadding="5">
<tr>
        <td align="left">
                <b>Confirm the lesson details</b><br />
                <p>By pressing on the Start button you can begin the lesson straight away.</br>
You can also schedule the lesson to start at a particular date and time.</p>
        </td>
</tr>   
<tr>
        <td>
                <div>
                        <table align="center">
                                <tr>
                                        <td align="left">
                                                <b>Name:</b><br /><input type="text" id="name" name="name" value="" size="50">
                                        </td>
                                </tr>
                                <tr>
                                        <td align="left">
                                                <br />
                                                <b>Introduction:</b><br/>
                                                <font size="1">
						<textarea name="introduction" cols=60 rows=15 wrap=soft></textarea>
                                                <br />                          
                                                </font>

                                        </td>
                                </tr>
                                <tr>
                                        <td align="left"></td>
                                </tr>
                                <tr>
                                        <td align="left" with="100%">
                                                <br />
                                                <table with="100%">
                                                        <tr>
                                                                <td align="left">
                                                                </td>
                                                                <td colspan="3" align="left">

                                                                </td>
                                                        </tr>
                                                        <tr>
                                                                <td></td>
                                                                <td align="left">

                                                                </td>
                                                                <td colspan="2" align="left">
                                                                </td>
                                                        </tr>
                                                        <tr>
                                                                <td></td>
                                                                <td align="left">

                                                                </td>
                                                                <td align="left">
                                                                </td>
                                                                <td align="right">
                                                                </td>
                                                        </tr>
                                                </table>
                                        </td>
                                </tr>
                        </table>
                </div>
        </td>
</tr>
<tr>
        <td align="center">
                <input type="submit" id="cancel" name=cancel value="Cancel">
                <input type="button" id="prev" name="prev" value="Prev" onClick="javascript:prevStep()" disabled="true" />
                <input type="submit" id="save2" name="save" value="Start Now" onClick="javascript:return validate()" />
        </td>
</tr>
</table>
</div>