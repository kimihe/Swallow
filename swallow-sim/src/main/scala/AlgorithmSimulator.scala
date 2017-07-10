/**
  * Created by zhouqihua on 2017/7/8.
  */

import scala.util.control.Breaks._

object AlgorithmSimulator {

  def main(args: Array[String]): Unit = {
    val ingress = new KMPort(
                              portId = "ingress",
                              portType =  KMPortType.ingress,
                              totalBandwidth =  1000,
                              totalCPU =  1,
                              remBandwidth =  800,
                              remCPU =  1,
                              computationSpeed =  350);
    val egress  = new KMPort("egress", KMPortType.egress, 1000, 1, 500, 1, 700);


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


    //when received msg, simulated with 'while'
    while (true) {
      schedulingFlows(timeSlice = 0.1, flows, ingress, egress);

      //if 10 flows completed
      val flag: Boolean = flowsDidCompleted(flows);
      if(flag) {
        println("****** Flows Completed !!! ******");
        break;
      }
    }







  }


  def flowsDidCompleted(flows: Array[KMFlow]): Boolean = {

    var flag: Boolean = true;
    for (aFlow <- flows) {
      if (!aFlow.isCompleted) {
        flag = false;
        break;
      }
    }

    return flag;
  }

  def flowTrafficInOneTimeSlice(timeSlice: Double, usedBandwidth: Long): Double = {
    val traffic = timeSlice * usedBandwidth;

    return traffic;
  }

  def SFSH(flows: Array[KMFlow]): Tuple3[KMFlow, Long, Long] = {

    // calculate and sort


    val flow: KMFlow = null;
    val usedBandwidth: Long = 0;
    val usedCPU: Long = 0;

//    val res: Map[String, Any] = Map("flow" -> flow, "usedBandwidth" -> usedBandwidth, "usedCPU" -> usedCPU);
    val res: Tuple3[KMFlow, Long, Long] = (flow, usedBandwidth, usedCPU);

    return res;
  }

  def schedulingFlows(timeSlice: Double, flows: Array[KMFlow], ingress: KMPort, egress: KMPort): Unit = {

    while (ingress.isBandwidthFree && egress.isBandwidthFree) {

      // sort with SFSH(Simple Flow Scheduling Heuristic)

//      val aMap: Map[String, Any] = SFSH(flows);
//      val aFlow: KMFlow = aMap.get("flow");
//      val usedBandwidth: Long = aMap.get("usedBandwidth");
//      val usedCPU: Long = aMap.get("usedCPU");


      val aTuple: Tuple3[KMFlow, Long, Long] = SFSH(flows);
      val aFlow: KMFlow = aTuple._1;
      val usedBandwidth: Long = aTuple._2;
      val usedCPU: Long = aTuple._3;

      val flowTraffic: Double = flowTrafficInOneTimeSlice(timeSlice, usedBandwidth);

      aFlow.updateFlowWith(finishedSize = flowTraffic, usedBandwidth = usedBandwidth, usedCPU = usedCPU);
      ingress.updatePortWithFlow(aFlow);
      egress.updatePortWithFlow(aFlow);
    }
  }
}
