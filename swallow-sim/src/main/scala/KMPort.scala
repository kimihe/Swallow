/**
  * Created by zhouqihua on 2017/7/8.
  */


class KMPort (val portId: String, // physical address

              val totalBandwidth: Long,
              val totalCPU: Long,

              var remBandwidth: Long,
              var remCPU: Long,
              var compressionSpeed: Long) extends Serializable {

  def updatePortWithFlow(flow: KMFlow): Unit = {

    this.remBandwidth = this.remBandwidth - flow.usedBandwidth;
    this.remCPU = this.remCPU - flow.usedCPU;
  }

  def isBandwidthFree: Boolean = {
    if (remBandwidth > 0) {
      return true;
    }
    else {
      return  false;
    }
  }

  def isCPUFree: Boolean = {
    if (remCPU > 0) {
      return  true;
    }
    else {
      return  false;
    }
  }

}
