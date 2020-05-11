package org.lamsfoundation.lams.timezone.util;

import java.util.Comparator;

import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;


/**
*
* @author Aung Thet Ko Ko
* @see org.lamsfoundation.lams.timezone.dto.TimezoneDTO
* Comparator to sort timezones by timezone id 
* LDEV-4377 - Show timezones to select order alphabetically
*/

public class TimezoneIDComparator implements Comparator<TimezoneDTO> {

    @Override
    public int compare(TimezoneDTO o1, TimezoneDTO o2) {
	if (o1 != null && o2 != null && o1.getTimeZoneId() != null && o2.getTimeZoneId() != null) {
	  
        return o1.getTimeZoneId().compareTo(o2.getTimeZoneId());

	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
    
   
}
