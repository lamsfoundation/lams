import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.mvc.*;

/**
 * Provides basic services for the "view" of
 * a Model/View/Controller triad.
 */
class org.lamsfoundation.lams.common.mvc.AbstractView implements Observer, View {
  private var model:Observable;
  private var controller:Controller;

  public function AbstractView (m:Observable, c:Controller) {
    // Set the model.
    setModel(m);
    // If a controller was supplied, use it. Otherwise let the first
    // call to getController() create the default controller.
    if (c !== undefined) {
      setController(c);
    }
  }

  /**
   * Returns the default controller for this view.
   */
  public function defaultController (model:Observable):Controller {
    return null;
  }

  /**
   * Sets the model this view is observing.
   */
  public function setModel (m:Observable):Void {
    model = m;
  }

  /**
   * Returns the model this view is observing.
   */
  public function getModel ():Observable {
    return model;
  }

  /**
   * Sets the controller for this view.
   */
  public function setController (c:Controller):Void {
    controller = c;
    // Tell the controller this object is its view.
    getController().setView(this);
  }

  /**
   * Returns this view's controller.
   */
  public function getController ():Controller {
     //trace('getController()');
    // If a controller hasn't been defined yet...
    if (controller === undefined) {
      // ...make one. Note that defaultController() is normally overridden 
      // by the AbstractView subclass so that it returns the appropriate
      // controller for the view.
      setController(defaultController(getModel()));
    }
    return controller;
  }

  /**
   * A do-nothing implementation of the Observer interface's
   * update() method. Subclasses of AbstractView will provide
   * a concrete implementation for this method.
   */
  public function update(o:Observable, infoObj:Object):Void {
  }
}