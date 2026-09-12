package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.R

/**
 * بخش ۱۲: ویجت هوم‌اسکرین - مواعد بحرانی
 */
class DeadlineWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_deadline_luxury)

            // در نسخه واقعی، داده‌ها از Room خوانده می‌شوند
            views.setTextViewText(R.id.widget_title, "میلانو لگال - مواعد بحرانی")
            views.setTextViewText(R.id.widget_content, "۳ موعد بحرانی در ۳ روز آینده\n• تجدیدنظرخواهی - ۳ روز\n• اعتراض کارشناسی - ۶ روز")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
