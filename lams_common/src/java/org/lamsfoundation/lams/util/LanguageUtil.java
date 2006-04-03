/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.Locale;

/**
 * Various internationalisation (internationalization) utilities.
 *  
 * @author Fiona Malikoff
 *
 */
public class LanguageUtil {

    /**
     * 
     */
    public LanguageUtil() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        
        String[] arr = null;
        
        Locale defaultLocale = Locale.getDefault();
        if ( defaultLocale != null ) {
            System.out.println("Default locale "+defaultLocale.getCountry()
                    +":"+defaultLocale.getISO3Country()
                    +":"+defaultLocale.getDisplayCountry()
                    +":"+defaultLocale.getLanguage()
                    +":"+defaultLocale.getISO3Language());
        }
        printArray(Locale.getISOCountries(), "2 code countries: ");
        printArray(Locale.getISOLanguages(), "2 code lamguages: ");
    }

    /**
     * @param arr
     * @param comment
     */
    private static void printArray(String[] arr, String comment) {
        System.out.println(comment);
        int i=0; 
        while ( i<arr.length ) {
            System.out.print(arr[i]);
            i++;
            if ( i < arr.length) {
                System.out.print(", ");
            }
        }
        System.out.println(".\n");
    }
}
