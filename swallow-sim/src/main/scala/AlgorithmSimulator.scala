/**
  * Created by zhouqihua on 2017/7/8.
  */

// TODO: Remove a flow from flowArrays when it is completed.
// TODO: Update flow size after determining whether compress or not

import scala.util.control.Breaks._

object AlgorithmSimulator {

  def main(args: Array[String]): Unit = {
    val ingress: KMPort = new KMPort(
                              portId = "ingress",
                              portType =  KMPortType.ingress,
                              totalBandwidth =  200,
                              totalCPU =  1,
                              computationSpeed =  400);
    val egress: KMPort  = new KMPort(
                              portId =  "egress",
                              portType = KMPortType.egress,
                              totalBandwidth = 100,
                              totalCPU = 1,
                              computationSpeed = 800);

    val channel: KMChannel = new KMChannel(ingress, egress);


    val flow0 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow0", channel, 400, 0, "this is flow-000000"));
    val flow1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1", channel, 100, 0, "this is flow-000001"));
    val flow2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2", channel, 100, 0, "this is flow-000002"));
    val flow3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3", channel, 200, 0, "this is flow-000003"));
    val flow4 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4", channel, 400, 0, "this is flow-000004"));
    val flow5 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5", channel, 300, 0, "this is flow-000005"));
    val flow6 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6", channel, 500, 0, "this is flow-000006"));
    val flow7 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7", channel, 100, 0, "this is flow-000007"));
    val flow8 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8", channel, 200, 0, "this is flow-000008"));
    val flow9 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9", channel, 300, 0, "this is flow-000009"));



    val flows10: Array[KMFlow] = Array(flow0, flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9);
    val flows3:  Array[KMFlow] = Array(flow1, flow2, flow7);
    val flows2:  Array[KMFlow] = Array(flow1, flow7);
    val flows1:  Array[KMFlow] = Array(flow1);

    /**
      * EXAMPLE 1:
      * compressionRatio = 0.5; computationSpeed = 400 (only consider compression)
      *
      * 1. Execution Time = network time + compression time (dataSIze/200 + dataSize/400);
      * flow0(400) = 2.0 + 1.0  = 3.0;
      * flow1(100) = 0.5 + 0.25 = 0.75;
      * flow2(100) = 0.5 + 0.25 = 0.75;
      * flow3(200) = 1.0 + 0.5  = 1.5;
      * flow4(400) = 2.0 + 1.0  = 3.0;
      * flow5(300) = 1.5 + 0.75 = 2.25;
      * flow6(500) = 2.5 + 1.25 = 3.75;
      * flow7(100) = 0.5 + 0.25 = 0.75;
      * flow8(200) = 1.0 + 0.5  = 1.5
      * flow9(300) = 1.5 + 0.75 = 2.25;
      *
      *
      * 2. sort seq: flow1, flow2, flow7, flow3, flow8, flow5, flow9, flow0, flow4, flow6;
      *
      * 3. FCT = Execution Time (network time + compression time) + Waiting Time (other flows are being compressed and transmitted => Execution Time);
      * flow1 = 0.75 + 0.0 = 0.75;
      * flow2 = 0.75 + 0.75 = 1.5;
      * flow7 = 0.75 + 1.5 = 2.25;
      * flow3 = 1.5 + 2.25 = 3.75;
      * flow8 = 1.5 + 3.75 = 5.25;
      * flow5 = 2.25 + 5.25 = 7.5;
      * flow9 = 2.25 + 7.5 = 9.75;
      * flow0 = 3.0 + 9.75 = 12.75;
      * flow4 = 3.0 + 12.75 = 15.75;
      * flow6 = 3.75 + 15.75 = 19.5;
    */


    val testFlows: Array[KMFlow] = flows10;

    val scheduler: KMScheduler = new KMScheduler;
    scheduler.addNewFlows(testFlows);

    // time slice, simulated with 'while'
    breakable {
      while (true) {
        scheduler.scheduling(timeSlice = 0.01);

        //if all flows completed
        val flag: Boolean = scheduler.flowsDidCompleted;
        if(flag) {
          println("\n************************ Flows Completed !!! ************************\n");
          for (aFlow <- testFlows) {
            println(s"${aFlow.flowInfo.flowId} FCT: ${aFlow.consumedTime}");
          }

          break();
        }
      }
    }
  }

}
