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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.mvc.*;

/**
 * Specifies the minimum services that the "controller" of
 * a Model/View/Controller triad must provide.
 */
interface org.lamsfoundation.lams.common.mvc.Controller {
  /**
   * Sets the model for this controller.
   */
  public function setModel (m:Observable):Void;

  /**
   * Returns the model for this controller.
   */
  public function getModel ():Observable;

  /**
   * Sets the view this controller is servicing.
   */
  public function setView (v:View):Void;

  /**
   * Returns this controller's view.
   */
  public function getView ():View;
}