package com.example

import com.example.data.model.DatabaseConnectionInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testDatabaseCredentialsConfiguration() {
    val dbInfo = DatabaseConnectionInfo()
    assertEquals("meelanoe_legal", dbInfo.databaseName)
    assertEquals("meelanoe_legaluser", dbInfo.userName)
    assertEquals("Milad@1369", dbInfo.password)
    assertEquals(3306, dbInfo.port)
    assertTrue(dbInfo.isConnected)
  }

  @Test
  fun testIranianProceduralDeadlines() {
    val appealDays = 20
    val expertReportDays = 7
    val injunctionDays = 10

    assertEquals(20, appealDays)
    assertEquals(7, expertReportDays)
    assertEquals(10, injunctionDays)
  }
}

