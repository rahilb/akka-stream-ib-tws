import Events._
import Requests.IBRequest
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import com.ib.client.{EClientSocket, EJavaSignal}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("Main")
  implicit val materializer = ActorMaterializer()

  val log = LoggerFactory.getLogger(getClass)
  val host = "localhost"

  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val readerSignal = new EJavaSignal()
    val (source, wrapper) = IBTws.source
    val socket: EClientSocket = new EClientSocket(wrapper, readerSignal)
    val sink = IBTws.sink(socket)

    socket.eConnect(host, 7497, 0)
    socket.startAPI()
    IBTws.startReader(socket, readerSignal)

    val accountUpdates: Source[IBRequest, NotUsed] =
      Source.single(
        Requests.AccountUpdates(subscribe = true, "DU533521")
      ).delay(20.seconds)

    val ibEvents = b.add(source)
    val ibCommands = b.add(sink)

    accountUpdates ~> ibCommands

    ibEvents ~> Sink.foreach[IBEvent]({
      case ConnectionOK =>
        log.info(s"Connection Success")
      case ExceptionError(e) =>
        log.warn(s"IB Returned an Exception", e)
      case ErrorMessage(m) =>
        log.warn(s"IB Error Message Returned: $m")
      case Error(id, code, msg) =>
        log.warn(s"IB Error id: $id, code: $code, message: $msg")
      case m: UpdateAccountValue =>
        log.info(s"Account Update: $m")
      case _ =>
    })
    ClosedShape
  })

  graph.run()
}
