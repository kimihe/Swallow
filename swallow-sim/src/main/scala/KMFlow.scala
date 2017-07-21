/**
  * Created by zhouqihua on 2017/7/8.
  */

object KMFlowState extends Enumeration {
  type FlowState = Value

  val waiting_newcome,
      executing_compression, completed_compression,
      executing_transmission, completed_transmission,
      waiting_suspended,
      completed_exit,
      other = Value;
}

object KMFlow {
  def initWithFlowInfo(flowInfo: KMFlowInfo): KMFlow = new KMFlow(flowInfo)
}

class KMFlow (val flowInfo: KMFlowInfo) extends Serializable {

  val compressionRatio: Double     = 0.5;
  val flowTotalSizeIfCompressed: Double = KMScalaKit.bigDemicalDoubleMul(this.flowInfo.totalSize, compressionRatio);

  var hasBeenCompressed: Boolean   = false;
  var compressionIsCompleted: Boolean = false;

  var consumedTime: Double = 0.0;

  var usedBandwidth: Long = 0;
  var usedCPU: Long = 0;

  var remSize: KMFlowSize = new KMFlowSize(compressedSize = 0.0, rawSize = flowInfo.totalSize);
  var flowState: KMFlowState.FlowState = KMFlowState.waiting_newcome;



  def updateFlowState(state: KMFlowState.FlowState): Unit = {
    this.flowState = state;
  }

  def updateFlowWithCompressSize(compressedSize: Double): Unit = {

    if (this.remSize.compressedSize < this.flowTotalSizeIfCompressed) {
      val comp: Double = KMScalaKit.bigDemicalDoubleMul(compressedSize, this.compressionRatio);
      val sum: Double = KMScalaKit.bigDemicalDoubleAdd(this.remSize.compressedSize, comp);
      val sub: Double = KMScalaKit.bigDemicalDoubleSub(this.remSize.rawSize, comp);
      this.remSize.updateWith(compressedSize = sum, rawSize = sub);

      if (this.remSize.compressedSize >= this.flowTotalSizeIfCompressed) {
        this.remSize.updateWith(compressedSize = this.flowTotalSizeIfCompressed, rawSize = 0.0);
        this.compressionIsCompleted = true;
      }
    }
    else {
      // TODO: if this.remSize.compressedSize > this.flowTotalSizeIfCompressed , throw an exception
    }
  }

  def updateFlowWithTransmittedSize(transmittedSize: Double): Unit = {
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
    println("[KMFlow Description]:                                      \n" +
            s"flowId                  : ${this.flowInfo.flowId}         \n" +
            s"compressionRatio        : ${this.compressionRatio}        \n" +
            s"hasBeenCompressed       : ${this.hasBeenCompressed}       \n" +
            s"consumedTime            : ${this.consumedTime}            \n" +
            s"usedBandwidth           : ${this.usedBandwidth}           \n" +
            s"usedCPU                 : ${this.usedCPU}                 \n" +
            s"remSize.compressedSize  : ${this.remSize.compressedSize}  \n" +
            s"remSize.rawSize         : ${this.remSize.rawSize}         \n" +
            s"remSize.mixedSize       : ${this.remSize.mixedSize}"
    );
  }
}

class KMFlowInfo (val flowId: String,

                  val ingress: KMPort,
                  val egress: KMPort,
                  val totalSize: Double,

                  val arrivedDate: Long,
                  val description: String
                 ) extends Serializable {

}

class KMFlowSize (var compressedSize: Double, var rawSize: Double) extends Serializable {
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
