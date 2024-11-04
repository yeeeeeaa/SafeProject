package com.example.safeproject

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.safeproject.databinding.ActivitySigninBinding
import com.example.safeproject.databinding.ActivityStepBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.math.BigDecimal
import java.math.RoundingMode

class StepActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var initialStepCount = 0 // 초기 걸음 수 저장 변수
    private var stepCount = 0
    private var steps = 0
    private var scoreCount = 0.0

    private lateinit var stepCountTextView: TextView
    private lateinit var stepCountBtn: Button

    private lateinit var binding: ActivityStepBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stepCountTextView = binding.stepCountTextView
        stepCountBtn = binding.plusBtn

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        database = FirebaseDatabase.getInstance()
        val uid = Firebase.auth.currentUser?.uid.toString()
        var random_id = ""
        stepCountBtn.setOnClickListener() {
            database.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue(Friend::class.java)
                        // UID 값을 변수에 저장
                        if (item?.uid.equals(uid)) {
                            random_id = data.key.toString()
                            if (item != null) {
                                println(stepCount)
                                steps = stepCount + item.steps!!
                                scoreCount = roundToTwoDecimalPlaces(((stepCount * 237.9) * 0.001) + item.score!!)
                                println((stepCount * 237.9) * (1/1000))
                            }
                            item?.let {
                                val updateStep = mapOf(
                                    "steps" to steps, // name 필드만 업데이트
                                    "score" to scoreCount
                                )
                                database.getReference("users").child(random_id).updateChildren(updateStep)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            resetStepCount()
                                            println(stepCount)
                                            val toast = Toast.makeText(
                                                this@StepActivity,
                                                "걸음 수가 반영되었습니다!",
                                                Toast.LENGTH_LONG
                                            )
                                            toast.show()
                                        } else {
                                            Log.e(
                                                "FirebaseError",
                                                "데이터 업데이트 실패: ${task.exception?.message}"
                                            )
                                        }
                                    }
                            }
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    // 데이터를 읽는 데 실패할 때 호출됩니다.
                }
            })
        }
    }
    fun roundToTwoDecimalPlaces(value: Double): Double {
        val bd = BigDecimal(value)
        return bd.setScale(2, RoundingMode.HALF_UP).toDouble()
    }
    override fun onResume() {
        super.onResume()
        // 센서 재등록
        val stepCounterSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount == 0) {
                initialStepCount = event.values[0].toInt() // 초기 걸음 수 저장
            }
            stepCount = event.values[0].toInt() - initialStepCount // 현재 걸음 수 계산
            stepCountTextView.text = "걸음 수: $stepCount"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 정확도 변경 처리
    }

    private fun resetStepCount() {
        stepCount = 0 // 걸음 수 초기화
        Log.d("StepCount", "걸음 수가 초기화되었습니다.")
    }
}