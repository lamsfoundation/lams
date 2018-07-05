function removeLearnerFile(detailId,sessionId,userId,filename) {
	var msg = LABEL_DELETE.replace('{0}', filename)
	var answer = confirm(msg);
	if (answer) {	
		location.href = MONITOR_URL + "?method=removeLearnerFile&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
	}
}

function restoreLearnerFile(detailId,sessionId,userId,filename) {
	var msg = LABEL_RESTORE.replace('{0}', filename)
	var answer = confirm(msg);
	if (answer) {	
		location.href = MONITOR_URL + "?method=restoreLearnerFile&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
	}
}