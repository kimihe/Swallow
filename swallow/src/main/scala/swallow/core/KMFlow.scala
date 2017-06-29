package swallow.core

/**
  * Created by zhouqihua on 2017/6/28.
  */

object KMFlow {
  def initWithFlowInfo(flowInfo: KMFlowInfo): KMFlow = new KMFlow(flowInfo)
}

class KMFlow (val flowInfo: KMFlowInfo) extends Serializable{

}
