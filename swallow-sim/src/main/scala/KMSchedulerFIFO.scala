/**
  * Created by zhouqihua on 2017/7/27.
  */

import scala.util.control.Breaks.{break, breakable}

class KMSchedulerFIFO extends KMSchedulerSFSH {

  override protected val schedulerType: String = "FIFO";

  override def coreSchedulingAlgorithm(inOneChannel: KMChannel): KMSchedulingResult = {

    // optimal(op) flow, bandwidth and CPU
    var opFlow: KMFlow                        = null;
    var opUsedBandwidth: Long                 = 0;
    var opUsedCPU: Long                       = 0;
    var opCompressionFlag: Boolean            = false;
    var opFlowFCT_thisRound: Double           = Double.MaxValue;
    var opCompressionTime: Double             = 0.0;
    var opBottleneckPort: KMPortType.PortType = KMPortType.other;



    // iteration
    var stepOut: Boolean = false;
    for (aFlow <- this.uncompletedFlows; if(!stepOut)) {
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




        FCT = KMScalaKit.bigDemicalDoubleDiv(aFlow.remSize.mixedSize, bnBandwidth);

        /**
          * FIFO
          */

        opFlow              = aFlow;
        opUsedBandwidth     = bnBandwidth;
        opUsedCPU           = usedCPU;
        opCompressionFlag   = compressionFlag;
        opFlowFCT_thisRound = FCT;
        opCompressionTime   = compressionTime;
        opBottleneckPort    = bnPort;

        stepOut = true;
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



}
