package com.example.safeproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar


class Weekly : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Firebase 데이터 수정 작업 호출
        WeeklyData.updateDatabaseValue()


    }
}