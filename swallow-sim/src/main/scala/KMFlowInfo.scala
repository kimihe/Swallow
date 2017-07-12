/**
  * Created by zhouqihua on 2017/7/8.
  */

class KMFlowInfo (val flowId: String,

                  val ingress: KMPort,
                  val egress: KMPort,
                  val totalSize: Double,

                  val arrivedDate: Long,
                  val description: String
                 ) extends Serializable {

}
