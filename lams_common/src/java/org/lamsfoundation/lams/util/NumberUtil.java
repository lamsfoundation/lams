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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.util;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Various internationalisation (internationalization) utilities for numbers
 *
 * @author Fiona Malikoff
 *
 */
public class NumberUtil {

    private static Locale getServerLocale() {
	String defaults[] = LanguageUtil.getDefaultLangCountry();
	return new Locale(defaults[0] != null ? defaults[0] : "", defaults[1] != null ? defaults[1] : "");
    }

    /**
     * Format a given float or double to the I18N format specified by the locale, with a fixed number of decimal places. 
     * If numFractionDigits is 2: 4.051 -> 4.05, 4 -> 4.00
     *
     * @param mark
     * @param locale
     * @param numFractionDigits
     * @return
     */
    public static String formatLocalisedNumberForceDecimalPlaces(Number number, Locale locale, int numFractionDigits) {
	NumberFormat format = null;
	if (locale == null) {
	    format = NumberFormat.getInstance(NumberUtil.getServerLocale());
	} else {
	    format = NumberFormat.getInstance(locale);
	}
	format.setMinimumFractionDigits(numFractionDigits);
	return format.format(number);
    }


    /**
     * Format a given float or double to the I18N format specified by the locale. If no locale supplied, uses LAMS
     * server's default locale.
     * 
     * Will set it to numFractionDigits decimal places if needed. If numFractionDigits is 2: 4.051 -> 4.05, 4 stays as 4.
     *
     * @param mark
     * @param locale
     * @param numFractionDigits
     * @return
     */
    public static String formatLocalisedNumber(Number number, Locale locale, int numFractionDigits) {
	NumberFormat format = null;
	if (locale == null) {
	    format = NumberFormat.getInstance(NumberUtil.getServerLocale());
	} else {
	    format = NumberFormat.getInstance(locale);
	}
	return NumberUtil.formatLocalisedNumber(number, format, numFractionDigits);
    }

    /**
     * Format a given float or double to the I18N format specified by the numberFormat. If no numberFormat supplied,
     * uses LAMS server's default numberFormat based on the server locale.
     *
     * @param mark
     * @param locale
     * @param numFractionDigits
     * @return
     */
    public static String formatLocalisedNumber(Number number, NumberFormat numberFormat, int numFractionDigits) {
	NumberFormat format = numberFormat != null ? numberFormat : NumberFormat.getInstance();
	format.setMaximumFractionDigits(numFractionDigits);
	return format.format(number);
    }

    /**
     * Convert a string back into a Float. Assumes string was formatted using formatLocalisedNumber originally. Should
     * ensure that it is using the same locale/number format as when it was formatted. If no locale is suppied, it will
     * use the server's locale
     *
     * Need to strip out any spaces as spaces are valid group separators in some European locales (e.g. Polish) but they
     * seem to come back from Firefox as a plain space rather than the special separating space.
     */
    public static Float getLocalisedFloat(String inputStr, Locale locale) {
	String numberStr = inputStr;
	if (numberStr != null) {
	    numberStr = numberStr.replace(" ", "");
	}
	if ((numberStr != null) && (numberStr.length() > 0)) {
	    Locale useLocale = locale != null ? locale : NumberUtil.getServerLocale();
	    NumberFormat format = NumberFormat.getInstance(useLocale);
	    ParsePosition pp = new ParsePosition(0);
	    Number num = format.parse(numberStr, pp);
	    if ((num != null) && (pp.getIndex() == numberStr.length())) {
		return num.floatValue();
	    }
	}
	throw new NumberFormatException("Unable to convert number " + numberStr + "to float using locale "
		+ locale.getCountry() + " " + locale.getLanguage());
    }

    /**
     * Convert a string back into a Float. Assumes string was formatted using formatLocalisedNumber originally. Should
     * ensure that it is using the same locale/number format as when it was formatted. If no locale is suppied, it will
     * use the server's locale.
     *
     * Need to strip out any spaces as spaces are valid group separators in some European locales (e.g. Polish) but they
     * seem to come back from Firefox as a plain space rather than the special separating space.
     */
    public static Double getLocalisedDouble(String inputStr, Locale locale) {
	String numberStr = inputStr;
	if (numberStr != null) {
	    numberStr = numberStr.replace(" ", "");
	}
	if ((numberStr != null) && (numberStr.length() > 0)) {
	    Locale useLocale = locale != null ? locale : NumberUtil.getServerLocale();
	    NumberFormat format = NumberFormat.getInstance(useLocale);
	    ParsePosition pp = new ParsePosition(0);
	    Number num = format.parse(numberStr, pp);
	    if ((num != null) && (pp.getIndex() == numberStr.length())) {
		return num.doubleValue();
	    }
	}
	throw new NumberFormatException("Unable to convert number " + numberStr + "to double using locale "
		+ locale.getCountry() + " " + locale.getLanguage());
    }

}
