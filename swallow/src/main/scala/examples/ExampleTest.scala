package examples

import akka.actor._
import swallow.Util.KMAkkaKit
import swallow.core.KMActorMessages.{ReceiverGetFlow, TestFlow}
import swallow.core.{KMDataType, KMFlow, KMFlowInfo, KMNode}

import scala.io.StdIn

/**
  * Created by zhouqihua on 2017/7/1.
  */
object ExampleTest {

  def main(args: Array[String]): Unit = {

    val testActor =  KMAkkaKit.initActorRefWith("TestSystem", "0.0.0.0", 17233, Props(new ExampleTest), "testActor")

    val flowId: String = "FLOW-000001"
    val taskId: String = "TASK-000001"
    val master: String = "akka.tcp://MasterSystem@0.0.0.0:17200/user/masterActor"
    val from: String = "akka.tcp://SenderSystem@0.0.0.0:17201/user/senderActor"
    val to: String = "akka.tcp://ReceiverSystem@0.0.0.0:17202/user/receiverActor"
    val content: String = "****** Hello Akka !!! ******"
    val description: String = "Version 0.4-SNAPSHOT"
    val dataType: KMDataType.DataType = KMDataType.FAKE

    val flowInfo = new KMFlowInfo(flowId, taskId, master, from, to, content, description, dataType)
    val flow = KMFlow.initWithFlowInfo(flowInfo)


    StdIn.readLine()
    testActor ! TestFlow(flow)
  }

}

class ExampleTest extends Actor with ActorLogging {
  override def receive: Receive = {
    case TestFlow(flow) =>
      log.info(s"[LocalActor] transferFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val remoteActor = context.actorSelection(s"${flow.flowInfo.to}")
      remoteActor ! ReceiverGetFlow(flow)
  }
}
