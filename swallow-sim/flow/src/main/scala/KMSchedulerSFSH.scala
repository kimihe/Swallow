/**
  * Created by zhouqihua on 2017/7/23.
  */

import scala.collection.mutable.{ArrayBuffer, Set}
import scala.util.control.Breaks.{break, breakable}

class KMSchedulerSFSH() {

  /**
    * Use "Set" to guarantee idempotence (幂等)
    */

  val ports:            Set[KMPort]         = Set[KMPort]();
  val ingresses:        Set[KMPort]         = Set[KMPort]();
  val egresses:         Set[KMPort]         = Set[KMPort]();
  val channels:         Set[KMChannel]      = Set[KMChannel]();
  val uncompletedFlows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();
  val completedFlows:   ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();

  protected val schedulerType: String = "SFSH";
  protected var printDebugInfo: Boolean = false;
  protected var iterations: Long = 1;



  protected def updateIterations(): Unit = {
    this.iterations += 1;
  }

  protected def addOnePort(aPort: KMPort): Unit = {
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

  protected def removeOnePort(aPort: KMPort): Unit = {
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

  protected def addOneChannel(aChannel: KMChannel): Unit = {
    this.channels += aChannel;
    this.addOnePort(aChannel.ingress);
    this.addOnePort(aChannel.egress);
  }

  def removeOneChannel(aChannel: KMChannel): Unit = {
    this.channels -= aChannel;
    this.removeOnePort(aChannel.ingress);
    this.removeOnePort(aChannel.egress);
  }

  protected def resetOneChannel(aChannel: KMChannel): Unit = {
    aChannel.resetChannel();
  }

  protected def resetAllChannels(): Unit = {
    for (aChannel <- this.channels) {
      this.resetOneChannel(aChannel);
    }
  }

  def addNewFlows(newFlows: Array[KMFlow]): Unit = {
    try {
      for (aFlow <- newFlows) {
        if (!this.uncompletedFlows.contains(aFlow)) {
          this.uncompletedFlows += aFlow;
          this.addOneChannel(aFlow.flowInfo.channel);
        }
        else {
          throw {
            new RuntimeException(s"This flow[${aFlow.flowInfo.flowId}] is already contained in ArrayBuffer: uncompletedFlows.\n It should not be added again !!!");
          }
        }
      }
    }
    catch {
      case e: Exception => println(s"[Catched Exception: ${e.getMessage}]");
    }
  }

  protected def resetAllFlows(): Unit = {
    for (aFlow <- this.uncompletedFlows) {
      aFlow.resetFlow;
    }
  }

  protected def updateFlowArraysWithOneFlow(aFlow: KMFlow): Unit = {
    if (aFlow.isCompleted) {
      try {
        if (this.uncompletedFlows.contains(aFlow)) {
          this.uncompletedFlows -= aFlow;

          if (!this.completedFlows.contains(aFlow)) {
            this.completedFlows += aFlow;
          }
          else {
            throw {
              new RuntimeException(s"This flow[${aFlow.flowInfo.flowId}] is already contained in ArrayBuffer: completedFlows.\n It should not be added again !!!");
            }
          }
        }
        else {
          throw {
            new RuntimeException(s"This flow[${aFlow.flowInfo.flowId}] is not contained in ArrayBuffer: uncompletedFlows.\n The remove operation should not be executed !!!");
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
  }

  protected def updateUncompletedFlowsWithConsumedTime(consumedTime: Double): Unit = {
    for (aFlow <- this.uncompletedFlows) {
      aFlow.updateFlowWithConsumedTime(consumedTime);
    }
  }

  def allFlowsIsCompleted: Boolean = {

//    var flag: Boolean = true;
//    // pass a function to the breakable method
//    breakable {
//      for (aFlow <- this.uncompletedFlows) {
//        if (!aFlow.isCompleted) {
//          flag = false;
//          break();
//        }
//      }
//    }

    var flag: Boolean = false;
    if (this.uncompletedFlows.isEmpty) {
      flag = true;
    }

    return flag;
  }

  protected def averageFlowCompletionTime(): Double = {
    val len: Int = this.completedFlows.length;
    var sum: Double = 0.0;

    for (aFlow <- this.completedFlows) {
      sum += aFlow.consumedTime;
    }

    val avg: Double = sum / len;

    return avg;
  }

  def printCompletedFlowsInOrder(): Unit = {
    for (aFlow <- this.completedFlows) {
      println(s"FLOW: ${aFlow.flowInfo.flowId}, FCT: ${aFlow.consumedTime}");
    }

    val avgFCT: Double = this.averageFlowCompletionTime();
    println(s"${this.schedulerType} AVG-FCT: $avgFCT");
  }

  def printCompletedFlowsInOrderPrettyily(): Unit = {
    for (aChannel <- this.channels) {
      var order: Long = 1;
      for (aFlow <- this.completedFlows) {
        if (aFlow.flowInfo.channel.equals(aChannel)) {
          println(s"${this.schedulerType}[${order}], CHANNEL: ${aChannel.channelId}, FLOW: ${aFlow.flowInfo.flowId}, FCT: ${aFlow.consumedTime}");
          order += 1;
        }
      }
      println("\n");
    }

    val avgFCT: Double = this.averageFlowCompletionTime();
    println(s"${this.schedulerType} AVG-FCT: $avgFCT");
  }

  def printDebugInfoOff(): Unit = {
    this.printDebugInfo = false;
  }

  def printDebugInfoOn(): Unit = {
    this.printDebugInfo = true;
  }

  def clear(): Unit = {
    this.ports.clear();
    this.ingresses.clear();
    this.egresses.clear();
    this.channels.clear();
    this.uncompletedFlows.clear();
    this.completedFlows.clear();
  }

  def description(): Unit = {
    println("[KMScheduler Description]:               \n" +
      s"port              : ${this.ports}             \n" +
      s"channels          : ${this.channels}          \n" +
      s"ingresses         : ${this.ingresses}         \n" +
      s"egresses          : ${this.egresses}          \n" +
      s"uncompletedFlows  : ${this.uncompletedFlows}  \n" +
      s"completedFlows    : ${this.completedFlows}"
    );
  }



  /**
    * calculate completion time and sort
    */
  protected def coreSchedulingAlgorithm(inOneChannel: KMChannel): KMSchedulingResult = {

    // optimal(op) flow, bandwidth and CPU
    var opFlow: KMFlow                        = null;
    var opUsedBandwidth: Long                 = 0;
    var opUsedCPU: Long                       = 0;
    var opCompressionFlag: Boolean            = false;
    var opFlowFCT_thisRound: Double           = Double.MaxValue;
    var opCompressionTime: Double             = 0.0;
    var opBottleneckPort: KMPortType.PortType = KMPortType.other;



    // iteration
    for (aFlow <- this.uncompletedFlows) {
      breakable {
        if (!aFlow.flowInfo.channel.equals(inOneChannel))
          break();

        if (aFlow.isCompleted) {
          println("Should never go here !!!");
          throw {
            new RuntimeException("Should never go here !!!");
          }
          break();   // continue
        }



        // init variables
        var bnBandwidth: Long           = 0;
        var usedCPU                     = 0;
        var compressionFlag: Boolean    = false;
        var FCT: Double                 = 0;
        var compressionTime: Double     = 0.0;
        var bnPort: KMPortType.PortType = KMPortType.other;



        // bandwidth bottleneck(bn)
        val ingressBandwidth: Long = aFlow.flowInfo.channel.ingress.remBandwidth;
        val egressBandwidth: Long  = aFlow.flowInfo.channel.egress.remBandwidth;

        if (ingressBandwidth <= egressBandwidth) {
          bnBandwidth = ingressBandwidth;
          bnPort = KMPortType.ingress;
        }
        else {
          bnBandwidth = egressBandwidth;
          bnPort = KMPortType.egress;
        }

        if (bnBandwidth == 0)
          break(); // equivalent to 'continue', when no bandwidth resources, skip this iteration



        // TODO: If a flow is compressed totally, do not compress it again
        if (aFlow.isTotallyCompressed) {
          FCT = KMScalaKit.bigDemicalDoubleDiv(aFlow.remSize.mixedSize, bnBandwidth);
          compressionFlag = false;
          compressionTime = 0.0;
        }
        else {
          // completion time under uncompressed
          val T_uc_i: Double = KMScalaKit.bigDemicalDoubleDiv(aFlow.remSize.mixedSize, bnBandwidth);
          val T_uc_j: Double = T_uc_i;
          val T_uc_max: Double = T_uc_i;

          // completion time under compressed
          val T_c_i: Double  = (aFlow.remSize.rawSize * aFlow.compressionRatio + aFlow.remSize.compressedSize) / bnBandwidth +
            aFlow.remSize.rawSize / aFlow.flowInfo.channel.ingress.computationSpeed;
          val T_c_j: Double  = (aFlow.remSize.rawSize * aFlow.compressionRatio + aFlow.remSize.compressedSize) / bnBandwidth +
            aFlow.remSize.rawSize / aFlow.flowInfo.channel.egress.computationSpeed;
          val T_c_max: Double  = math.max(T_c_i, T_c_j);

          // comparison of compressed and uncompressed
          if (T_c_max <= T_uc_max) {
            FCT = T_c_max;
            compressionFlag = true;
            compressionTime = KMScalaKit.bigDemicalDoubleDiv(aFlow.remSize.rawSize, aFlow.flowInfo.channel.ingress.computationSpeed);
          }
          else {
            FCT = T_uc_max;
            compressionFlag = false;
            compressionTime = 0.0;
          }
        }



        /**
          * SFSH
          */

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


    /**
     * (opFlow == null) means that: there are no uncompleted flows belong to this channel. Therefore, SFSH will return KMSchedulingResult with null
     */

    if (opFlow == null) {
      return null;
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

  protected def schedulingFlows(timeSlice: Double, channel: KMChannel): Unit = {

    // sort with SFSH(Simple Flow Scheduling Heuristic)
    val schedulingRes: KMSchedulingResult = this.coreSchedulingAlgorithm(inOneChannel = channel);

    if (schedulingRes == null) {
      return ; // there are no uncompleted flows belong to this channel, skip scheduling on this channel
    }

    val opFlow: KMFlow                        = schedulingRes.opFlow;
    val opUsedBandwidth: Long                 = schedulingRes.opUsedBandwidth;
    val opUsedCPU: Long                       = schedulingRes.opUsedCPU;
    val opCompressionFlag                     = schedulingRes.opCompressionFlag;
    val opFlowFCT_thisRound: Double           = schedulingRes.opFlowFCT_thisRound;
    val opCompressionTime: Double             = schedulingRes.opCompressionTime;
    val opBottleneckPort: KMPortType.PortType = schedulingRes.opBottleneckPort;

    if (printDebugInfo) {
      println(s"Time Slice[${this.iterations}] on Channel[${channel.channelId}] - SFSH:\n" +
        s"(opFlow: ${opFlow.flowInfo.flowId}, opUsedBandwidth: $opUsedBandwidth, opUsedCPU: $opUsedCPU, opCompressionFlag: $opCompressionFlag," +
        s" opFlowFCT_thisRound: $opFlowFCT_thisRound, opCompressionTime: $opCompressionTime, opBottleneckPort: $opBottleneckPort)");
      // opFlow.description();
    }

//    // TODO: How to calculate the consumed time?
//    this.updateUncompletedFlowsWithConsumedTime(consumedTime = timeSlice);


    /**
      * When the time slice is used for compression, the bandwidth and CPU resources still need to be cut for transmission conservation in the future
      */
    opFlow.updateFlowWith(opUsedBandwidth, opUsedCPU);
    opFlow.updateChannel();

    // compression or transmission
    if (opCompressionFlag) {
      // TODO: Take Compression Time Into Account !!!
      opFlow.updateFlowWithCompressionTimeSlice(timeSlice);
    }
    else {
      opFlow.updateFlowWithTransmissionTimeSlice(timeSlice);
    }


    if (printDebugInfo) {
      opFlow.description();
    }

    this.updateFlowArraysWithOneFlow(opFlow);
//    this.updateIterations();
  }

  def scheduling(timeSlice: Double): Unit = {

    // each scheduling time point release all resources
    this.resetAllChannels();
    this.resetAllFlows();

    // TODO: How to calculate the consumed time?
    this.updateUncompletedFlowsWithConsumedTime(consumedTime = timeSlice);

    // TODO: How to expand to multi-channel?  Sort channels by min bnPort.
    // greedy algorithm
    for (aChannel <- this.channels) {
      // Work Conserving Allocation: utilize the unallocated bandwidth through this channel as much as possible
      if (aChannel.isBandwidthFree()) {
        this.schedulingFlows(timeSlice, aChannel);
      }
    }

    this.updateIterations();
  }



}
