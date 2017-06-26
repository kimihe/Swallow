/**
  * Created by zhouqihua on 2017/6/25.
  */

import akka.actor._
import com.typesafe.config._

import scala.io.StdIn

object KMMasterActor {

  import MasterActor._

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("masterActor", config.getConfig("masterActor").withFallback(config))
    val masterActor = system.actorOf(Props[MasterActor], name = "masterActor")


    StdIn.readLine()

    val taskId: String = "FLOW-0000001"
    val master: String = "akka.tcp://masterActor@0.0.0.0:17200/user/masterActor"
    val from: String = "akka.tcp://localActor@0.0.0.0:17201/user/localActor"
    val to: String = "akka.tcp://remoteActor@0.0.0.0:17202/user/remoteActor"
    val content: String = "****** Hello Remote !!! ******"
    val description: String = "Version Beta 0.1"

    masterActor ! SubmitNewFlow(taskId, master, from, to, content, description)

    StdIn.readLine()
    system.terminate()

  }
}
object MasterActor {
  final case class SubmitNewFlow(taskId: String, master: String, from: String, to: String, content: String, description: String)
  final case class AggregateFlow(taskId: String, master: String, from: String, to: String, content: String, description: String)
}

class MasterActor extends Actor with ActorLogging {
  import LocalActor._
  import MasterActor._

  override def receive: Receive = {

    case SubmitNewFlow(taskId, master, from, to, content, description) =>
      log.info(s"[MasterActor] submitNewFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: $from; to: $to; content: $content")

      val localActor = context.actorSelection(s"$from")
      localActor ! TransferFlow(taskId, master, from, to, content, description)

    case AggregateFlow(taskId, master, from, to, content, description) =>
      log.info(s"[MasterActor] aggregateFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: $from; to: $to; content: $content")
  }
}
