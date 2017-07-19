/**
  * Created by zhouqihua on 2017/7/19.
  */

import java.math.BigDecimal

object KMScalaKit {
  def BigDemicalDoubleAdd(number1: Double, number2: Double): Double = {
    val a: BigDecimal = new BigDecimal((number1.toString));
    val b: BigDecimal = new BigDecimal(number2.toString());
    val sum: Double =  a.add(b).doubleValue();

    return  sum;
  }
}
