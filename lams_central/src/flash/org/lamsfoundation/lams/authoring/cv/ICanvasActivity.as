import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*

/**
 * Specifies the minimum services that a canvaas element must provide
 */
interface org.lamsfoundation.lams.authoring.cv.ICanvasActivity {
   
  
  /**
   * Sets the activity for this Canvas Element. If its a complex activity it will get the mainActivity.
   */
  public function getActivity():Activity;

  /**
   * Sets the activity for this Canvas Element.  If its a complex activity it will set the mainActivity.
   */
 public function setActivity(newActivity:Activity);
 
  /**
   * Retrieves the visible width and height of the canvas element, usefull for the transition class
   */
 public function getVisibleWidth():Number;
 
 public function getVisibleHeight():Number;
 
 


}