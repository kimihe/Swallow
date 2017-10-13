/**
  * Created by zhouqihua on 2017/7/25.
  */
class KMChannel(val channelId: String,
                val ingress: KMPort,
                val egress: KMPort,
                val channelDesciption: String) extends Serializable {

  def updateChannelWith(usedBandwidth: Long, usedCPU: Long): Unit = {
    this.ingress.updatePortWith(usedBandwidth, usedCPU);
    this.egress.updatePortWith(usedBandwidth, usedCPU);
  }

  def resetChannel(): Unit = {
    this.ingress.resetPort;
    this.egress.resetPort;
  }

  def bottleneckPort(): KMPort = {
    var bnPort: KMPort = this.ingress;
    if (this.egress.remBandwidth < this.ingress.remBandwidth)
      bnPort = this.egress;

    return bnPort;
  }

  def isBandwidthFree(): Boolean = {
    if (this.bottleneckPort().isBandwidthFree)
      return true;
    else
      return false;
  }

  def description(): Unit = {
    println("[KMChannel Description]:      \n" +
      s"channelId:     : ${this.channelId} \n" +
      s"ingress        : ${this.ingress}   \n" +
      s"egress         : ${this.egress}    \n"
    );
  }

}
