package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import swallow.Util.KMAkkaKit
import swallow.slave.KMSender

object ExampleSender {
  def main(args: Array[String]): Unit = {

    KMAkkaKit.initActorRefWith("SenderSystem", "0.0.0.0", 17201, KMSender.props, "senderActor")
  }
}
