package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import swallow.Util.KMAkkaKit
import swallow.slave.KMReceiver

object ExampleReceiver {
  def main(args: Array[String]): Unit = {

    KMAkkaKit.initActorRefWith("ReceiverSystem", "0.0.0.0", 17202, KMReceiver.props, "receiverActor")
  }
}
