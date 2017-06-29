package swallow.core

/**
  * Created by zhouqihua on 2017/6/28.
  */

import java.util.Date

object KMTaskType extends Enumeration {
  type TaskType = Value

  val DEFAULT, SHUFFLE, BROADCAST, INCAST, ANYCAST = Value
}

class KMTaskInfo (val taskId: String,
                  val startTime: Long,
                  val submitDate: Date,

                  val name: String,
                  val taskType: KMTaskType.TaskType,  // http://www.scala-lang.org/node/7661
                  val maxFlows: Int,  // Upper-bound on the number of flows
                  val maxSizeInBytes: Long,  // Upper-bound on coflow size
                  val deadlineMillis: Long = 0,  // Greater than 0 is valid deadline

                  val containedFlows: Array[KMFlow])
                  extends Serializable {

}
