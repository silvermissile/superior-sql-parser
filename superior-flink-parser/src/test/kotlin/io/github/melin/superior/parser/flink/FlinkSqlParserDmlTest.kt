package io.github.melin.superior.parser.flink

import io.github.melin.superior.common.relational.create.CreateTable
import io.github.melin.superior.common.relational.dml.QueryStmt
import org.junit.Assert
import org.junit.Test

/**
 *
 * Created by libinsong on 2018/1/10.
 */
class FlinkSqlParserDmlTest {

    @Test
    fun selectSqlTest() {
        val sql = """
            WITH orders_with_total AS (
                SELECT order_id, price + tax AS total
                FROM Orders
            )
            SELECT order_id, SUM(total)
            FROM orders_with_total
            GROUP BY order_id
            limit 10;
            
            SELECT order_id, price FROM (VALUES (1, 2.0), (2, 3.1))  AS t (order_id, price);
            
            SELECT * FROM TABLE(TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES));
            
            SELECT * FROM TABLE(
               TUMBLE(
                 DATA => TABLE Bid,
                 TIMECOL => DESCRIPTOR(bidtime),
                 SIZE => INTERVAL '10' MINUTES));
                 
            SELECT * FROM TABLE(
                HOP(
                  DATA => TABLE Bid,
                  TIMECOL => DESCRIPTOR(bidtime),
                  SLIDE => INTERVAL '5' MINUTES,
                  SIZE => INTERVAL '10' MINUTES));
                  
            SELECT * FROM TABLE(
            CUMULATE(
              DATA => TABLE Bid,
              TIMECOL => DESCRIPTOR(bidtime),
              STEP => INTERVAL '2' MINUTES,
              SIZE => INTERVAL '10' MINUTES));
              
            SELECT window_start, window_end, SUM(price)
              FROM TABLE(
                TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES, INTERVAL '1' MINUTES))
              GROUP BY window_start, window_end;  
              
            SELECT *
            FROM (
              SELECT *,
                ROW_NUMBER() OVER (PARTITION BY category ORDER BY sales DESC) AS row_num
              FROM ShopSales)
            WHERE row_num <= 5;
            
            SELECT *
            FROM Ticker
                MATCH_RECOGNIZE (
                    PARTITION BY symbol
                    ORDER BY rowtime
                    MEASURES
                        START_ROW.rowtime AS start_tstamp,
                        LAST(PRICE_DOWN.rowtime) AS bottom_tstamp,
                        LAST(PRICE_UP.rowtime) AS end_tstamp
                    ONE ROW PER MATCH
                    AFTER MATCH SKIP TO LAST PRICE_UP
                    PATTERN (START_ROW PRICE_DOWN+ PRICE_UP)
                    DEFINE
                        PRICE_DOWN AS
                            (LAST(PRICE_DOWN.price, 1) IS NULL AND PRICE_DOWN.price < START_ROW.price) OR
                                PRICE_DOWN.price < LAST(PRICE_DOWN.price, 1),
                        PRICE_UP AS
                            PRICE_UP.price > LAST(PRICE_DOWN.price, 1)
                ) MR;
        """.trimIndent()

        val statements = FlinkSqlHelper.parseMultiStatement(sql)
        var queryStmt = statements.get(0)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals("Orders", queryStmt.inputTables.get(0).tableName)
            Assert.assertEquals(10, queryStmt.limit)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(1)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(0, queryStmt.inputTables.size)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(2)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Bid", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(3)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Bid", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(4)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Bid", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(5)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Bid", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(6)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Bid", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(7)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("ShopSales", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }

        queryStmt= statements.get(8)
        if (queryStmt is QueryStmt) {
            Assert.assertEquals(1, queryStmt.inputTables.size)
            Assert.assertEquals("Ticker", queryStmt.inputTables.get(0).tableName)
        } else {
            Assert.fail()
        }
    }
}
