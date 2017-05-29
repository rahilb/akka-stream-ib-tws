import Events.IBEvent
import Requests._
import akka.stream.scaladsl.{Sink, Source}
import akka.{Done, NotUsed}
import com.ib.client.{EClientSocket, EJavaSignal, EReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.Try

class IBTws(host: String) {

  private val readerSignal = new EJavaSignal()
  private val wrapper = new IBEventWrapper
  private val socket: EClientSocket = new EClientSocket(wrapper, readerSignal)
  private val reader = new EReader(socket, readerSignal)

  socket.eConnect(host, 7497, 0)
  socket.startAPI()
  reader.start()

  Future(
    while (socket.isConnected) {
      readerSignal.waitForSignal()
      Try(reader.processMsgs())
    }
  )

  lazy val source: Source[IBEvent, NotUsed] =
    Source.fromIterator[IBEvent](() => {
      wrapper.buffer.iterator().asScala
    })

  lazy val sink: Sink[IBRequest, Future[Done]] = Sink.foreach[IBRequest]({
    case SoftDollarTiers(r) => socket.reqSoftDollarTiers(r)
    case OpenOrders => socket.reqOpenOrders()
    case req: MarketDataType => socket.reqMarketDataType(req.marketDataType)
    case req: FundamentalData => socket.reqFundamentalData(req.reqId, req.contract, req.reportType)
    case req: HistoricalData => socket.reqHistoricalData(req.tickerId, req.contract, req.endDateTime,
      req.durationStr, req.barSizeSetting, req.whatToShow, req.useRTH, req.formatDate, req.chartOptions)
    case req: AccountUpdates => socket.reqAccountUpdates(req.subscribe, req.acctCode)
    case req: MktDepth => socket.reqMktDepth(req.tickerId, req.contract, req.numRows, req.mktDepthOptions)
    case req: SecDefOptParams => socket.reqSecDefOptParams(req.reqId, req.underlyingSymbol, req.futFopExchange, req.underlyingSecType, req.underlyingConId)
    case req: NewsBulletins => socket.reqNewsBulletins(req.allMsgs)
    case req: PositionsMulti => socket.reqPositionsMulti(req.reqId, req.account, req.modelCode)
    case ScannerParameters => socket.reqScannerParameters()
    case req: RealTimeBars => socket.reqRealTimeBars(req.tickerId, req.contract, req.barSize, req.whatToShow, req.useRTH, req.realTimeBarsOptions)
    case req: Ids => socket.reqIds(req.numIds)
    case CurrentTime => socket.reqCurrentTime()
    case req: CalculateImpliedVolatility => socket.calculateImpliedVolatility(req.reqId, req.contract, req.optionPrice, req.underPrice)
    case req: MktData => socket.reqMktData(req.i, req.contract, req.s, req.b, req.list)
    case AllOpenOrders => socket.reqAllOpenOrders()
    case GlobalCancel => socket.reqGlobalCancel()
    case req: AccountSummary => socket.reqAccountSummary(req.reqId, req.group, req.tags)
    case req: AutoOpenOrders => socket.reqAutoOpenOrders(req.bAutoBind)
    case Positions => socket.reqPositions()
    case req: ContractDetails => socket.reqContractDetails(req.reqId, req.contract)
    case req: ReplaceFA => socket.replaceFA(req.faDataType, req.xml)
    case req: AccountUpdatesMulti => socket.reqAccountUpdatesMulti(req.reqId, req.account, req.modelCode, req.ledgerAndNLV)
    case req: PlaceOrder => socket.placeOrder(req.id, req.contract, req.order)
    case req: Executions => socket.reqExecutions(req.reqId, req.filter)
    case req: RequestFA => socket.requestFA(req.faDataType)
    case req: CalculateOptionPrice => socket.calculateOptionPrice(req.reqId, req.contract, req.volatility, req.underPrice)
    case req: ScannerSubscription => socket.reqScannerSubscription(req.tickerId, req.subscription, req.scannerSubscriptionOptions)
  })

}
