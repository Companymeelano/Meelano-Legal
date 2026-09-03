package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.LegalCase
import com.example.ui.components.LegalCaseCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleCase = LegalCase(
      caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
      archiveClassNumber = "۰۳۰۰۲۴۵",
      courtBranch = "شعبه ۱۲ دادگاه تجدیدنظر استان تهران",
      caseTitle = "مطالبه وجه چک صیادی و خسارت تاخیر تادیه",
      clientName = "شرکت مهندسی میلانو",
      clientRole = "تجدیدنظرخواه",
      oppositeParty = "شرکت سازه پایدار",
      caseStatus = "در حال تجدیدنظر",
      priority = "فوری",
      summary = "دادخواست تجدیدنظرخواهی از دادنامه بدوی مبنی بر رد دعوی خسارت تاخیر",
      defenseStrategy = "استناد به استفساریه تبصره الحاقی به ماده ۲ قانون صدور چک"
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        LegalCaseCard(
          legalCase = sampleCase,
          onClick = {},
          onDelete = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

