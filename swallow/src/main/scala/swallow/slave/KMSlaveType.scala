package swallow.slave

/**
  * Created by zhouqihua on 2017/6/29.
  */

object KMSlaveType extends Enumeration {
  type SlaveType = Value

  val Sender, Receiver, Other = Value
}
