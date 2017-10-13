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

class KMFlow (val flowInfo: KMFlowInfo) extends Serializable {

  val compressionRatio: Double     = 0.5;
  private val flowTotalSizeIfCompressed: Double = KMScalaKit.bigDemicalDoubleMul(this.flowInfo.totalSize, compressionRatio);

  var consumedTime: Double = 0.0;

  var usedBandwidth: Long = 0;
  var usedCPU: Long = 0;

  var remSize: KMFlowSize = new KMFlowSize(compressedSize = 0.0, rawSize = flowInfo.totalSize);
  private var flowState: KMFlowState.FlowState = KMFlowState.waiting_newcome;



  def updateFlowState(state: KMFlowState.FlowState): Unit = {
    this.flowState = state;
  }

  def updateFlowWithCompressionTimeSlice(timeSlice: Double): Unit = {

    // TODO: if this.remSize.compressedSize > this.flowTotalSizeIfCompressed , throw an exception
    try {
      // if have not been totolly compressed, then flow can continue to being compressed
      if (!this.isTotallyCompressed) {
        val traffic: Double = KMScalaKit.bigDemicalDoubleMul(timeSlice, this.flowInfo.channel.ingress.computationSpeed);
        val comp: Double = KMScalaKit.bigDemicalDoubleMul(traffic, this.compressionRatio);

        val sum: Double = KMScalaKit.bigDemicalDoubleAdd(this.remSize.compressedSize, comp);
        val sub: Double = KMScalaKit.bigDemicalDoubleSub(this.remSize.rawSize, traffic);

        this.remSize.updateWith(compressedSize = sum, rawSize = sub);

        // if flow has been totolly compressed, adjust the size
        if (this.remSize.compressedSize >= this.flowTotalSizeIfCompressed) {
          this.remSize.updateWith(compressedSize = this.flowTotalSizeIfCompressed, rawSize = 0.0);
        }
      }
      else {
        throw {
          new RuntimeException("Flow is totoally compressed, it should not be compressed any more !!!");
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

  def updateFlowWithTransmissionTimeSlice(timeSlice: Double): Unit = {
    try {
      if (!this.isCompleted) {
        val traffic: Double = KMScalaKit.bigDemicalDoubleMul(timeSlice, this.usedBandwidth);

        if (this.remSize.mixedSize > traffic) {
          this.remSize.updateWithTransmissionSize(traffic);
        }
        else {
          this.remSize.updateWith(compressedSize = 0.0, rawSize = 0.0);
        }
      }
      else {
        throw {
          new RuntimeException("Flow is completed, it should not be transmitted any more !!!");
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

//  def updateFlowWith(finishedSize: Double,
//                     usedBandwidth: Long,
//                     usedCPU: Long): Unit = {
//    if (this.remSize.rawSize > finishedSize) {
//      this.remSize.rawSize = this.remSize.rawSize - finishedSize;
//    }
//    else {
//      this.remSize.rawSize = 0;
//    }
//
//    this.usedBandwidth = usedBandwidth;
//    this.usedCPU = usedCPU;
//  }

  def updateFlowWith(usedBandwidth: Long,
                     usedCPU: Long): Unit = {
    this.usedBandwidth = usedBandwidth;
    this.usedCPU = usedCPU;
  }

  /**
  * Updating a flow with compression arguments has the "Property of Idempotence" (幂等性)
  */
//  def updateFlowWithCompressionArgs(compressionFlag: Boolean, compressionTime: Double): Unit = {
//    if (compressionFlag) {
//      if (!this.hasBeenCompressed) {
//        this.remSize.rawSize = this.remSize.rawSize * this.compressionRatio;
//        this.consumedTime = KMScalaKit.bigDemicalDoubleAdd(this.consumedTime, compressionTime);
//        this.hasBeenCompressed = true;
//      }
//    }
//  }

  def updateFlowWithConsumedTime(consumedTime: Double): Unit = {
    if (!this.isCompleted) {
      this.consumedTime = KMScalaKit.bigDemicalDoubleAdd(this.consumedTime, consumedTime);
    }
  }

  def updateChannel(): Unit = {
    this.flowInfo.channel.updateChannelWith(this.usedBandwidth, this.usedCPU);
  }

  /**
  * N: total raw size
  * X: size will be compressed
  * K: compression ratio
  *
  * test case: (N-X) + K*X = K*N
  * ==>  X = N
  * ==>  When raw size is 0, flow is totally compressed !!!
  */
  def isTotallyCompressed: Boolean = {
    if (this.remSize.rawSize == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  def isCompleted: Boolean = {
    if (this.remSize.mixedSize == 0) {
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

  def description(): Unit = {
    println("[KMFlow Description]:                                            \n" +
            s"flowId                    : ${this.flowInfo.flowId}             \n" +
            s"flowOnWhichChannel        : ${this.flowInfo.channel.channelId}  \n" +
            s"compressionRatio          : ${this.compressionRatio}            \n" +
            s"consumedTime              : ${this.consumedTime}                \n" +
            s"usedBandwidth             : ${this.usedBandwidth}               \n" +
            s"usedCPU                   : ${this.usedCPU}                     \n" +
            s"remSize.compressedSize    : ${this.remSize.compressedSize}      \n" +
            s"remSize.rawSize           : ${this.remSize.rawSize}             \n" +
            s"remSize.mixedSize         : ${this.remSize.mixedSize}           \n" +
            s"flowState                 : ${this.flowState}                   \n"
    );
  }
}

object KMFlow {
  def initWithFlowInfo(flowInfo: KMFlowInfo): KMFlow = new KMFlow(flowInfo)
}

class KMFlowInfo (val flowId: String,
                  val channel: KMChannel,
                  val totalSize: Double,

                  val arrivedDate: Long,
                  val flowDescription: String
                 ) extends Serializable {

}

class KMFlowSize (var compressedSize: Double, var rawSize: Double) extends Serializable {

  // TODO: override setter mothods

  // Do not invoke these methods outside KMFlow, just use them in this field.
  def updateWithCompressedSize (compressedSize: Double): Unit = {
    this.compressedSize = compressedSize;
  }

  def updateWithRawSize (rawSize: Double): Unit = {
    this.rawSize = rawSize;
  }

  def updateWith (compressedSize: Double, rawSize: Double): Unit = {
    this.compressedSize = compressedSize;
    this.rawSize = rawSize;
  }

  def updateWithTransmissionSize (transmissionSize: Double): Unit = {
    // compressionSize first
    if (this.compressedSize >= transmissionSize) {
      this.compressedSize = KMScalaKit.bigDemicalDoubleSub(this.compressedSize, transmissionSize);
    }
    else {
      this.compressedSize = 0.0;

      val rem = KMScalaKit.bigDemicalDoubleSub(transmissionSize, this.compressedSize);
      if (this.rawSize >= rem) {
        this.rawSize = KMScalaKit.bigDemicalDoubleSub(this.rawSize, rem);
      }
      else {
        this.rawSize = 0.0;
      }
    }
  }

  def mixedSize: Double = {
    return this.compressedSize + this.rawSize;
  }
}
