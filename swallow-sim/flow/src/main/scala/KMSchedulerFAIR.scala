/**
  * Created by zhouqihua on 2017/9/26.
  */

import scala.collection.mutable.ArrayBuffer

class KMSchedulerFAIR(val parallelism: Int = 5) extends KMSchedulerSFSH {
  override protected val schedulerType: String = "FAIR"

  override def scheduling(timeSlice: Double): Unit = {

  }

    def schedulingInOneChannel(channel: KMChannel): Unit = {

      val bandwidth = channel.bottleneckPort().remBandwidth
      val parallelFlows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]()

      while(this.uncompletedFlows.length > 0) {

        var step: Int = 0
        if (this.uncompletedFlows.length >= this.parallelism)
          step = this.parallelism
        else
          step = this.uncompletedFlows.length

        var sizeSum: Double = 0.0
        for (i <- 0 until step) { // same as "i <- 0 to step-1"
          val aFlow: KMFlow =  this.uncompletedFlows(i)
          parallelFlows += aFlow
          sizeSum = sizeSum + aFlow.remSize.mixedSize
        }
        val consumedTime = KMScalaKit.bigDemicalDoubleDiv(sizeSum, bandwidth)
        this.updateUncompletedFlowsWithConsumedTime(consumedTime)


        for (aFlow <- parallelFlows) {
          aFlow.remSize.updateWith(compressedSize = 0.0, rawSize = 0.0)
          this.updateFlowArraysWithOneFlow(aFlow)
        }

        parallelFlows.clear()
      }
    }
}
