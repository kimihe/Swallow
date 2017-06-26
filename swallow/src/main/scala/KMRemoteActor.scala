/**
  * Created by zhouqihua on 2017/6/25.
  */

import com.typesafe.config._
import akka.actor._

object KMRemoteActor {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("remoteActor", config.getConfig("remoteActor").withFallback(config))
    val remoteActor = system.actorOf(Props[RemoteActor], name = "remoteActor")
  }
}

object RemoteActor {
  final case class ReceiveFlow(taskId: String, master: String, from: String, to: String, content: String, description: String)
}

class RemoteActor extends Actor with ActorLogging{
  import LocalActor._
  import RemoteActor._

  override def receive: Receive = {
    case ReceiveFlow(taskId, master, from, to, content, description) =>
      log.info(s"[RemoteActor] receiveFlow; From sender: $sender")
      log.info(s"[Flow Info] from: $from; to: $to; content: $content")

      val localActor = context.actorSelection(s"$from")
      localActor ! CompleteFlow(taskId, master, from, to, "****** Flow Completed !!! ******", description)
  }
}

