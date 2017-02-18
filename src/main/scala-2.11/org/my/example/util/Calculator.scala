package org.my.example.util

/**
  * Created by kasper on 18/02/2017.
  * This is created as part of a concurrency example.
  */


class Calculator {
  /**
    * Will take int and return boolean true or false
    * if int is even or odd.
    * even -> true
    * odd -> false
    * @param i
    * @return Boolean
    */
  def oddOrEven(i: Int): Boolean =  {
    this.synchronized {
      val r: Int = i % 2
      if (r == 0) {
        true
      } else {
        false
      }
    }
  }
}




