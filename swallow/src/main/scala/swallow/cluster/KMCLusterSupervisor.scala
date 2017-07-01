package swallow.cluster

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.Props
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import swallow.core.{KMFlow, KMNode}
import swallow.core.KMActorMessages._


object KMClusterSupervisor {
  def props: Props = Props(new KMClusterSupervisor)
}

class KMClusterSupervisor extends KMNode {

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive = {
    case state: CurrentClusterState =>
      log.info("****** Current members: {} ******", state.members.mkString(", "))
    case MemberUp(member) =>
      log.info("****** Member is Up: {} ******", member.address)
    case UnreachableMember(member) =>
      log.info("****** Member detected as unreachable: {} ******", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("****** Member is Removed: {} after {} ******",
        member.address, previousStatus)
    case _: MemberEvent => // ignore


    case ClusterSuperviseFlow(flow: KMFlow) =>
      log.info(s"[KMClusterListener] superviseFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")
  }
}
