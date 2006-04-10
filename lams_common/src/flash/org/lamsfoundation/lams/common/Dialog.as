/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.*
/*
* Dialog Interface - This ensures that Theme, Resize + Close events are handled in Dialog classes
* @author   DI
* @usage    
*           import org.lamsfoundation.lams.common.*
*           class MyDialog implements Dialog{....
*/
interface Dialog {
    //Called by Parent LFWindow when it is resized
    public function setSize(w:Number,h:Number):Void;
    
    //Click handler for close button of LFWindow    
    public function click(e:Object):Void;

    //Handler for theme changes broadcast from Theme Manager
    public function themeChanged(event:Object):Void;
    
}
