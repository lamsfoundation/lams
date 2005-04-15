import org.lamsfoundation.lams.common.util.Observable;

/**
 * The interface that must be implemented by all observers of an
 * Observable object.
 */
interface org.lamsfoundation.lams.common.util.Observer {
  /**
   * Invoked automatically by an observed object when it changes.
   * 
   * @param   o   The observed object (an instance of Observable).
   * @param   infoObj   An arbitrary data object sent by 
   *                    the observed object.
   */
  public function update(o:Observable, infoObj:Object):Void;
}