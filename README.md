# akka-stream-ib-tws

An akka-stream application that integrates with the [Interactive Brokers Trader Workstation API](http://interactivebrokers.github.io/tws-api/#gsc.tab=0)

## Features

- Boilerplate ADT for all `EWrapper` methods (see `Events.scala`). 
- Boilerplate ADT for most requests from `EClientSocket` (see `Requests.scala`).

## Limitations

- At the moment not all `EClientSocket` methods have an accompanying `Request` ADT entry: Only `EClientSocket` methods starting with `req` are implemented. PRs for the outstanding methods are very welcome.
- No Tests(!)

## Usage

- Clone this repo
- Add the IB TWS Jar to the lib directory
- Configure TWS to allow API access
- ?????
- PROFIT

## Example

The following graph will subscribe to account updates (`Main.scala` in the repo). 

```scala 
object Main extends App {

  implicit val system = ActorSystem("Main")
  implicit val materializer = ActorMaterializer()

  val log = LoggerFactory.getLogger(getClass)
  val host = "localhost"

  val nextValidId: AtomicInteger = new AtomicInteger(0)

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
      case m: UpdateAccountValue =>
        log.info(s"Account Update: $m")
      case _ =>
    })
    ClosedShape
  })

  graph.run()
}

```