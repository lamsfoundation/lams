package org.lamsfoundation.lams.common.util
{
   import flash.utils.Dictionary;
   
   import mx.core.IFactory;

   public class ArgumentsToRendererFactory implements IFactory
   {
      private var klass:Class;
      private var args:*;
      
      public function ArgumentsToRendererFactory(args:*,klass:Class) {
         this.args=args;
         this.klass=klass;
      }
      
      public function newInstance():*
      {
         var instance:Object = new klass;
         

         for (var key:String in args) {
            if (instance.hasOwnProperty(key)) {
               instance[key] = args[key];
            }
         }
         
         return instance;   
      }
   }
}