/**
  * Created by zhouqihua on 2017/7/23.
  */

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks.{break, breakable}

class KMScheduler {

  val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();

  def addNewFlows(newFlows:Array[KMFlow]): Unit = {
    this.flows ++= newFlows;
  }

  def removeCompletedFlows(completedFlows: Array[KMFlow]): Unit = {
    this.flows --= completedFlows;
  }

  def flowsDidCompleted: Boolean = {

    var flag: Boolean = true;
    // pass a function to the breakable method
    breakable {
      for (aFlow <- this.flows) {
        if (!aFlow.isCompleted) {
          flag = false;
          break();
        }
      }
    }

    return flag;
  }

  /**
    * calculate completion time and sort
    */
  def SFSH: Tuple7[KMFlow, Long, Long, Boolean, Double, Double, KMPortType.PortType] = {

    // optimal(op) flow, bandwidth and CPU
    var opFlow: KMFlow                        = null;
    var opUsedBandwidth: Long                 = 0;
    var opUsedCPU: Long                       = 0;
    var opCompressionFlag: Boolean            = false;
    var opFlowFCT_thisRound: Double           = Double.MaxValue;
    var opCompressionTime: Double             = 0.0;
    var opBottleneckPort: KMPortType.PortType = KMPortType.other;



    // iteration
    for (aFlow <- this.flows) {
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



        // TODO: If a flow is compressed totally, do not compress it again
        if (aFlow.isTotallyCompressed) {
          FCT = aFlow.remSize.mixedSize / bnBandwidth;
          compressionFlag = false;
          compressionTime = 0.0;
        }
        else {
          // completion time under uncompressed
          val T_uc_i: Double = aFlow.remSize.mixedSize / bnBandwidth;
          val T_uc_j: Double = aFlow.remSize.mixedSize / bnBandwidth;

          // completion time under compressed
          val T_c_i: Double  = (aFlow.remSize.rawSize * aFlow.compressionRatio + aFlow.remSize.compressedSize) / bnBandwidth +
            aFlow.remSize.rawSize / aFlow.flowInfo.ingress.computationSpeed;
          val T_c_j: Double  = (aFlow.remSize.rawSize * aFlow.compressionRatio + aFlow.remSize.compressedSize) / bnBandwidth +
            aFlow.remSize.rawSize / aFlow.flowInfo.egress.computationSpeed;

          // comparison of compressed and uncompressed
          val T_c_max: Double  = math.max(T_c_i, T_c_j);
          val T_uc_max: Double = math.max(T_uc_i, T_uc_j);

          if (T_c_max <= T_uc_max) {
            FCT = T_c_max;
            compressionFlag = true;
            compressionTime = aFlow.remSize.rawSize / aFlow.flowInfo.ingress.computationSpeed;
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

  def schedulingFlows(timeSlice: Double, ingress: KMPort, egress: KMPort, iterationsNumber: Long): Unit = {

    // each scheduling time point reset all resources
    ingress.resetPort;
    egress.resetPort;
    for (aFlow <- this.flows) {
      aFlow.resetFlow;
    }

    // TODO: How to expand to multi-channel ?
    // greedy algorithm
    while (ingress.isBandwidthFree && egress.isBandwidthFree) {

      // sort with SFSH(Simple Flow Scheduling Heuristic)
      val aTuple: Tuple7[KMFlow, Long, Long, Boolean, Double, Double, KMPortType.PortType] = SFSH;

      val opFlow: KMFlow                        = aTuple._1;
      var opUsedBandwidth: Long                 = aTuple._2;
      var opUsedCPU: Long                       = aTuple._3;
      val opCompressionFlag                     = aTuple._4;
      val opFlowFCT_thisRound: Double           = aTuple._5;
      val opCompressionTime: Double             = aTuple._6;
      val opBottleneckPort: KMPortType.PortType = aTuple._7;

      println(s"SFSH[$iterationsNumber]: " +
        s"(opFlow: ${opFlow.flowInfo.flowId}, opUsedBandwidth: $opUsedBandwidth, opUsedCPU: $opUsedCPU, opCompressionFlag: $opCompressionFlag," +
        s" opFlowFCT_thisRound: $opFlowFCT_thisRound, opCompressionTime: $opCompressionTime, opBottleneckPort: $opBottleneckPort)");
      // opFlow.description;

      // TODO: How to calculate the consumed time?
      for (aFlow <- this.flows) {
        aFlow.updateFlowWithConsumedTime(consumedTime = timeSlice);
      }


      // compression or transmission
      if (opCompressionFlag) {
        // TODO: Take Compression Time Into Account !!!
        opFlow.updateFlowWithCompressionTimeSlice(timeSlice);
      }
      else {
        opFlow.updateFlowWith(opUsedBandwidth, opUsedCPU);
        opFlow.updateFlowWithTransmissionTimeSlice(timeSlice);

        opFlow.updatePort;
      }

      opFlow.description;
    }
  }

  
  
  
  
  
}
