/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

using System;
using System.Collections.Generic;
using System.Text;

namespace LamsSharePointIntegration
{
    /// <summary>
    /// This class map the Microsoft LCIDs to the ISO standard language strings used by LAMS
    /// Author: Luke Foxton
    /// </summary>
    class LAMSLanguageMap
    {
        //1025	Arabic
        //2052	Chinese - Simplified
        //1028	Chinese - Traditional
        //1029	Czech
        //1030	Danish
        //1043	Dutch
        //1033	English
        //1035	Finnish
        //1036	French
        //1031	German
        //1032	Greek
        //1037	Hebrew
        //1038	Hungarian
        //1040	Italian
        //1041	Japanese
        //1042	Korean
        //1044	Norwegian
        //1045	Polish
        //2070	Portuguese
        //1046	Portuguese - Brazilian
        //1049	Russian
        //1034	Spanish
        //1053	Swedish
        //1054	Thai
        //1055	Turkish

        public const uint ARABIC                 = 1025;
        public const uint CHINESE_SIMPLIFIED     = 2052;
        public const uint DANISH                 = 1030;
        public const uint DUTCH                  = 1043;
        public const uint ENGLISH                = 1033;
        public const uint FINNISH                = 1035;
        public const uint FRENCH                 = 1036;
        public const uint GERMAN                 = 1031;
        public const uint GREEK                  = 1032;
        public const uint ITALIAN                = 1040;
        public const uint JAPANESE               = 1041;
        public const uint KOREAN                 = 1042;
        public const uint NORWEGAN               = 1044;
        public const uint POLISH                 = 1045;
        public const uint PORTUGUESE_BRAZIL      = 1046;
        public const uint RUSSIAN                = 1049;
        public const uint SPANISH                = 1034;
        public const uint SWEDISH                = 1053;
        public const uint THAI                   = 1054;        

        public static string[] getLanguageMap(uint lcid)
        {
            string[] ret = new string[2];
            switch (lcid)
            {
                case ARABIC:
                    ret[0] = "ar";
                    ret[1] = "JO";
                    return ret;

                case CHINESE_SIMPLIFIED:
                    ret[0] = "zh";
                    ret[1] = "CN";
                    return ret;

                case DANISH:
                    ret[0] = "da";
                    ret[1] = "DK";
                    return ret; 
               
                case DUTCH:
                    ret[0] = "nl";
                    ret[1] = "BE";
                    return ret;
                   
                case ENGLISH:
                    ret[0] = "en";
                    ret[1] = "AU";
                    return ret;
                    
                case FRENCH:
                    ret[0] = "fr";
                    ret[1] = "FR";
                    return ret;
                    
                case GERMAN:
                    ret[0] = "de";
                    ret[1] = "DE";
                    return ret;
                   
                case GREEK:
                    ret[0] = "el";
                    ret[1] = "GR";
                    return ret;
                    
                case ITALIAN:
                    ret[0] = "ar";
                    ret[1] = "JO";
                    return ret;
                    
                case JAPANESE:
                    ret[0] = "it";
                    ret[1] = "IT";
                    return ret;
                    
                case KOREAN:
                    ret[0] = "ko";
                    ret[1] = "KR";
                    return ret;
                    
                case NORWEGAN:
                    ret[0] = "no";
                    ret[1] = "NO";
                    return ret;
                    
                case PORTUGUESE_BRAZIL:
                    ret[0] = "pt";
                    ret[1] = "BR";
                    return ret;
                    
                case POLISH:
                    ret[0] = "pl";
                    ret[1] = "PL";
                    return ret;
                    break;
                case RUSSIAN:
                    ret[0] = "ru";
                    ret[1] = "RU";
                    return ret;
                    
                case SPANISH:
                    ret[0] = "es";
                    ret[1] = "ES";
                    return ret;
                    break;
                case SWEDISH:
                    ret[0] = "sv";
                    ret[1] = "SE";
                    return ret;
                    
                case THAI:
                    ret[0] = "th";
                    ret[1] = "TH";
                    return ret;
                    
                default:
                    ret[0] = "en";
                    ret[1] = "AU";
                    return ret;
            }
        }
    }
}
