package swallow.core

/**
  * Created by zhouqihua on 2017/6/28.
  */

object KMDataType extends Enumeration {
  type DataType = Value

  val FAKE, INMEMORY, ONDISK = Value
}

class KMFlowInfo (
                   val flowId: String,
                   val taskId: String,
                   val master: String,
                   val from: String,
                   val to: String,
                   val content: String,
                   val description: String,
                   val dataType: KMDataType.DataType
                 ) extends Serializable{

}
