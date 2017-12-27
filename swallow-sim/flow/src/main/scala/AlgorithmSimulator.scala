/**
  * Created by zhouqihua on 2017/7/8.
  */

// TODO: Remove a flow from flowArrays when it is completed.
// TODO: Update flow size after determining whether compress or not

import scala.util.control.Breaks.{break, breakable}

object AlgorithmSimulator {

  def main(args: Array[String]): Unit = {
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
    val ingress2: KMPort = new KMPort(
                              portId = "ingress2",
                              portType =  KMPortType.ingress,
                              totalBandwidth =  200,
                              totalCPU =  1,
                              computationSpeed =  400);
    val egress2: KMPort  = new KMPort(
                              portId =  "egress2",
                              portType = KMPortType.egress,
                              totalBandwidth = 100,
                              totalCPU = 1,
                              computationSpeed = 800);

    val ingress3: KMPort = new KMPort(
                               portId = "ingress3",
                               portType =  KMPortType.ingress,
                               totalBandwidth =  200,
                               totalCPU =  1,
                               computationSpeed =  400);
    val egress3: KMPort  = new KMPort(
                               portId =  "egress3",
                               portType = KMPortType.egress,
                               totalBandwidth = 100,
                               totalCPU = 1,
                               computationSpeed = 800);


    val channel1: KMChannel = new KMChannel("channel1", ingress1, egress1, "ingress1-egress1");
    val channel2: KMChannel = new KMChannel("channel2", ingress2, egress2, "ingress2-egress2");
    val channel3: KMChannel = new KMChannel("channel3", ingress3, egress3, "ingress3-egress3");


    val flow0_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow0_ch1", channel1, 400, 0, "this is flow-000000"));
    val flow1_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1_ch1", channel1, 100, 0, "this is flow-000001"));
    val flow2_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2_ch1", channel1, 100, 0, "this is flow-000002"));
    val flow3_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3_ch1", channel1, 200, 0, "this is flow-000003"));
    val flow4_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4_ch1", channel1, 400, 0, "this is flow-000004"));
    val flow5_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5_ch1", channel1, 300, 0, "this is flow-000005"));
    val flow6_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6_ch1", channel1, 500, 0, "this is flow-000006"));
    val flow7_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7_ch1", channel1, 100, 0, "this is flow-000007"));
    val flow8_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8_ch1", channel1, 200, 0, "this is flow-000008"));
    val flow9_ch1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9_ch1", channel1, 300, 0, "this is flow-000009"));

    val flow0_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow0_ch2", channel2, 400, 0, "this is flow-000000"));
    val flow1_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1_ch2", channel2, 100, 0, "this is flow-000001"));
    val flow2_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2_ch2", channel2, 100, 0, "this is flow-000002"));
    val flow3_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3_ch2", channel2, 200, 0, "this is flow-000003"));
    val flow4_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4_ch2", channel2, 400, 0, "this is flow-000004"));
    val flow5_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5_ch2", channel2, 300, 0, "this is flow-000005"));
    val flow6_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6_ch2", channel2, 500, 0, "this is flow-000006"));
    val flow7_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7_ch2", channel2, 100, 0, "this is flow-000007"));
    val flow8_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8_ch2", channel2, 200, 0, "this is flow-000008"));
    val flow9_ch2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9_ch2", channel2, 300, 0, "this is flow-000009"));

    val flow0_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow0_ch3", channel3, 400, 0, "this is flow-000000"));
    val flow1_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1_ch3", channel3, 100, 0, "this is flow-000001"));
    val flow2_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2_ch3", channel3, 100, 0, "this is flow-000002"));
    val flow3_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3_ch3", channel3, 200, 0, "this is flow-000003"));
    val flow4_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4_ch3", channel3, 400, 0, "this is flow-000004"));
    val flow5_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5_ch3", channel3, 300, 0, "this is flow-000005"));
    val flow6_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6_ch3", channel3, 500, 0, "this is flow-000006"));
    val flow7_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7_ch3", channel3, 100, 0, "this is flow-000007"));
    val flow8_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8_ch3", channel3, 200, 0, "this is flow-000008"));
    val flow9_ch3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9_ch3", channel3, 300, 0, "this is flow-000009"));


    val flows10_ch1: Array[KMFlow] = Array(flow0_ch1, flow1_ch1, flow2_ch1, flow3_ch1, flow4_ch1, flow5_ch1, flow6_ch1, flow7_ch1, flow8_ch1, flow9_ch1);
    val flows10_ch2: Array[KMFlow] = Array(flow0_ch2, flow1_ch2, flow2_ch2, flow3_ch2, flow4_ch2, flow5_ch2)//, flow6_ch2, flow7_ch2, flow8_ch2, flow9_ch2);
    val flows10_ch3: Array[KMFlow] = Array(flow0_ch3, flow1_ch3, flow2_ch3)//, flow3_ch3, flow4_ch3, flow5_ch3, flow6_ch3, flow7_ch3, flow8_ch3, flow9_ch3);
    val flowsFIFO_ch1:   Array[KMFlow] = Array(flow1_ch1, flow2_ch1, flow7_ch1, flow3_ch1, flow8_ch1, flow5_ch1, flow9_ch1, flow0_ch1, flow4_ch1, flow6_ch1);


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


    val testFlows_ch1: Array[KMFlow] = flows10_ch1;
    val testFlows_ch2: Array[KMFlow] = flows10_ch2;
    val testFlows_ch3: Array[KMFlow] = flows10_ch3;



//    val scheduler: KMSchedulerSFSH = new KMSchedulerSFSH();
//    scheduler.addNewFlows(testFlows_ch1);
//    scheduler.addNewFlows(testFlows_ch2);
//    scheduler.addNewFlows(testFlows_ch3);
//    scheduler.addNewFlows(flowsFIFO_ch1);






    /**
      * Modify Place
      */

    val traces1:    Array[Double] = Array(400, 100, 100, 200, 400, 300, 500, 100, 200, 300);
    val traces2:    Array[Double] = Array(400, 100, 100, 200, 400, 300)//, 500, 100, 200, 300);
    val traces3:    Array[Double] = Array(400, 100, 100)//, 200, 400, 300, 500, 100, 200, 300);

    val flows1: Array[KMFlow] = KMTraceGenerator.generateFlowsCustom(channel = channel1, traces = traces1);
    val flows2: Array[KMFlow] = KMTraceGenerator.generateFlowsCustom(channel = channel2, traces = traces2);
    val flows3: Array[KMFlow] = KMTraceGenerator.generateFlowsCustom(channel = channel3, traces = traces3);

//    val scheduler: KMSchedulerFIFO = new KMSchedulerFIFO();
//    val scheduler: KMSchedulerSRTF = new KMSchedulerSRTF();
    val scheduler: KMSchedulerSFSH = new KMSchedulerSFSH();
//    val scheduler: KMSchedulerFAIR = new KMSchedulerFAIR()

    scheduler.addNewFlows(flows1);
//    scheduler.addNewFlows(flows2);
//    scheduler.addNewFlows(flows3);



    println("\n************************ Flows Are Scheduling ... Please Wait ... ************************\n");

    // time slice, simulated with 'while'
    breakable {
      while (true) {
        scheduler.scheduling(timeSlice = 0.01);
//        scheduler.schedulingInOneChannel(channel1)  //For FAIR


        //if all flows completed
        if(scheduler.allFlowsIsCompleted) {
          println("\n************************ All Flows Are Completed !!! ************************\n");
          scheduler.printCompletedFlowsInOrderPrettyily();

          break();
        }
      }
    }
  }

}
