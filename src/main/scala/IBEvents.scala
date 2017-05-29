import java.util.Map
import java.{lang, util}

import com.ib.client
import com.ib.client._
import org.slf4j.LoggerFactory

import scala.collection.parallel.mutable.ParArray

object Events {

  sealed trait IBEvent
  case object ConnectionOK extends IBEvent
  case class HistoricalData(reqId: Int, date: String, open: Double, high: Double, low: Double, close: Double,
                            volume: Int, count: Int, WAP: Double, hasGaps: Boolean) extends IBEvent
  case class ScannerDataEnd(reqId: Int) extends IBEvent
  case class DeltaNeutralValidation(reqId: Int, underComp: DeltaNeutralContract) extends IBEvent
  case class TickOptionComputation(tickerId: Int, field: Int, impliedVol: Double,
                                   delta: Double, optPrice: Double, pvDividend: Double,
                                   gamma: Double, vega: Double, theta: Double, undPrice: Double) extends IBEvent
  case class SoftDollarTiers(reqId: Int, tiers: Array[SoftDollarTier]) extends IBEvent
  case class DisplayGroupUpdated(reqId: Int, contractInfo: String) extends IBEvent
  case class PositionMultiEnd(reqId: Int) extends IBEvent
  case class FundamentalData(reqId: Int, data: String) extends IBEvent
  case class BondContractDetails(reqId: Int, contractDetails: client.ContractDetails) extends IBEvent
  case class NextValidId(orderId: Int) extends IBEvent
  case class PositionMulti(reqId: Int, account: String, modelCode: String, contract: Contract, pos: Double, avgCost: Double) extends IBEvent
  case class UpdateAccountTime(timeStamp: String) extends IBEvent
  case class MarketDataType(reqId: Int, marketDataType: Int) extends IBEvent
  case class AccountDownloadEnd(accountName: String) extends IBEvent
  case class ExecDetailsEnd(reqId: Int) extends IBEvent
  case class AccountSummary(reqId: Int, account: String, tag: String, value: String, currency: String) extends IBEvent
  case class CurrentTime(time: Long) extends IBEvent
  case class UpdateAccountValue(key: String, value: String, currency: String, accountName: String) extends IBEvent
  case class TickPrice(i: Int, i1: Int, v: Double, i2: Int) extends IBEvent
  case class TickSnapshotEnd(reqId: Int) extends IBEvent
  case class TickEFP(tickerId: Int, tickType: Int, basisPoints: Double, formattedBasisPoints: String, impliedFuture: Double, holdDays: Int, futureLastTradeDate: String, dividendImpact: Double, dividendsToLastTradeDate: Double) extends IBEvent
  case class Position(account: String, contract: Contract, pos: Double, avgCost: Double) extends IBEvent
  case class ExecDetails(reqId: Int, contract: Contract, execution: Execution) extends IBEvent
  case object PositionEnd extends IBEvent
  case class Error(id: Int, errorCode: Int, errorMsg: String) extends IBEvent
  case class TickGeneric(tickerId: Int, tickType: Int, value: Double) extends IBEvent
  case class UpdateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String) extends IBEvent
  case class RealtimeBar(reqId: Int, time: Long, open: Double, high: Double, low: Double, close: Double, volume: Long, wap: Double, count: Int) extends IBEvent
  case class AccountSummaryEnd(reqId: Int) extends IBEvent
  case class ScannerData(reqId: Int, rank: Int, contractDetails: client.ContractDetails, distance: String, benchmark: String, projection: String, legsStr: String) extends IBEvent
  case class UpdatePortfolio(contract: Contract, position: Double, marketPrice: Double, marketValue: Double, averageCost: Double, unrealizedPNL: Double, realizedPNL: Double, accountName: String) extends IBEvent
  case class SecurityDefinitionOptionalParameterEnd(reqId: Int) extends IBEvent
  case class OrderStatus(orderId: Int, status: String, filled: Double, remaining: Double, avgFillPrice: Double, permId: Int, parentId: Int, lastFillPrice: Double, clientId: Int, whyHeld: String) extends IBEvent
  case class ExceptionError(e: Exception) extends IBEvent
  case class ErrorMessage(str: String) extends IBEvent
  case class TickSize(tickerId: Int, field: Int, size: Int) extends IBEvent
  case class AccountUpdateMultiEnd(reqId: Int) extends IBEvent
  case class VerifyCompleted(isSuccessful: Boolean, errorText: String) extends IBEvent
  case class CommissionReport(commissionReport: client.CommissionReport) extends IBEvent
  case class VerifyMessageAPI(apiData: String) extends IBEvent
  case class UpdateMktDepthL2(tickerId: Int, position: Int, marketMaker: String, operation: Int, side: Int, price: Double, size: Int) extends IBEvent
  case class ContractDetails(reqId: Int, contractDetails: client.ContractDetails) extends IBEvent
  case class DisplayGroupList(reqId: Int, groups: String) extends IBEvent
  case class ManagedAccounts(accountsList: String) extends IBEvent
  case class ContractDetailsEnd(reqId: Int) extends IBEvent
  case class AccountUpdateMulti(reqId: Int, account: String, modelCode: String, key: String, value: String, currency: String) extends IBEvent
  case class TickString(tickerId: Int, tickType: Int, value: String) extends IBEvent
  case class OpenOrder(orderId: Int, contract: Contract, order: Order, orderState: OrderState) extends IBEvent
  case class UpdateMktDepth(tickerId: Int, position: Int, operation: Int, side: Int, price: Double, size: Int) extends IBEvent
  case class ReceiveFA(faDataType: Int, xml: String) extends IBEvent
  case object OpenOrderEnd extends IBEvent
  case object ConnectionClosed extends IBEvent
  case class ScannerParameters(xml: String) extends IBEvent
  case class VerifyAndAuthMessageAPI(apiData: String, xyzChallange: String) extends IBEvent
  case class VerifyAndAuthCompleted(isSuccessful: Boolean, errorText: String) extends IBEvent
  case class SecurityDefinitionOptionalParameter(reqId: Int, exchange: String, underlyingConId: Int, tradingClass: String, multiplier: String, expirations: util.Set[String], strikes: util.Set[lang.Double]) extends IBEvent
  case class SymbolSamples(i: Int, contractDescriptions: Array[ContractDescription]) extends IBEvent
  case class HistoricalNews(i: Int, s: String, s1: String, s2: String, s3: String) extends IBEvent
  case class TickPriceWithAttr(i: Int, i1: Int, v: Double, tickAttr: TickAttr) extends IBEvent
  case class SmartComponents(i: Int, map: util.Map[Integer, Map.Entry[String, Character]]) extends IBEvent
  case class NewsArticle(i: Int, i1: Int, s: String) extends IBEvent
  case class NewsProviders(newsProviders: Array[NewsProvider]) extends IBEvent
  case class HeadTimestamp(i: Int, s: String) extends IBEvent
  case class TickReqParams(i: Int, v: Double, s: String, i1: Int) extends IBEvent
  case class TickNews(i: Int, l: Long, s: String, s1: String, s2: String, s3: String) extends IBEvent
  case class HistoricalDataEnd(i: Int, s: String, s1: String) extends IBEvent
  case class HistogramData(i: Int, list: util.List[Map.Entry[lang.Double, lang.Long]]) extends IBEvent
  case class FamilyCodes(familyCodes: Array[FamilyCode]) extends IBEvent
  case class HistoricalNewsEnd(i: Int, b: Boolean) extends IBEvent
  case class MktDepthExchanges(depthMktDataDescriptions: Array[DepthMktDataDescription]) extends IBEvent

}

class IBEventWrapper(f: Events.IBEvent => Unit) extends EWrapper {
  import Events._

  val log = LoggerFactory.getLogger(getClass)

  override def historicalData(reqId: Int, date: String, open: Double, high: Double, low: Double, close: Double,
                              volume: Int, count: Int, WAP: Double, hasGaps: Boolean): Unit = {
    f(HistoricalData(reqId, date, open, high, low, close, volume, count, WAP,hasGaps))
  }

  override def scannerDataEnd(reqId: Int): Unit = {
    f(ScannerDataEnd(reqId))
  }

  override def deltaNeutralValidation(reqId: Int, underComp: DeltaNeutralContract): Unit = {
    f(DeltaNeutralValidation(reqId, underComp))
  }

  override def scannerParameters(xml: String): Unit = f(ScannerParameters(xml))

  override def verifyAndAuthMessageAPI(apiData: String, xyzChallange: String): Unit =
    f(VerifyAndAuthMessageAPI(apiData, xyzChallange))

  override def verifyAndAuthCompleted(isSuccessful: Boolean, errorText: String): Unit = {}

  override def tickOptionComputation(tickerId: Int, field: Int, impliedVol: Double,
                                     delta: Double, optPrice: Double, pvDividend: Double,
                                     gamma: Double, vega: Double, theta: Double, undPrice: Double): Unit = {
    f(TickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend,
      gamma, vega, theta, undPrice))
  }

  override def softDollarTiers(reqId: Int, tiers: Array[SoftDollarTier]): Unit =
    f(SoftDollarTiers(reqId, tiers))

  override def displayGroupUpdated(reqId: Int, contractInfo: String): Unit =
    f(DisplayGroupUpdated(reqId, contractInfo))

  override def positionMultiEnd(reqId: Int): Unit =
    f(PositionMultiEnd(reqId))

  override def fundamentalData(reqId: Int, data: String): Unit = f(FundamentalData(reqId, data))

  override def nextValidId(orderId: Int): Unit = f(NextValidId(orderId))

  override def positionMulti(reqId: Int, account: String, modelCode: String,
                             contract: Contract, pos: Double, avgCost: Double): Unit =
    f(PositionMulti(reqId, account, modelCode, contract, pos, avgCost))

  override def updateAccountTime(timeStamp: String): Unit = f(UpdateAccountTime(timeStamp))

  override def marketDataType(reqId: Int, marketDataType: Int): Unit = f(MarketDataType(reqId, marketDataType))

  override def accountDownloadEnd(accountName: String): Unit = f(AccountDownloadEnd(accountName))

  override def execDetailsEnd(reqId: Int): Unit = f(ExecDetailsEnd(reqId))

  override def accountSummary(reqId: Int, account: String, tag: String, value: String, currency: String): Unit =
    f(AccountSummary(reqId, account, tag, value, currency))

  override def currentTime(time: Long): Unit = f(CurrentTime(time))

  override def updateAccountValue(key: String, value: String, currency: String, accountName: String): Unit =
    f(UpdateAccountValue(key, value, currency, accountName))

  override def tickSnapshotEnd(reqId: Int): Unit =
    f(TickSnapshotEnd(reqId))

  override def tickEFP(tickerId: Int, tickType: Int, basisPoints: Double, formattedBasisPoints: String,
                       impliedFuture: Double, holdDays: Int, futureLastTradeDate: String,
                       dividendImpact: Double, dividendsToLastTradeDate: Double): Unit =
    f(TickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture,
      holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate))

  override def position(account: String, contract: Contract, pos: Double, avgCost: Double): Unit =
    f(Position(account, contract, pos, avgCost))

  override def execDetails(reqId: Int, contract: Contract, execution: Execution): Unit =
    f(ExecDetails(reqId, contract, execution))

  override def positionEnd(): Unit =
    f(PositionEnd)

  override def tickGeneric(tickerId: Int, tickType: Int, value: Double): Unit =
    f(TickGeneric(tickerId, tickType, value))

  override def updateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String): Unit =
    f(UpdateNewsBulletin(msgId, msgType, message, origExchange))

  override def realtimeBar(reqId: Int, time: Long, open: Double, high: Double, low: Double,
                           close: Double, volume: Long, wap: Double, count: Int): Unit =
    f(RealtimeBar(reqId, time, open, high, low, close, volume, wap, count))

  override def accountSummaryEnd(reqId: Int): Unit =
    f(AccountSummaryEnd(reqId))

  override def updatePortfolio(contract: Contract, position: Double, marketPrice: Double, marketValue: Double,
                               averageCost: Double, unrealizedPNL: Double, realizedPNL: Double, accountName: String): Unit =
    f(UpdatePortfolio(contract, position, marketPrice, marketValue,
      averageCost, unrealizedPNL, realizedPNL, accountName))

  override def securityDefinitionOptionalParameterEnd(reqId: Int): Unit =
    f(SecurityDefinitionOptionalParameterEnd(reqId))

  override def orderStatus(orderId: Int, status: String, filled: Double, remaining: Double, avgFillPrice: Double,
                           permId: Int, parentId: Int, lastFillPrice: Double, clientId: Int, whyHeld: String): Unit =
    f(OrderStatus(orderId, status, filled, remaining, avgFillPrice, permId,
      parentId, lastFillPrice, clientId, whyHeld))

  override def error(e: Exception): Unit =
    f(ExceptionError(e))

  override def error(str: String): Unit =
    f(ErrorMessage(str))

  override def error(id: Int, errorCode: Int, errorMsg: String): Unit =
    f(Error(id, errorCode, errorMsg))

  override def tickSize(tickerId: Int, field: Int, size: Int): Unit =
    f(TickSize(tickerId, field, size))

  override def accountUpdateMultiEnd(reqId: Int): Unit =
    f(AccountUpdateMultiEnd(reqId))

  override def verifyCompleted(isSuccessful: Boolean, errorText: String): Unit =
    f(VerifyCompleted(isSuccessful, errorText))

  override def verifyMessageAPI(apiData: String): Unit =
    f(VerifyMessageAPI(apiData))

  override def updateMktDepthL2(tickerId: Int, position: Int, marketMaker: String, operation: Int, side: Int, price: Double, size: Int): Unit =
    f(UpdateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size))

  override def displayGroupList(reqId: Int, groups: String): Unit =
    f(DisplayGroupList(reqId, groups))

  override def managedAccounts(accountsList: String): Unit =
    f(ManagedAccounts(accountsList))

  override def contractDetailsEnd(reqId: Int): Unit =
    f(ContractDetailsEnd(reqId))

  override def accountUpdateMulti(reqId: Int, account: String, modelCode: String, key: String, value: String, currency: String): Unit =
    f(AccountUpdateMulti(reqId, account, modelCode, key, value, currency))

  override def tickString(tickerId: Int, tickType: Int, value: String): Unit =
    f(TickString(tickerId, tickType, value))

  override def openOrder(orderId: Int, contract: Contract, order: Order, orderState: OrderState): Unit =
    f(OpenOrder(orderId, contract, order, orderState))

  override def updateMktDepth(tickerId: Int, position: Int, operation: Int, side: Int, price: Double, size: Int): Unit =
    f(UpdateMktDepth(tickerId, position, operation, side, price, size))

  override def receiveFA(faDataType: Int, xml: String): Unit =
    f(ReceiveFA(faDataType, xml))

  override def openOrderEnd(): Unit =
    f(OpenOrderEnd)

  override def connectionClosed(): Unit =
    f(ConnectionClosed)

  override def connectAck(): Unit =
    f(ConnectionOK)

  override def securityDefinitionOptionalParameter(reqId: Int, exchange: String, underlyingConId: Int,
                                                   tradingClass: String, multiplier: String,
                                                   expirations: util.Set[String], strikes: util.Set[lang.Double]): Unit =
    f(SecurityDefinitionOptionalParameter(reqId, exchange, underlyingConId,
      tradingClass, multiplier, expirations, strikes))

  override def bondContractDetails(reqId: Int, contractDetails: client.ContractDetails): Unit =
    f(BondContractDetails(reqId, contractDetails))

  override def scannerData(reqId: Int, rank: Int, contractDetails: client.ContractDetails,
                           distance: String, benchmark: String, projection: String, legsStr: String): Unit =
    f(ScannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr))

  override def commissionReport(commissionReport: client.CommissionReport): Unit =
    f(CommissionReport(commissionReport))

  override def contractDetails(reqId: Int, contractDetails: client.ContractDetails): Unit =
    f(ContractDetails(reqId, contractDetails))

  override def symbolSamples(i: Int, contractDescriptions: Array[ContractDescription]): Unit =
    f(SymbolSamples(i, contractDescriptions))

  override def historicalNews(i: Int, s: String, s1: String, s2: String, s3: String): Unit =
    f(HistoricalNews(i, s, s1, s2, s3))

  override def tickPrice(i: Int, i1: Int, v: Double, tickAttr: TickAttr): Unit =
    f(TickPriceWithAttr(i, i1, v, tickAttr))

  override def smartComponents(i: Int, map: util.Map[Integer, Map.Entry[String, Character]]): Unit =
    f(SmartComponents(i, map))

  override def newsArticle(i: Int, i1: Int, s: String): Unit =
    f(NewsArticle(i, i1, s))

  override def newsProviders(newsProviders: Array[NewsProvider]): Unit =
    f(NewsProviders(newsProviders))

  override def headTimestamp(i: Int, s: String): Unit =
    f(HeadTimestamp(i, s))

  override def tickReqParams(i: Int, v: Double, s: String, i1: Int): Unit =
    f(TickReqParams(i, v, s, i1))

  override def tickNews(i: Int, l: Long, s: String, s1: String, s2: String, s3: String): Unit =
    f(TickNews(i, l, s, s1, s2, s3))

  override def historicalDataEnd(i: Int, s: String, s1: String): Unit =
    f(HistoricalDataEnd(i, s, s1))

  override def histogramData(i: Int, list: util.List[Map.Entry[lang.Double, lang.Long]]): Unit =
    f(HistogramData(i, list))

  override def familyCodes(familyCodes: Array[FamilyCode]): Unit =
    f(FamilyCodes(familyCodes))

  override def historicalNewsEnd(i: Int, b: Boolean): Unit =
    f(HistoricalNewsEnd(i, b))

  override def mktDepthExchanges(depthMktDataDescriptions: Array[DepthMktDataDescription]): Unit =
    f(MktDepthExchanges(depthMktDataDescriptions))
}
