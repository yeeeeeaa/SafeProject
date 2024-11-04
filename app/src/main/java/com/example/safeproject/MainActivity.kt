package com.example.safeproject

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safeproject.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWeeklyAlarm()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.HomeFragment -> {
                    setFrag(0)
                }
                R.id.FriendsFragment -> {
                    setFrag(1)
                }
                R.id.CheckFragment -> {
                    setFrag(2)
                }
                R.id.InfoFragment -> {
                    setFrag(3)
                }
                else -> {
                    false
                }
            }
        }
        setFrag(0)
    }private fun setFrag(fragNum : Int) : Boolean {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum){
            0 -> {
                ft.replace(R.id.main_frame, HomeFragment()).commit()
            }
            1 -> {
                ft.replace(R.id.main_frame, FriendsFragment()).commit()
            }
            2 -> {
                ft.replace(R.id.main_frame, CheckFragment()).commit()
            }
            3 -> {
                ft.replace(R.id.main_frame, InfoFragment()).commit()
            }
        }
        return true
    }
    private fun setWeeklyAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, Weekly::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 17) // 정각 설정
            set(Calendar.MINUTE, 33)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // 현재 시간이 월요일 정각 이전이라면 그대로 사용
            // 현재 시간이 월요일 정각 이후라면 다음 월요일로 설정
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        // AlarmClock을 설정합니다
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, null),
            pendingIntent
        )
    }
}