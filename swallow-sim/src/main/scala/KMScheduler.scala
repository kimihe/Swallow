/**
  * Created by zhouqihua on 2017/7/23.
  */

import scala.collection.mutable.{ArrayBuffer, Set}
import scala.util.control.Breaks.{break, breakable}

class KMScheduler {

  private var iterations: Long = 0;

  val ports: Set[KMPort]         = Set[KMPort]();
  val ingresses: Set[KMPort]     = Set[KMPort]();
  val egresses:  Set[KMPort]     = Set[KMPort]();
  val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();

  private def updateIterations(): Unit = {
    this.iterations += 1;
  }

  def addOnePort(aPort: KMPort): Unit = {
    try {
      if(aPort.portType == KMPortType.ingress) {
        this.ports += aPort;
        this.ingresses += aPort;
      }
      else if(aPort.portType == KMPortType.egress) {
        this.ports += aPort;
        this.egresses += aPort;
      }
      else {
        throw {
          new RuntimeException("Unknown port type !!!");
        }
      }
    }
    catch {
      case e: Exception => println(s"[Catched Exception: ${e.getMessage}]");
    }
    finally {
      // sth
    }
  }

  def removeOnePort(aPort: KMPort): Unit = {
    try {
      if(aPort.portType == KMPortType.ingress) {
        this.ports -= aPort;
        this.ingresses -= aPort;
      }
      else if(aPort.portType == KMPortType.egress) {
        this.ports -= aPort;
        this.egresses -= aPort;
      }
      else {
        throw {
          new RuntimeException("Unknown port type !!!");
        }
      }
    }
    catch {
      case e: Exception => println(s"[Catched Exception: ${e.getMessage}]");
    }
    finally {
      // sth
    }
  }

  def addPorts(ports: Set[KMPort]): Unit = {
    for (aPort <- ports) {
      this.addOnePort(aPort);
    }
  }

  def removePorts(ports: Set[KMPort]): Unit = {
    for (aPort <- ports) {
      this.removeOnePort(aPort);
    }
  }

  def addNewFlows(newFlows: Array[KMFlow]): Unit = {
    this.flows ++= newFlows;

    for (aFlow <- newFlows) {
      val ingress: KMPort = aFlow.flowInfo.ingress;
      val egress:  KMPort = aFlow.flowInfo.egress;

      this.addOnePort(ingress);
      this.addOnePort(egress);
    }
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

  def description(): Unit = {


    println("[KMScheduler Description]: \n" +
      s"port       : ${this.ports}      \n" +
      s"ingresses  : ${this.ingresses}  \n" +
      s"egresses   : ${this.egresses}   \n" +
      s"flows      : ${this.flows}"
    );
  }




  /**
    * calculate completion time and sort
    */
  private def SFSH(): KMSchedulingResult = {

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



    val res: KMSchedulingResult = new KMSchedulingResult(
      opFlow,
      opUsedBandwidth,
      opUsedCPU,
      opCompressionFlag,
      opFlowFCT_thisRound,
      opCompressionTime,
      opBottleneckPort);

    return res;
  }

  private def schedulingFlows(timeSlice: Double, ingress: KMPort, egress: KMPort): Unit = {

    // each scheduling time point reset all resources
    ingress.resetPort;
    egress.resetPort;
    for (aFlow <- this.flows) {
      aFlow.resetFlow;
    }


    // sort with SFSH(Simple Flow Scheduling Heuristic)
    val schedulingRes: KMSchedulingResult = this.SFSH();

    val opFlow: KMFlow                        = schedulingRes.opFlow;
    var opUsedBandwidth: Long                 = schedulingRes.opUsedBandwidth;
    var opUsedCPU: Long                       = schedulingRes.opUsedCPU;
    val opCompressionFlag                     = schedulingRes.opCompressionFlag;
    val opFlowFCT_thisRound: Double           = schedulingRes.opFlowFCT_thisRound;
    val opCompressionTime: Double             = schedulingRes.opCompressionTime;
    val opBottleneckPort: KMPortType.PortType = schedulingRes.opBottleneckPort;

    println(s"SFSH[${this.iterations}]: " +
      s"(opFlow: ${opFlow.flowInfo.flowId}, opUsedBandwidth: $opUsedBandwidth, opUsedCPU: $opUsedCPU, opCompressionFlag: $opCompressionFlag," +
      s" opFlowFCT_thisRound: $opFlowFCT_thisRound, opCompressionTime: $opCompressionTime, opBottleneckPort: $opBottleneckPort)");
    // opFlow.description();

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

    opFlow.description();

    this.updateIterations();
  }

  def scheduling(timeSlice: Double): Unit = {
    // TODO: How to expand to multi-channel ?
    // greedy algorithm
//        while (ingress.isBandwidthFree && egress.isBandwidthFree) {
//
//        }

    for (ingress <- this.ingresses) {
      for (egress <- this.egresses) {
        this.schedulingFlows(timeSlice, ingress, egress);
      }
    }
  }



}
