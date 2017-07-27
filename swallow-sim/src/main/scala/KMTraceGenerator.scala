/**
  * Created by zhouqihua on 2017/7/27.
  */

import scala.collection.mutable.{Set}

object KMTraceGenerator {
  def generateFlows(): Set[KMFlow] = {

    val ingress1: KMPort = new KMPort(
      portId = "ingress1",
      portType =  KMPortType.ingress,
      totalBandwidth =  200,
      totalCPU =  1,
      computationSpeed =  400);
    val egress1: KMPort  = new KMPort(
      portId =  "egress1",
      portType = KMPortType.egress,
      totalBandwidth = 100,
      totalCPU = 1,
      computationSpeed = 800);

    val channel1: KMChannel = new KMChannel("channel1", ingress1, egress1, "ingress1-egress1");
    val flows: Set[KMFlow] = Set[KMFlow]();


    /**
      * Modify Place
      */
    val traceArr: Array[Double] = Array(
      400, 100, 100, 200, 400, 300, 500, 100, 200, 300
    );



    var i: Long = 1;
    for (aTrace <- traceArr) {
      val flowId: String = s"flow$i";
      val flowDescription = s"$flowId: $aTrace";
      val aFlow: KMFlow = KMFlow.initWithFlowInfo(new KMFlowInfo(flowId = flowId,
                                                                  channel = channel1,
                                                                  totalSize = aTrace,
                                                                   arrivedDate = 0,
                                                                   flowDescription = flowDescription));
      flows += aFlow;
      i += 1;
    }

    return flows;
  }
}


class KMTraceGenerator {

}
