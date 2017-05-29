import com.ib.client._
import com.ib.client
import java.util

object Requests {
  sealed trait IBRequest
  case object StartAPI extends IBRequest
  case object CancelPositions extends IBRequest
  case class HistogramData(i: Int, contract: Contract, b: Boolean, s: String) extends IBRequest
  case class MatchingSymbols(i: Int, s: String) extends IBRequest
  case class CancelOrder(i: Int) extends IBRequest
  case class CancelAccountSummary(i: Int) extends IBRequest
  case class CancelAccountUpdatesMulti(i: Int) extends IBRequest
  case class UnsubscribeFromGroupEvents(i: Int) extends IBRequest
  case class VerifyMessage(s: String) extends IBRequest
  case class QueryDisplayGroups(i: Int) extends IBRequest
  case object TwsConnectionTime extends IBRequest
  case object ServerVersion extends IBRequest
  case class SoftDollarTiers(reqId: Int) extends IBRequest
  case object OpenOrders extends IBRequest
  case class MarketDataType(marketDataType: Int) extends IBRequest
  case class FundamentalData(reqId: Int, contract: Contract, reportType: String) extends IBRequest
  case class HistoricalData(tickerId: Int, contract: Contract, endDateTime: String, durationStr: String, barSizeSetting: String, whatToShow: String, useRTH: Int, formatDate: Int, chartOptions: util.List[TagValue]) extends IBRequest
  case class AccountUpdates(subscribe: Boolean, acctCode: String) extends IBRequest
  case class MktDepth(tickerId: Int, contract: Contract, numRows: Int, mktDepthOptions: util.ArrayList[TagValue]) extends IBRequest
  case class SecDefOptParams(reqId: Int, underlyingSymbol: String, futFopExchange: String, underlyingSecType: String, underlyingConId: Int) extends IBRequest
  case class NewsBulletins(allMsgs: Boolean) extends IBRequest
  case class PositionsMulti(reqId: Int, account: String, modelCode: String) extends IBRequest
  case object ScannerParameters extends IBRequest
  case class RealTimeBars(tickerId: Int, contract: Contract, barSize: Int, whatToShow: String, useRTH: Boolean, realTimeBarsOptions: util.ArrayList[TagValue]) extends IBRequest
  case class Ids(numIds: Int) extends IBRequest
  case object CurrentTime extends IBRequest
  case class CalculateImpliedVolatility(reqId: Int, contract: Contract, optionPrice: Double, underPrice: Double) extends IBRequest
  case class MktData(i: Int, contract: Contract, s: String, b: Boolean, b1: Boolean, list: util.List[TagValue]) extends IBRequest
  case object AllOpenOrders extends IBRequest
  case object GlobalCancel extends IBRequest
  case class AccountSummary(reqId: Int, group: String, tags: String) extends IBRequest
  case class AutoOpenOrders(bAutoBind: Boolean) extends IBRequest
  case object Positions extends IBRequest
  case class ContractDetails(reqId: Int, contract: Contract) extends IBRequest
  case class ReplaceFA(faDataType: Int, xml: String) extends IBRequest
  case class AccountUpdatesMulti(reqId: Int, account: String, modelCode: String, ledgerAndNLV: Boolean) extends IBRequest
  case class PlaceOrder(id: Int, contract: Contract, order: Order) extends IBRequest
  case class Executions(reqId: Int, filter: ExecutionFilter) extends IBRequest
  case class RequestFA(faDataType: Int) extends IBRequest
  case class CalculateOptionPrice(reqId: Int, contract: Contract, volatility: Double, underPrice: Double) extends IBRequest
  case class ScannerSubscription(tickerId: Int, subscription: client.ScannerSubscription, scannerSubscriptionOptions: util.ArrayList[TagValue]) extends IBRequest
}
