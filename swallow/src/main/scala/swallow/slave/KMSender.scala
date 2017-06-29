package swallow.slave

/**
  * Created by zhouqihua on 2017/6/29.
  */

import swallow.core._

object KMSender {

  def main(args: Array[String]): Unit = {

  }
}

class KMSender {

}

object KMSenderActor {
  final case class FLOW(flow: KMFlow)
}

class KMSenderActor (slaveType: KMSlaveType.SlaveType) extends KMNode {

}
