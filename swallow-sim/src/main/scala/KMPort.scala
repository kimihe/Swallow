/**
  * Created by zhouqihua on 2017/7/8.
  */

object KMPortType extends Enumeration {
  type PortType = Value

  val ingress, egress, other = Value
}


class KMPort (val portId: String, // physical address
              val portType: KMPortType.PortType,

              val totalBandwidth: Long,
              val totalCPU: Long,
              val computationSpeed: Long) extends Serializable {

  var remBandwidth: Long = totalBandwidth;
  var remCPU: Long       = totalCPU;

  def updatePortWithFlow(flow: KMFlow): Unit = {

    if (this.remBandwidth >= flow.usedBandwidth) {
      this.remBandwidth = this.remBandwidth - flow.usedBandwidth;
    }
    else {
      this.remBandwidth = 0;
    }

    if (this.remCPU > flow.usedCPU) {
      this.remCPU = this.remCPU - flow.usedCPU;
    }
    else {
      this.remCPU = 0;
    }

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

  def resetPort: Unit = {
    this.remBandwidth = totalBandwidth;
    this.remCPU       = totalCPU;
  }

  def description: Unit = {
    println("[KMPort Description]:                        \n" +
            s"portId           : ${this.portId}           \n" +
            s"portType         : ${this.portType}         \n" +
            s"totalBandwidth   : ${this.totalBandwidth}   \n" +
            s"totalCPU         : ${this.totalCPU}         \n" +
            s"remBandwidth     : ${this.remBandwidth}     \n" +
            s"remCPU           : ${this.remCPU}           \n" +
            s"computationSpeed : ${this.computationSpeed}");
  }

}
