/**
 * Dialog class for creating and managing dialogs.
 * Usage - Create a new dialog passing in the linkage ID of the symbol that makes it's contents
 * 
 * @author  DI
 */

import org.lamsfoundation.lams.common.ui.*
import mx.containers.*
import mx.managers.*

class Dialog {
    
//   
    //private static var _dialogInstances:Array;
    //private static var _currentIndex:Number;
//    
    ///**
    //* This will return a reference to the Dialog that is currently in focus. If there are none in focus is will return null
    //*/
    //public static function getDialogWithFocus():Object{
        //_dialogInstances = [];
        //_currentIndex = null;
        //return null;
    //}
//    
//  
    ///**
    //* Constuctor
    //* @param target The target movieclip in which
    //*/
    //function Dialog(target:MovieClip,linkageId:String,x:Number,y:Number,w:Number,h:Number){
        ////Create a new window for the dialog
        //trace('Dialog');
        ////componentInstance.createClassObject(linkageName, instanceName, depth, initObject);
    //}
//

    static function createPopUp(path:MovieClip,cls:Object, initobj:Object):MovieClip{
        return path.createClassChildAtDepth(cls, DepthManager.kTopmost, initobj);
    }

   
}