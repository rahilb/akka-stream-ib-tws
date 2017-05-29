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

The following graph will request an account summary every 10 seconds (`Main.scala` in the repo). 

```scala 
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
        log.info(s"Receieved IB Message: $other")
    })
    ClosedShape
  })

  graph.run()
}
```