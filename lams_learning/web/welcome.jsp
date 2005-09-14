<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>

<table width="100%" height="295" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
	<tr> 
		<td valign="middle"> 
			<div align="center">
				<p class="heading">Select an Activity Sequence from the panel on the left to begin.</p>
            	<p class="body">You may start a new sequence, resume an existing sequence or view a completed sequence.</p>
          	</div>
        </td>
    </tr>
    <TR>
		<TD>
			<html:link href="/lams/learning/learner.do?method=joinLesson&userId=2&lessonId=2" target="_self" >
				Join a started Lesson
			</html:link>
		</TD>
    </TR>
</table>
