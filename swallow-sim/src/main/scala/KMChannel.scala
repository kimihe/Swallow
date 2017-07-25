/**
  * Created by zhouqihua on 2017/7/25.
  */
class KMChannel(val ingress: KMPort,
                val egress: KMPort) extends Serializable {


  def updateChannelWith(usedBandwidth: Long, usedCPU: Long): Unit = {
    this.ingress.updatePortWith(usedBandwidth, usedCPU);
    this.egress.updatePortWith(usedBandwidth, usedCPU);
  }

  def resetChannel: Unit = {
    this.ingress.resetPort;
    this.egress.resetPort;
  }

  def description(): Unit = {
    println("[KMChannel Description]:      \n" +
      s"ingress        : ${this.ingress}   \n" +
      s"egress         : ${this.egress}"
    );
  }

}
