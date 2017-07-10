/**
  * Created by zhouqihua on 2017/7/8.
  */

object KMPortType extends Enumeration {
  type PortType = Value

  val ingress, egress = Value
}


class KMPort (val portId: String, // physical address
              val portType: KMPortType.PortType,

              val totalBandwidth: Long,
              val totalCPU: Long,

              var remBandwidth: Long,
              var remCPU: Long,
              var computationSpeed: Long) extends Serializable {

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

  def description: Unit = {
    println("[KMPort Description]:");

    println(s"portId           : ${this.portId}");
    println(s"portType         : ${this.portType}");
    println(s"totalBandwidth   : ${this.totalBandwidth}");
    println(s"totalCPU         : ${this.totalCPU}");
    println(s"remBandwidth     : ${this.remBandwidth}");
    println(s"remCPU           : ${this.remCPU}");
    println(s"computationSpeed : ${this.computationSpeed}");
  }

}
