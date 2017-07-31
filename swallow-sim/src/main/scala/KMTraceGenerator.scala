/**
  * Created by zhouqihua on 2017/7/27.
  */

import scala.collection.mutable.{ArrayBuffer}

object KMTraceGenerator {
  def generateFlowsDeafault(): Array[KMFlow] = {

    val ingress: KMPort = new KMPort(
      portId = "ingress-default",
      portType =  KMPortType.ingress,
      totalBandwidth =  200,
      totalCPU =  1,
      computationSpeed =  400);
    val egress: KMPort  = new KMPort(
      portId =  "egress-default",
      portType = KMPortType.egress,
      totalBandwidth = 100,
      totalCPU = 1,
      computationSpeed = 800);

    val channel: KMChannel = new KMChannel("channel-default", ingress, egress, "ingress-egress-default");
    val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();

    /**
      * Default Traces
      */
    val traces: Array[Double] = Array(
      400, 100, 100, 200, 400, 300, 500, 100, 200, 300
    );



    var i: Long = 0;
    for (aTrace <- traces) {
      val flowId: String = s"flow$i-${channel.channelId}";
      val flowDescription = s"flowId:$flowId, flowSize: $aTrace, channelDesc: ${channel.channelDesciption}";

      val aFlow: KMFlow = KMFlow.initWithFlowInfo(new KMFlowInfo(flowId           = flowId,
                                                                 channel          = channel,
                                                                 totalSize        = aTrace,
                                                                 arrivedDate      = 0,
                                                                 flowDescription  = flowDescription));
      flows += aFlow;
      i += 1;
    }

    return flows.toArray;
  }

  def generateFlowsCustom(channel: KMChannel, traces: Array[Double]): Array[KMFlow] = {
    val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();

    var i: Long = 0;
    for (aTrace <- traces) {
      val flowId: String = s"flow$i-${channel.channelId}";
      val flowDescription = s"flowId:$flowId, flowSize: $aTrace, channelDesc: ${channel.channelDesciption}";

      val aFlow: KMFlow = KMFlow.initWithFlowInfo(new KMFlowInfo(flowId           = flowId,
                                                                 channel          = channel,
                                                                 totalSize        = aTrace,
                                                                 arrivedDate      = 0,
                                                                 flowDescription  = flowDescription));
      flows += aFlow;
      i += 1;
    }

    return flows.toArray;
  }
}


class KMTraceGenerator {

}
