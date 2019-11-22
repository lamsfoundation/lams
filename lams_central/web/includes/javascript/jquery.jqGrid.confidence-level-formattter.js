//confidence level column formatter function for jqGrid
function gradientNumberFormatter (cellvalue) {
	var MIN_DATA_VALUE = 0,
		MAX_DATA_VALUE = 10,
		//default CONFIDENCE_LEVELS_TYPE to 1
		type = confidenceLevelsSettings ? confidenceLevelsSettings.type : '1';
	
	var dataAsNumber = parseInt(cellvalue, 10);
	if (dataAsNumber == -1) {
		return "";
	}
	if (dataAsNumber > MAX_DATA_VALUE) {
		dataAsNumber = MAX_DATA_VALUE;
	}
	if (dataAsNumber < MIN_DATA_VALUE) {
		dataAsNumber = MIN_DATA_VALUE;
	}
	var procents = (dataAsNumber - MIN_DATA_VALUE) * 100 / (MAX_DATA_VALUE - MIN_DATA_VALUE);
	var gradientClass;
	switch (true) {
		case (procents < 40):
			gradientClass = "gradient-red";
		break;
		case (procents >= 40 && procents <= 60):
			gradientClass = "gradient-orange";
		break;
		default:
			gradientClass = "gradient-green";
		break;
	}
	
	var text = "";
	switch (type) {
	  case '1':
			return '<div class="filled-bar">' +
						'<div class="gradient ' + gradientClass + '" style="width:' + procents + '%;"></div>' + 
						'<div class="filled-bar-text">' + (dataAsNumber * 10) + '%</div>' + 
				  '</div>';
			break;
	    
	  case '2':
		  switch (dataAsNumber) {
			  case 0:
				  text = confidenceLevelsSettings.LABEL_NOT_CONFIDENT;
				  break;
			  case 5:
				  text = confidenceLevelsSettings.LABEL_CONFIDENT;
				  break;
			  case 10:
				  text = confidenceLevelsSettings.LABEL_VERY_CONFIDENT;
		  }
		  break;
	    
	  case '3':
		  switch (dataAsNumber) {
			  case 0:
				  text = confidenceLevelsSettings.LABEL_NOT_SURE;
				  break;
			  case 5:
				  text = confidenceLevelsSettings.LABEL_SURE;
				  break;
			  case 10:
				  text = confidenceLevelsSettings.LABEL_VERY_SURE;
		  }
	}
	
	return '<div class="filled-bar">' +
				'<div class="gradient ' + gradientClass + '" style="width:100%;"></div>' + 
				'<div class="filled-bar-text">' + text + '</div>' + 
			'</div>';
};