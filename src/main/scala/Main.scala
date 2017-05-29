import Events._
import Requests.IBRequest
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("Main")
  implicit val materializer = ActorMaterializer()

  val log = LoggerFactory.getLogger(getClass)
  val ib = new IBTws("127.0.0.1")

  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val tick: Source[IBRequest, Cancellable] =
      Source.tick(
        10.seconds, 10.seconds,
        Requests.AccountSummary(1, "All", "CashBalance")
      )

    val ibEvents = b.add(ib.source)
    val ibCommands = b.add(ib.sink)

    tick ~> ibCommands

    ibEvents ~> Sink.foreach[IBEvent]({
      case ConnectionOK =>
        log.info(s"Connection Success")
      case ExceptionError(e) =>
        log.warn(s"IB Returned an Exception", e)
      case ErrorMessage(m) =>
        log.warn(s"IB Error Message Returned: $m")
      case Error(id, code, msg) =>
        log.warn(s"IB Error id: $id, code: $code, message: $msg")
      case other =>
        log.info(s"Received IB Message: $other")
    })
    ClosedShape
  })

  graph.run()
}
