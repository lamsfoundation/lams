package org.lamsfoundation.lams.timezone;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

import java.util.TimeZone;

public class TimeZoneUtil {

    public static String getServerTimezone() {
	String serverTimezone = Configuration.get(ConfigurationKeys.SERVER_TIMEZONE);
	return StringUtils.isBlank(serverTimezone) ? TimeZone.getDefault().getID() : serverTimezone;
    }

    public static boolean isTimezoneValid(String timezoneId) {
	return ArrayUtils.contains(TimeZone.getAvailableIDs(), timezoneId);
    }
}