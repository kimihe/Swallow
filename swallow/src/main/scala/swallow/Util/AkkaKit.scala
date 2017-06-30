package swallow.Util

/**
  * Created by zhouqihua on 2017/6/30.
  */

object AkkaKit {
  def serializedWith(sth: Any): Unit = {
    //      val serialization = SerializationExtension(context.system)
    //      // Have something to serialize
    //      val original = flow.flowInfo
    //
    //      // Find the Serializer for it
    //      val serializer = serialization.findSerializerFor(original)
    //      // Turn it into bytes
    //      val bytes = serializer.toBinary(original)
    //      // Turn it back into an object
    //      //val back = serializer.fromBinary(bytes, manifest = None)
    //
    //      localActor ! TransferFlowSeri(bytes)
  }
}
