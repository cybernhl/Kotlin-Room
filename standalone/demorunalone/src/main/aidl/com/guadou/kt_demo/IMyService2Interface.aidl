
package com.guadou.kt_demo;
import com.guadou.kt_demo.IMyCallbackInterface;

interface IMyService2Interface {

   void doServiceMethod(String msg);
   void destoryService();

   void registerListener(IMyCallbackInterface listener);

   void unregisterListener(IMyCallbackInterface listener);

}