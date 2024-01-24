package org.lamsfoundation.lams.timezone;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class TimeZoneUtil {

    private static final List<String> TIMEZONES = ZoneId.getAvailableZoneIds().stream().sorted()
	    .collect(Collectors.toList());

    public static String getServerTimeZone() {
	String serverTimezone = Configuration.get(ConfigurationKeys.SERVER_TIMEZONE);
	return StringUtils.isBlank(serverTimezone) ? TimeZone.getDefault().getID() : serverTimezone;
    }

    public static boolean isTimeZoneValid(String timezoneId) {
	return getTimeZones().contains(timezoneId);
    }

    public static List<String> getTimeZones() {
	return TIMEZONES;
    }
}