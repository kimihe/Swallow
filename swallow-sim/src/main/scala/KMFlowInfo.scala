/**
  * Created by zhouqihua on 2017/7/8.
  */

class KMFlowInfo (val flowId: String,

                  val src: KMPort,
                  val dest: KMPort,
                  val totalSize: Long,

                  val arrivedDate: Long,
                  val description: String
                 ) extends Serializable {

}
