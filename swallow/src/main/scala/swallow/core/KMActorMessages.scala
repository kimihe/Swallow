package swallow.core

/**
  * Created by zhouqihua on 2017/6/30.
  */

object KMActorMessages {

  final case class ClusterSuperviseFlow(flow: KMFlow)

  final case class MasterSubmitNewFlow(flow: KMFlow)
  final case class MasterDispatchNewFlow(flow: KMFlow)
  final case class MasterAggregateFlow(flow: KMFlow)

  final case class SenderTransmitFlow(flow: KMFlow)
  final case class SenderCompleteFlow(flow: KMFlow)

  final case class ReceiverGetFlow(flow: KMFlow)
}
