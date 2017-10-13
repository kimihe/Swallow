/**
  * Created by zhouqihua on 2017/7/24.
  */



class KMSchedulingResult (val opFlow: KMFlow,
                          val opUsedBandwidth: Long,
                          val opUsedCPU: Long,
                          val opCompressionFlag: Boolean,
                          val opFlowFCT_thisRound: Double,
                          val opCompressionTime: Double,
                          val opBottleneckPort: KMPortType.PortType) {

}
