/**
  * Created by zhouqihua on 2017/7/12.
  */

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






  }

}
