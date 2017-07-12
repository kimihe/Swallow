/**
  * Created by zhouqihua on 2017/7/8.
  */

object KMFlow {
  def initWithFlowInfo(flowInfo: KMFlowInfo): KMFlow = new KMFlow(flowInfo)
}

class KMFlow (val flowInfo: KMFlowInfo) extends Serializable{

  val compressionRatio: Double = 0.5;
  var willBeCompressed: Boolean = false;

  var consumedTime: Double = 0;
  var remSize: Double = flowInfo.totalSize;

  var usedBandwidth: Long = 0;
  var usedCPU: Long = 0;

  def updateFlowWith(finishedSize: Double,
                     usedBandwidth: Long,
                     usedCPU: Long): Unit = {
    if (this.remSize > finishedSize) {
      this.remSize = this.remSize - finishedSize;
    }
    else {
      this.remSize = 0;
    }

    this.usedBandwidth = usedBandwidth;
    this.usedCPU = usedCPU;
  }

  def updateFlowWith(usedBandwidth: Long,
                     usedCPU: Long): Unit = {
    this.usedBandwidth = usedBandwidth;
    this.usedCPU = usedCPU;
  }

  def updateFlowWith(consumedTime: Double): Unit = {
    if (!this.isCompleted) {
      this.consumedTime += consumedTime;
    }
  }

  def isCompleted: Boolean = {
    if (this.remSize == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  def resetFlow: Unit = {
    this.usedBandwidth = 0;
    this.usedCPU = 0;
  }

}
