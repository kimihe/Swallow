/**
  * Created by zhouqihua on 2017/7/8.
  */

object KMFlow {
  def initWithFlowInfo(flowInfo: KMFlowInfo): KMFlow = new KMFlow(flowInfo)
}

class KMFlow (val flowInfo: KMFlowInfo) extends Serializable {

  val compressionRatio: Double     = 0.5;
  val flowTotalSizeIfCompressed: Double = KMScalaKit.bigDemicalDoubleMul(this.flowInfo.totalSize, compressionRatio);

  var hasBeenCompressed: Boolean   = false;

  var consumedTime: Double = 0.0;
//  var remSize: Double = flowInfo.totalSize;
//  var remCompressionTime: Double = 0.0;

  var usedBandwidth: Long = 0;
  var usedCPU: Long = 0;

  var remSize: KMFlowSize = new KMFlowSize(compressedSize = 0.0, rawSize = flowInfo.totalSize);

  def updateFlowWithCompressSize(compressedSize: Double): Unit = {
    if (this.remSize.compressedSize < this.flowTotalSizeIfCompressed) {
      val sum: Double = KMScalaKit.bigDemicalDoubleAdd(this.remSize.compressedSize, compressedSize);
      this.remSize.updateWithCompressedSize(sum);
    }

    // TODO: if this.remSize.compressedSize > this.flowTotalSizeIfCompressed , throw an exception
  }

  def updateFlowWithTransmittedSize(transmittedSize: Double) = {
    if (this.remSize.mixedSize > transmittedSize) {
      this.remSize.updateWithTransmittedSize(transmittedSize);
    }
    else {
      this.remSize.updateWith(compressedSize = 0.0, rawSize = 0.0);
    }
  }

  def updateFlowWith(finishedSize: Double,
                     usedBandwidth: Long,
                     usedCPU: Long): Unit = {
    if (this.remSize.rawSize > finishedSize) {
      this.remSize.rawSize = this.remSize.rawSize - finishedSize;
    }
    else {
      this.remSize.rawSize = 0;
    }

    this.usedBandwidth = usedBandwidth;
    this.usedCPU = usedCPU;
  }

  def updateFlowWith(usedBandwidth: Long,
                     usedCPU: Long): Unit = {
    this.usedBandwidth = usedBandwidth;
    this.usedCPU = usedCPU;
  }

  /**
  * Updating a flow with compression arguments has the "Property of Idempotence" (幂等性)
  */
  def updateFlowWithCompressionArgs(compressionFlag: Boolean, compressionTime: Double): Unit = {
    if (compressionFlag) {
      if (!this.hasBeenCompressed) {
        this.remSize.rawSize = this.remSize.rawSize * this.compressionRatio;
        this.consumedTime = KMScalaKit.bigDemicalDoubleAdd(this.consumedTime, compressionTime);
        this.hasBeenCompressed = true;
      }
    }
  }

  def updateFlowWithConsumedTime(consumedTime: Double): Unit = {
    if (!this.isCompleted) {
      this.consumedTime = KMScalaKit.bigDemicalDoubleAdd(this.consumedTime, consumedTime);
    }
  }

  def isCompleted: Boolean = {
    if (this.remSize.rawSize == 0) {
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

  def description: Unit = {
    println("[KMFlow Description]:                              \n" +
            s"flowId              : ${this.flowInfo.flowId}     \n" +
            s"compressionRatio    : ${this.compressionRatio}    \n" +
            s"hasBeenCompressed   : ${this.hasBeenCompressed}   \n" +
            s"consumedTime        : ${this.consumedTime}        \n" +
            s"remSize             : ${this.remSize}             \n" +
            //s"remCompressionTime  : ${this.remCompressionTime}  \n" +
            s"usedBandwidth       : ${this.usedBandwidth}       \n" +
            s"usedCPU             : ${this.usedCPU}");
  }
}

class KMFlowSize (var compressedSize: Double, var rawSize: Double) {
  var mixedSize = this.compressedSize + rawSize;

  // TODO: override setter mothods
  def updateWithCompressedSize (compressedSize: Double): Unit = {
    this.compressedSize = compressedSize;
    this.mixedSize = this.compressedSize + this.rawSize;
  }

  def updateWithRawSize (rawSize: Double): Unit = {
    this.rawSize = rawSize;
    this.mixedSize = this.compressedSize + this.rawSize;
  }

  def updateWith (compressedSize: Double, rawSize: Double): Unit = {
    this.compressedSize = compressedSize;
    this.rawSize = rawSize;
    this.mixedSize = this.compressedSize + this.rawSize;
  }

  def updateWithTransmittedSize (transmittedSize: Double): Unit = {
    // compressionSize first
    if (this.compressedSize >= transmittedSize) {
      this.compressedSize = KMScalaKit.bigDemicalDoubleSub(this.compressedSize, transmittedSize);
    }
    else {
      this.compressedSize = 0.0;

      val rem = KMScalaKit.bigDemicalDoubleSub(transmittedSize, this.compressedSize);
      if (this.rawSize >= rem) {
        this.rawSize = KMScalaKit.bigDemicalDoubleSub(this.rawSize, rem);
      }
      else {
        this.rawSize = 0.0;
      }
    }

    this.mixedSize = this.compressedSize + this.rawSize;
  }


}
