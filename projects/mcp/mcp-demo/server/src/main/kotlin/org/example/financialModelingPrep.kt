package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

private val fmpApiKey = System.getenv("FMP_API_KEY") ?: error("FMP_API_KEY environment variable is not set")

/**
 * Retrieves the current price of a stock
 *
 * @param symbol The stock ticker symbol or financial instrument identifier for which to fetch the price.
 * @return A [Quote] object containing the details of the financial instrument's current price, or null if the request is unsuccessful or the symbol is invalid.
 */
suspend fun HttpClient.getCurrentPrice(symbol: String): Quote? {
    val response = this.get("quote/${symbol.uppercase()}") {
        parameter("apikey", fmpApiKey)
    }.takeIf { it.status == HttpStatusCode.OK } ?: run {
        System.err.println("Failed to retrieve data for $symbol")
        null
    }

    return response?.body<List<Quote>>()?.firstOrNull()
}

/**
 * Fetches the historical price data for a given stock
 *
 * @param symbol The stock ticker symbol of the financial asset to retrieve historical price data for.
 * @param from An optional parameter specifying the start date of the historical data range in YYYY-MM-DD format.
 * @param to An optional parameter specifying the end date of the historical data range in YYYY-MM-DD format.
 * @return A [HistoricalPrice] object containing the historical price data for the specified symbol, or null if the data could not be retrieved.
 */
suspend fun HttpClient.getHistoricalPrice(symbol: String, from: String? = null, to: String? = null): HistoricalPrice? {
    val response = this.get("historical-price-full/${symbol.uppercase()}") {
        url {
            with(parameters) {
                from?.let { append("from", it) }
                to?.let { append("to", it) }
                append("apikey", fmpApiKey)
            }
        }
    }.takeIf { it.status == HttpStatusCode.OK } ?: run {
        System.err.println("Failed to retrieve data for $symbol")
        null
    }

    return response?.body<HistoricalPrice>()
}
