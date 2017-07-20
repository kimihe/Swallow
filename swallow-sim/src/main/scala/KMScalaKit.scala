/**
  * Created by zhouqihua on 2017/7/19.
  */

import java.math.BigDecimal

object KMScalaKit {

  def bigDemicalDoubleAdd(number1: Double, number2: Double): Double = {
    val a: BigDecimal = new BigDecimal((number1.toString));
    val b: BigDecimal = new BigDecimal(number2.toString());
    val sum: Double =  a.add(b).doubleValue();

    return  sum;
  }

  def bigDemicalDoubleSub(number1: Double, number2: Double): Double = {
    val a: BigDecimal = new BigDecimal((number1.toString));
    val b: BigDecimal = new BigDecimal(number2.toString());
    val res: Double =  a.subtract(b).doubleValue();

    return  res;
  }

  def bigDemicalDoubleMul(number1: Double, number2: Double): Double = {
    val a: BigDecimal = new BigDecimal((number1.toString));
    val b: BigDecimal = new BigDecimal(number2.toString());
    val res: Double = a.multiply(b).doubleValue();

    return res;
  }

  def bigDemicalDoubleDiv(number1: Double, number2: Double): Double = {
    val a: BigDecimal = new BigDecimal((number1.toString));
    val b: BigDecimal = new BigDecimal(number2.toString());
    val res: Double =  a.divide(b).doubleValue();

    return  res;
  }
}
