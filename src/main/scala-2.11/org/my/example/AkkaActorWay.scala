package org.my.example

import akka.actor._

/**
  * Created by kasper on 18/02/2017.
  * This is inspired by the scala actor way of creating a ping pong
  * concurrent demo of scala actors. Here we use the akka actors which
  * should be used since scala ~ 2.10
  *
  */

case object Ping
case object Pong
case object Stop
case object Start

class Pong extends Actor {
  var pongCount = 0
  def receive = {
    case Ping =>
      if (pongCount % 1000 == 0)
        println("Pong: ping!")
      sender ! Pong
      pongCount += 1
    case Stop =>
      println("Pong: stop")
      //Problem getting the actors to stop.
      context.system.stop(self)
      sender ! PoisonPill
  }
}


class Ping(startcount: Int, pong: ActorRef) extends Actor {
  var count: Int = startcount
  def decrement { count -= 1 }
  def receive = {
    case Start =>
      decrement
      pong ! Ping
    case Pong =>
      if (count % 1000 == 0)
        println("Ping: pong!")
      if (count > 0) {
        pong ! Ping
        decrement
      } else {
        println("Ping: stop")
        pong ! Stop
        //Problem getting the actors to stop.
        context.system.stop(self)
        context.system.terminate()
      }
  }
}

object AkkaActorWay extends App {

  val system = ActorSystem("AkkaActorSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(10000, pong)), name = "ping")

  // We make a 'tell' call not a 'ask' which should return a Future[T]
  ping ! Start
}




