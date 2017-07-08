/**
  * Created by zhouqihua on 2017/7/8.
  */
object AlgorithmSimulator {

  def main(args: Array[String]): Unit = {
    val ingress = new KMPort("ingress", 1000, 1, 800, 1, 300)
    val egress  = new KMPort("egress",  1000, 1, 500, 1, 300)


    val flow1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1", ingress, egress, 2500, 0, "this is flow-000001"));
    val flow2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2", ingress, egress, 3000, 0, "this is flow-000002"));
    val flow3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3", ingress, egress, 3500, 0, "this is flow-000003"));
    val flow4 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4", ingress, egress, 4000, 0, "this is flow-000004"));
    val flow5 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5", ingress, egress, 4500, 0, "this is flow-000005"));
    val flow6 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6", ingress, egress, 5000, 0, "this is flow-000006"));
    val flow7 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7", ingress, egress, 1000, 0, "this is flow-000007"));
    val flow8 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8", ingress, egress, 2000, 0, "this is flow-000008"));
    val flow9 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9", ingress, egress, 3000, 0, "this is flow-000009"));
    val flow10 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow10", ingress, egress, 4000, 0, "this is flow-0000010"));


    val flows: Array[KMFlow] = Array(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10);


    //when received msg
    schedulingFlows(flows, ingress, egress);

    //if 10 flows completed
    if(flow1.isCompleted) {
      println("****** Flows Completed !!! ******");
    }

  }


  def SFSH(flows: Array[KMFlow]): KMFlow = {

    val flow: KMFlow = null;
    return flow;
  }

  def schedulingFlows(flows: Array[KMFlow], ingress: KMPort, egress: KMPort): Unit = {

    while (ingress.isBandwidthFree && egress.isBandwidthFree) {

      // sort with SFSH(Simple Flow Scheduling Heuristic)
      val aFlow = SFSH(flows);

      aFlow.updateFlowWith(0, 0, 0);
      ingress.updatePortWithFlow(aFlow);
      egress.updatePortWithFlow(aFlow);
    }
  }
}
