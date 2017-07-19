/**
  * Created by zhouqihua on 2017/7/8.
  */

// TODO: Remove a flow from flowArrays when it is completed.
// TODO: Update flow size after determining whether compress or not

import scala.util.control.Breaks._

object AlgorithmSimulator {

  def main(args: Array[String]): Unit = {
    val ingress = new KMPort(
                              portId = "ingress",
                              portType =  KMPortType.ingress,
                              totalBandwidth =  200,
                              totalCPU =  1,
                              computationSpeed =  400);
    val egress  = new KMPort(
                              portId =  "egress",
                              portType = KMPortType.egress,
                              totalBandwidth = 100,
                              totalCPU = 1,
                              computationSpeed = 800);


    val flow1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1", ingress, egress, 100, 0, "this is flow-000001"));
    val flow2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2", ingress, egress, 100, 0, "this is flow-000002"));
    val flow3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3", ingress, egress, 200, 0, "this is flow-000003"));
    val flow4 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4", ingress, egress, 400, 0, "this is flow-000004"));
    val flow5 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5", ingress, egress, 450, 0, "this is flow-000005"));
    val flow6 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6", ingress, egress, 500, 0, "this is flow-000006"));
    val flow7 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7", ingress, egress, 100, 0, "this is flow-000007"));
    val flow8 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8", ingress, egress, 200, 0, "this is flow-000008"));
    val flow9 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9", ingress, egress, 300, 0, "this is flow-000009"));
    val flow10 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow10", ingress, egress, 400, 0, "this is flow-0000010"));


    val flows10: Array[KMFlow] = Array(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10);
    val flows3:  Array[KMFlow] = Array(flow1, flow2, flow7);
    val flows2:  Array[KMFlow] = Array(flow1, flow7);
    val flows1:  Array[KMFlow] = Array(flow1);

    /**
      * EXAMPLE 1:
      * 1. compeletion time = network time + compression time;
      * flow1 = 0.5 + 0.25 = 0.75;
      * flow2 = 0.5 + 0.25 = 0.75;
      * flow3 = 1.0 + 0.5 = 1.5;
      *
      * 2. sort seq: flow1, flow2, flow3;
      *
      * 3. Final FCT = network time + compression time + waiting time(other flows are being transmitted);
      * flow1 = 0.75 + 0.0 = 0.75;
      * flow2 = 0.75 + 0.5 = 1.25;
      * flow3 = 1.5 +(0.5 + 0.5) = 2.5;
    */
    val testFlows: Array[KMFlow] = flows10//Array(flow3, flow1, flow2);


    //when received msg, simulated with 'while'
    var iterationsNumber: Long = 0;
    breakable {
      while (true) {
        schedulingFlows(timeSlice = 0.1, testFlows, ingress, egress, iterationsNumber);
        iterationsNumber = iterationsNumber+1;

        //if all flows completed
        val flag: Boolean = flowsDidCompleted(testFlows);
        if(flag) {
          println("************ Flows Completed !!! ************");
          for (aFlow <- testFlows) {
            println(s"$aFlow FCT: ${aFlow.consumedTime}");
          }

          break();
        }
      }
    }
  }





  def flowsDidCompleted(flows: Array[KMFlow]): Boolean = {

    var flag: Boolean = true;
    // pass a function to the breakable method
    breakable {
      for (aFlow <- flows) {
        if (!aFlow.isCompleted) {
          flag = false;
          break();
        }
      }
    }

    return flag;
  }

  def flowTrafficInOneTimeSlice(timeSlice: Double, usedBandwidth: Long): Double = {
    val traffic = timeSlice * usedBandwidth;

    return traffic;
  }


  /**
    * calculate completion time and sort
    */
  def SFSH(flows: Array[KMFlow]): Tuple7[KMFlow, Long, Long, Boolean, Double, Double, KMPortType.PortType] = {

    // optimal(op) flow, bandwidth and CPU
    var opFlow: KMFlow                        = null;
    var opUsedBandwidth: Long                 = 0;
    var opUsedCPU: Long                       = 0;
    var opCompressionFlag: Boolean            = false;
    var opFlowFCT_thisRound: Double           = Double.MaxValue;
    var opCompressionTime: Double             = 0.0;
    var opBottleneckPort: KMPortType.PortType = KMPortType.other;



    // iteration
    for (aFlow <- flows) {
      breakable {

        if (aFlow.isCompleted)
          break();   // continue


        // init variables
        var bnBandwidth: Long           = 0;
        var usedCPU                     = 0;
        var compressionFlag: Boolean    = false;
        var FCT: Double                 = 0;
        var compressionTime: Double     = 0.0;
        var bnPort: KMPortType.PortType = KMPortType.other;



        // bandwidth bottleneck(bn)
        val ingressBandwidth: Long = aFlow.flowInfo.ingress.remBandwidth;
        val egressBandwidth: Long  = aFlow.flowInfo.egress.remBandwidth;

        if (ingressBandwidth <= egressBandwidth) {
          bnBandwidth = ingressBandwidth;
          bnPort = KMPortType.ingress;
        }
        else {
          bnBandwidth = egressBandwidth;
          bnPort = KMPortType.egress;
        }

        if (bnBandwidth == 0)
          break(); // equivalent to 'continue'



        // TODO: If a flow is compressed, do not compress it again
        if (aFlow.hasBeenCompressed) {
          FCT = aFlow.remSize / bnBandwidth;
          compressionFlag = false;
          compressionTime = 0.0;
        }
        else {
          // compltion time under uncompression
          val T_uc_i: Double = aFlow.remSize / bnBandwidth;
          val T_uc_j: Double = aFlow.remSize / bnBandwidth;

          // completion time under compression
          val T_c_i: Double  = (aFlow.remSize * aFlow.compressionRatio) / bnBandwidth +
            aFlow.remSize / aFlow.flowInfo.ingress.computationSpeed;
          val T_c_j: Double  = (aFlow.remSize * aFlow.compressionRatio) / bnBandwidth +
            aFlow.remSize / aFlow.flowInfo.egress.computationSpeed;

          // comparison of compression and uncompression
          val T_c_max: Double  = math.max(T_c_i, T_c_j);
          val T_uc_max: Double = math.max(T_uc_i, T_uc_j);

          if (T_c_max <= T_uc_max) {
            FCT = T_c_max;
            compressionFlag = true;
            compressionTime = aFlow.remSize / aFlow.flowInfo.ingress.computationSpeed;
          }
          else {
            FCT = T_uc_max;
            compressionFlag = false;
            compressionTime = 0.0;
          }
        }





        // update and select
        if (FCT < opFlowFCT_thisRound) {

          opFlow              = aFlow;
          opUsedBandwidth     = bnBandwidth;
          opUsedCPU           = usedCPU;
          opCompressionFlag   = compressionFlag;
          opFlowFCT_thisRound = FCT;
          opCompressionTime   = compressionTime;
          opBottleneckPort    = bnPort;
        }
      }
    }




    val res: Tuple7[KMFlow, Long, Long, Boolean, Double, Double, KMPortType.PortType] = (
      opFlow, opUsedBandwidth, opUsedCPU, opCompressionFlag, opFlowFCT_thisRound, opCompressionTime, opBottleneckPort);

    return res;
  }

  def schedulingFlows(timeSlice: Double, flows: Array[KMFlow], ingress: KMPort, egress: KMPort, iterationsNumber: Long): Unit = {

    // each scheduling time point reset all resources
    ingress.resetPort;
    egress.resetPort;
    for (aFlow <- flows) {
      aFlow.resetFlow;
    }

    while (ingress.isBandwidthFree && egress.isBandwidthFree) {

      // TODO: How to calculate the consumed time?
      for (aFlow <- flows)
        aFlow.updateFlowWithConsumedTime(consumedTime = timeSlice);


      // sort with SFSH(Simple Flow Scheduling Heuristic)
      val aTuple: Tuple7[KMFlow, Long, Long, Boolean, Double, Double, KMPortType.PortType] = SFSH(flows);

      val opFlow: KMFlow                        = aTuple._1;
      var opUsedBandwidth: Long                 = aTuple._2;
      var opUsedCPU: Long                       = aTuple._3;
      val opCompressionFlag                     = aTuple._4;
      val opFlowFCT_thisRound: Double           = aTuple._5;
      val opCompressionTime: Double             = aTuple._6;
      val opBottleneckPort: KMPortType.PortType = aTuple._7;

      println(s"SFSH[$iterationsNumber]: " +
        s"(opFlow: $opFlow, opUsedBandwidth: $opUsedBandwidth, opUsedCPU: $opUsedCPU, opCompressionFlag: $opCompressionFlag," +
        s" opFlowFCT_thisRound: $opFlowFCT_thisRound, opCompressionTime: $opCompressionTime, opBottleneckPort: $opBottleneckPort)");

      //opFlow.description;
      val flowTraffic: Double = flowTrafficInOneTimeSlice(timeSlice, opUsedBandwidth);

      opFlow.updateFlowWithCompressionArgs(compressionFlag = opCompressionFlag,
                                          compressionTime = opCompressionTime);
      opFlow.updateFlowWith(finishedSize  = flowTraffic,
                           usedBandwidth = opUsedBandwidth,
                           usedCPU       = opUsedCPU);
      opFlow.description;

      ingress.updatePortWithFlow(opFlow);
      egress.updatePortWithFlow(opFlow);
    }
  }
}
