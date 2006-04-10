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

import org.lamsfoundation.lams.common.util.Observer;

/**
 * A Java-style Observable class used to represent the "subject"
 * of the Observer design pattern. Observers must implement the Observer
 * interface, and register to observe the subject via addObserver().
 */
class org.lamsfoundation.lams.common.util.Observable {
  // A flag indicating whether this object has changed.
  private var changed:Boolean = false;
  // A list of observers.
  private var observers:Array;

  /**
   * Constructor function.
   */
  public function Observable () {
    observers = new Array();
  }

  /**
   * Adds an observer to the list of observers.
   * @param   o   The observer to be added.
   */
  public function addObserver(o:Observer):Boolean {
    // Can't add a null observer.
    if (o == null) {
      return false;
    }

    // Don't add an observer more than once.
    for (var i:Number = 0; i < observers.length; i++) {
      if (observers[i] == o) {
        // The observer is already observing, so quit.
        return false;
      }
    }

    // Put the observer into the list.
    observers.push(o);
    return true;
  }

  /**
   * Removes an observer from the list of observers.
   *
   * @param   o   The observer to remove.
   */
  public function removeObserver(o:Observer):Boolean {
    // Find and remove the observer.
    var len:Number = observers.length;
    for (var i:Number = 0; i < len; i++) {
      if (observers[i] == o) {
        observers.splice(i, 1);
        return true;
      }
    }
    return false;
  }

  /**
   * Tell all observers that the subject has changed.
   *
   * @param   infoObj   An object containing arbitrary data 
   *                    to pass to observers.
   */
  public function notifyObservers(infoObj:Object):Void {
    // Use a null infoObject if none is supplied.
    if (infoObj == undefined) {
      infoObj = null;
    }

    // If the object hasn't changed, don't bother notifying observers.
    if (!changed) {
      return;
    }

    // Make a copy of the observers array. We do this
    // so that we can be sure the list won't change while
    // we're processing it.
    var observersSnapshot:Array = observers.slice(0);

    // This change has been processed, so unset the "changed" flag.
    clearChanged();

    // Invoke update() on all observers.
    for (var i:Number = observersSnapshot.length-1; i >= 0; i--) {
      observersSnapshot[i].update(this, infoObj);
    }
  }

  /**
   * Removes all observers from the observer list.
   */
  public function clearObservers():Void {
    observers = new Array();
  }

  /**
   * Indicates that the subject has changed.
   */
  private function setChanged():Void {
    changed = true;
  }

  /**
   * Indicates that the subject has either not changed or
   * has notified its observers of the most recent change.
   */
  private function clearChanged():Void {
    changed = false;
  }

  /**
   * Checks if the subject has changed.
   *
   * @return   true if the subject has changed, as determined by setChanged().
   */
  public function hasChanged():Boolean {
    return changed;
  }

  /**
   * Returns the number of observers in the observer list.
   *
   * @return   An integer: the number of observers for this subject.
   */
  public function countObservers():Number {
    return observers.length;
  }
}