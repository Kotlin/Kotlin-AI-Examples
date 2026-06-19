package org.example

import kotlinx.serialization.Serializable

/**
 * Represents a financial quote with various details about a stock.
 *
 * @property symbol The stock ticker symbol or financial instrument identifier.
 * @property name The full name of the financial instrument.
 * @property price The current price of the financial instrument.
 * @property changesPercentage The percentage change in price for the financial instrument.
 * @property dayLow The lowest trading price of the day.
 * @property dayHigh The highest trading price of the day.
 * @property yearHigh The highest price over the last year.
 * @property yearLow The lowest price over the last year.
 * @property marketCap The market capitalization value.
 * @property priceAvg50 The 50-day average price.
 * @property priceAvg200 The 200-day average price.
 * @property exchange The name of the exchange where the instrument is traded.
 * @property volume The trading volume of the financial instrument.
 * @property open The opening price at the start of the trading day.
 * @property previousClose The closing price from the previous trading day.
 * @property eps The earnings per share (EPS) value.
 * @property pe The price-to-earnings (P/E) ratio.
 * @property earningsAnnouncement The date and time of the last earnings announcement.
 * @property sharesOutstanding The total number of shares outstanding.
 * @property timestamp The timestamp of when the quote data was last updated.
 */
@Serializable
data class Quote(
    val symbol: String,
    val name: String,
    val price: Double,
    val changesPercentage: Double,
    val dayLow: Double,
    val dayHigh: Double,
    val yearHigh: Double,
    val yearLow: Double,
    val marketCap: Long,
    val priceAvg50: Double,
    val priceAvg200: Double,
    val exchange: String,
    val volume: Long,
    val open: Double,
    val previousClose: Double,
    val eps: Double,
    val pe: Double,
    val earningsAnnouncement: String,
    val sharesOutstanding: Long,
    val timestamp: Long
)

/**
 * Represents historical stock data for a specific date.
 *
 * @property date The date associated with the historical data in YYYY-MM-DD format.
 * @property open The opening price of the stock on the specified date.
 * @property high The highest price reached by the stock on the specified date.
 * @property low The lowest price reached by the stock on the specified date.
 * @property close The closing price of the stock on the specified date.
 */
@Serializable
data class Historical(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
)

/**
 * Represents historical price data for a specific financial asset.
 *
 * @property symbol The ticker symbol of the asset.
 * @property historical A list of historical data points, each represented by the `Historical` data class.
 */
@Serializable
data class HistoricalPrice(
    val symbol: String,
    val historical: List<Historical>
)