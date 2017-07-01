package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import swallow.Util.KMAkkaKit

import scala.io.StdIn
import swallow.core.{KMDataType, KMFlow, KMFlowInfo}
import swallow.master.{KMJobDispatcher, KMMaster}

object ExampleMaster {
  def main(args: Array[String]): Unit = {

    val masterSystem = KMAkkaKit.initActorSystemWith("MasterSystem", "0.0.0.0", 17200)
    KMAkkaKit.initActorRefWith(masterSystem, KMMaster.props, "masterActor")

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


    Thread.sleep(5000)
    println("****** Press Enter To Continue. ******")
    StdIn.readLine()

    KMJobDispatcher.dispatchNewFlow(masterSystem, flow)

    StdIn.readLine()
    masterSystem.terminate()
  }
}
