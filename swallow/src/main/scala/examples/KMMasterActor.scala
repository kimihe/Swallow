package examples

/**
  * Created by zhouqihua on 2017/6/25.
  */

import swallow.core._
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

    val flowId: String = "FLOW-000001"
    val taskId: String = "TASK-000001"
    val master: String = "akka.tcp://masterActor@0.0.0.0:17200/user/masterActor"
    val from: String = "akka.tcp://localActor@0.0.0.0:17201/user/localActor"
    val to: String = "akka.tcp://remoteActor@0.0.0.0:17202/user/remoteActor"
    val content: String = "****** Hello Remote !!! ******"
    val description: String = "Version Beta 0.1"

    val flowInfo = new KMFlowInfo(flowId, taskId, master, from, to, content, description, KMDataType.FAKE)
    val flow = KMFlow.initWithFlowInfo(flowInfo)

    masterActor ! SubmitNewFlow(flow)

    StdIn.readLine()
    system.terminate()

  }
}
object MasterActor {

  final case class SubmitNewFlow(flow: KMFlow)
  final case class AggregateFlow(flow: KMFlow)
}

class MasterActor extends Actor with ActorLogging {
  import LocalActor._
  import MasterActor._

  override def receive: Receive = {

    case SubmitNewFlow(flow: KMFlow) =>
      log.info(s"[MasterActor] submitNewFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val localActor = context.actorSelection(s"${flow.flowInfo.from}")
      localActor ! TransferFlow(flow)

    //      val serialization = SerializationExtension(context.system)
    //      // Have something to serialize
    //      val original = flow.flowInfo
    //
    //      // Find the Serializer for it
    //      val serializer = serialization.findSerializerFor(original)
    //      // Turn it into bytes
    //      val bytes = serializer.toBinary(original)
    //      // Turn it back into an object
    //      //val back = serializer.fromBinary(bytes, manifest = None)
    //
    //      localActor ! TransferFlowSeri(bytes)



    case AggregateFlow(flow: KMFlow) =>
      log.info(s"[MasterActor] aggregateFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")
  }
}

