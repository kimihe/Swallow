/**
  * Created by zhouqihua on 2017/7/12.
  */

import scala.collection.mutable.{ArrayBuffer, Set}
import scala.util.control.Breaks._

object test {
  def main(args: Array[String]): Unit = {

    for (i <- 1 to 5) {

      println(s"before breakable, i: $i");

      breakable {
        if (i == 3)
          break();
      }
      println(s"after breakable, i: $i");
    }



    val ingress: KMPort = new KMPort(
      portId = "ingress",
      portType =  KMPortType.ingress,
      totalBandwidth =  200,
      totalCPU =  1,
      computationSpeed =  400);
    val egress: KMPort  = new KMPort(
      portId =  "egress",
      portType = KMPortType.egress,
      totalBandwidth = 100,
      totalCPU = 1,
      computationSpeed = 800);

    val channel: KMChannel = new KMChannel("channel1", ingress, egress, "ingress-egress");


    val flow0 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow0", channel, 400, 0, "this is flow-000000"));
    val flow1 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1", channel, 100, 0, "this is flow-000001"));
    val flow2 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow2", channel, 100, 0, "this is flow-000002"));
    val flow3 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow3", channel, 200, 0, "this is flow-000003"));
    val flow4 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow4", channel, 400, 0, "this is flow-000004"));
    val flow5 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow5", channel, 300, 0, "this is flow-000005"));
    val flow6 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow6", channel, 500, 0, "this is flow-000006"));
    val flow7 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow7", channel, 100, 0, "this is flow-000007"));
    val flow8 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow8", channel, 200, 0, "this is flow-000008"));
    val flow9 = KMFlow.initWithFlowInfo(new KMFlowInfo("flow9", channel, 300, 0, "this is flow-000009"));

//    val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();
//
//    flows ++= Array(flow0, flow3, flow2, flow1);
//
//    flows --= Array(flow1, flow3, flow0, flow2);



//    val ports: Set[KMPort] = Set[KMPort]();
//    ports += ingress;
//    ports += egress;
//    ports += egress;




//    val flow1cp = KMFlow.initWithFlowInfo(new KMFlowInfo("flow1", channel, 100, 0, "this is flow-000001"));
//    val flag: Boolean = flow1.equals(flow1cp);
//
//    val flows: ArrayBuffer[KMFlow] = ArrayBuffer[KMFlow]();
//    flows += flow1cp;
//    val res1: Boolean = flows.contains(flow1);
//    val res2: Boolean = flows.contains(flow1cp);

    /**
      * TEST CASE
      */
    //    flow1.remSize.compressedSize = 49;
    //    flow1.remSize.rawSize = 0;
    //    flow1.updateFlowWithCompressionTimeSlice(0.1);
    //    return ;




    val arr: Array[Double] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    val avg: Double = arr.sum / arr.length;
    println(s"avg: $avg");


    val end = 0;

  }

}
