import java.util.concurrent.Executors

import Events.IBEvent
import Main.{host, log}
import Requests._
import akka.stream.{Materializer, OverflowStrategy}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.{Done, NotUsed}
import com.ib.client.{EClientSocket, EReader, EReaderSignal}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

object IBTws {

  private val log = LoggerFactory.getLogger(getClass)

  def startReader(socket: EClientSocket, readerSignal: EReaderSignal) = Future {
    val reader = new EReader(socket, readerSignal)
    reader.start()
    while (true) {
      if (socket.isConnected) {
        readerSignal.waitForSignal()
        try {
          reader.processMsgs()
        } catch {
          case NonFatal(e) =>
            log.warn("Error in reader thread", e)
        }
      } else {
        socket.eConnect(host, 7497, 0)
        socket.startAPI()
      }
    }
  }(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

  def source(implicit m: Materializer): (Source[IBEvent, NotUsed], IBEventWrapper) = {
    val (publisherActor, pub) = Source.actorRef[IBEvent](512, OverflowStrategy.dropHead)
      .toMat(Sink.asPublisher(true))(Keep.both).run()
    val wrapper = new IBEventWrapper(publisherActor ! _)
    (Source.fromPublisher(pub), wrapper)
  }

  def sink(socket: EClientSocket): Sink[IBRequest, Future[Done]] = Sink.foreach[IBRequest] {
    case req: SoftDollarTiers => socket.reqSoftDollarTiers(req.reqId)
    case req: MarketDataType => socket.reqMarketDataType(req.marketDataType)
    case req: FundamentalData => socket.reqFundamentalData(req.reqId, req.contract, req.reportType)
    case req: HistoricalData => socket.reqHistoricalData(req.tickerId, req.contract, req.endDateTime,
      req.durationStr, req.barSizeSetting, req.whatToShow, req.useRTH, req.formatDate, req.chartOptions)
    case req: AccountUpdates => socket.reqAccountUpdates(req.subscribe, req.acctCode)
    case req: MktDepth => socket.reqMktDepth(req.tickerId, req.contract, req.numRows, req.mktDepthOptions)
    case req: SecDefOptParams => socket.reqSecDefOptParams(req.reqId, req.underlyingSymbol, req.futFopExchange, req.underlyingSecType, req.underlyingConId)
    case req: NewsBulletins => socket.reqNewsBulletins(req.allMsgs)
    case req: PositionsMulti => socket.reqPositionsMulti(req.reqId, req.account, req.modelCode)
    case req: RealTimeBars => socket.reqRealTimeBars(req.tickerId, req.contract, req.barSize, req.whatToShow, req.useRTH, req.realTimeBarsOptions)
    case req: Ids => socket.reqIds(req.numIds)
    case req: CalculateImpliedVolatility => socket.calculateImpliedVolatility(req.reqId, req.contract, req.optionPrice, req.underPrice)
    case req: MktData => socket.reqMktData(req.i, req.contract, req.s, req.b, req.b1, req.list)
    case req: AccountSummary => socket.reqAccountSummary(req.reqId, req.group, req.tags)
    case req: AutoOpenOrders => socket.reqAutoOpenOrders(req.bAutoBind)
    case req: ContractDetails => socket.reqContractDetails(req.reqId, req.contract)
    case req: ReplaceFA => socket.replaceFA(req.faDataType, req.xml)
    case req: AccountUpdatesMulti => socket.reqAccountUpdatesMulti(req.reqId, req.account, req.modelCode, req.ledgerAndNLV)
    case req: PlaceOrder => socket.placeOrder(req.id, req.contract, req.order)
    case req: Executions => socket.reqExecutions(req.reqId, req.filter)
    case req: RequestFA => socket.requestFA(req.faDataType)
    case req: CalculateOptionPrice => socket.calculateOptionPrice(req.reqId, req.contract, req.volatility, req.underPrice)
    case req: ScannerSubscription => socket.reqScannerSubscription(req.tickerId, req.subscription, req.scannerSubscriptionOptions)
    case req: HistogramData => socket.reqHistogramData(req.i, req.contract, req.b, req.s)
    case req: MatchingSymbols => socket.reqMatchingSymbols(req.i, req.s)
    case req: CancelOrder => socket.cancelOrder(req.i)
    case req: CancelAccountSummary => socket.cancelAccountSummary(req.i)
    case req: CancelAccountUpdatesMulti => socket.cancelAccountUpdatesMulti(req.i)
    case req: UnsubscribeFromGroupEvents => socket.unsubscribeFromGroupEvents(req.i)
    case req: VerifyMessage => socket.verifyMessage(req.s)
    case req: QueryDisplayGroups => socket.queryDisplayGroups(req.i)
    case OpenOrders => socket.reqOpenOrders()
    case ScannerParameters => socket.reqScannerParameters()
    case CurrentTime => socket.reqCurrentTime()
    case AllOpenOrders => socket.reqAllOpenOrders()
    case GlobalCancel => socket.reqGlobalCancel()
    case Positions => socket.reqPositions()
    case StartAPI => socket.startAPI()
    case CancelPositions => socket.cancelPositions()
    case TwsConnectionTime => socket.TwsConnectionTime()
    case ServerVersion => socket.serverVersion()
  }

}
