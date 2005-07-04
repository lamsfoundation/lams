/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa;


/**
 * 
 * @author Ozgur Demirtas
 
 * 
 * * This is a constant utility class that defines all constants that needs to be 
 * shared around the qa tool.
 * 
 *
 */

/**
 *  This class could be redundant soon and be replaced by QaAppConstants.
 *  Defines application level constants
 */
public class QaConstants
{
    /**
     * Private Construtor to avoid instantiation.
     */
    private QaConstants(){}
    
    /*Currently we are hardcoDing the default content id.
     * This will be replaced when the deploy logic automatically assigns a default content id in the deploy script.
     */
    public static final long DEFAULT_CONTENT_ID = 10; 
    
    public static final int NOT_SHOWN = -1;
    public static final String OPEN_RESPONSE = "Open response";
    
    //-------------------Question Type------------------------//
    /*
    public static final String SIMPLE_CHOICE="simpleChoice";
    public static final String MULTIPLE_CHOICE="choiceMultiple";
    public static final String TEXT_ENTRY="textEntry";
    */
    public static final int NOT_SHOWN_CANDIDATE_ANSWER_ORDER = -1;
    
}
