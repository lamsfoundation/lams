/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.timezone.dto;

import java.util.Date;
import java.util.TimeZone;

import org.lamsfoundation.lams.timezone.Timezone;

/**
 * DTO object for {@link org.lamsfoundation.lams.timezone.Timezone}
 *
 * @author Andrey Balan
 *
 */
public class TimezoneDTO {

    /**
     * timezone id.
     */
    private String timeZoneId;
    /**
     * time zone raw offset
     */
    private Date rawOffset;
    /**
     * if raw offset is negative
     */
    private boolean isRawOffsetNegative;
    /**
     * time zone dst offset
     */
    private int dstOffset;
    /**
     * timezone human readable name
     */
    private String displayName;

    /**
     * Returns new <code>Timezone</code> object with populated values.
     */
    public static TimezoneDTO createTimezoneDTO(TimeZone timeZone) {
	TimezoneDTO timezoneDTO = new TimezoneDTO();
	timezoneDTO.timeZoneId = timeZone.getID();
	int timeZoneRawOffset = timeZone.getRawOffset();
	timezoneDTO.rawOffset = new Date(Math.abs(timeZoneRawOffset));
	timezoneDTO.isRawOffsetNegative = timeZoneRawOffset < 0;
	timezoneDTO.dstOffset = timeZone.getDSTSavings() / 60000;
	timezoneDTO.displayName = timeZone.getDisplayName();
	return timezoneDTO;
    }

    public static TimezoneDTO createTimezoneDTO(Timezone timeZone) {
	return TimezoneDTO.createTimezoneDTO(TimeZone.getTimeZone(timeZone.getTimezoneId()));
    }

    public String getTimeZoneId() {
	return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
	this.timeZoneId = timeZoneId;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public Date getRawOffset() {
	return rawOffset;
    }

    public void setRawOffset(Date rawOffset) {
	this.rawOffset = rawOffset;
    }

    public boolean isRawOffsetNegative() {
	return isRawOffsetNegative;
    }

    public void setRawOffsetNegative(boolean isRawOffsetNegative) {
	this.isRawOffsetNegative = isRawOffsetNegative;
    }

    public int getDstOffset() {
	return dstOffset;
    }

    public void setDstOffset(int dstOffset) {
	this.dstOffset = dstOffset;
    }
}
