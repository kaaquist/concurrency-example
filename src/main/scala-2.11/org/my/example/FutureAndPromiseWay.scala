package org.my.example
import org.my.example.util.Calculator
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

/**
  * Created by kasper on 18/02/2017.
  */

object FutureAndPromiseWay extends App{
  println("Code example start")
  val calculator = new Calculator()
  val f = Future {
    Thread.sleep(Random.nextInt(100))
    if (calculator.oddOrEven(Random.nextInt(5))) {
      "Future Task: Hello world - Done!"
    } else {
      new IllegalArgumentException("Remainder not zero")
    }
  }

  f.onComplete {
    case Success(s) => println(s"$s")
    case Failure(e) => println(e.printStackTrace.toString + " - Error")
  }
  println("Task 1 ..."); Thread.sleep(80)
  println("Task 2 ..."); Thread.sleep(300)
  println("Task 3 ..."); Thread.sleep(200)
  //This is a  blocking call, but since all other tasks is done there
  //are no risk in doing it. There is also set an await duration of 1 sec.
  Await.result(f,1 second)

  //Example where we use Promise.
  val evenOrOdd = Promise[Boolean]

  case class even(s: Boolean) {println("The number was even !!")}
  case class odd(e: Throwable) {println("The number was odd !!")}

  evenOrOdd.future.onComplete {
    case Success(s) => even(s)
    case Failure(e) => odd(e)
  }

  Future {
    if (calculator.oddOrEven(Random.nextInt(20)))
      evenOrOdd success true
    else
      evenOrOdd failure new NumberFormatException(s"We have an odd number!")
  }

  //This value is something we need to take into account.
  Thread.sleep(1500)
}

