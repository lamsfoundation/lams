function removeLearnerFile(detailId,sessionId,userId,filename) {
	var msg = LABEL_DELETE.replace('{0}', filename)
	var answer = confirm(msg);
	if (answer) {	
		location.href = MONITOR_URL + "/removeLearnerFile.do?userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
	}
}

function restoreLearnerFile(detailId,sessionId,userId,filename) {
	var msg = LABEL_RESTORE.replace('{0}', filename)
	var answer = confirm(msg);
	if (answer) {	
		location.href = MONITOR_URL + "/restoreLearnerFile.do?userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
	}
}