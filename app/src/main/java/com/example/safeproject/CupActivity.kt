package com.example.safeproject

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safeproject.databinding.ActivityCupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CupActivity : AppCompatActivity() {

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult

            val file = File(absolutelyPath(imageUri, this))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("cup", file.name, requestFile)

            Log.d("testt", file.name)

            sendCupImage(body)

            binding.imageGallery.setImageURI(imageUri)

        }
    }

    companion object {
        const val REQ_GALLERY = 1
    }

    private lateinit var binding: ActivityCupBinding
    private val retrofit = RetrofitInstance.getInstance().create(SafeApi::class.java)
    private lateinit var mCallTodoList: Call<String>
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var database: FirebaseDatabase
    private var cupCount = 0
    private var scoreCount = 0.0
    private var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            btnSelectPhoto.setOnClickListener {
                selectGallery()
            }
        }
        mProgressDialog = ProgressDialog(this) // Dialog 생성
    }

    private fun selectGallery() {
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED ||
            readPermission == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQ_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )

            imageResult.launch(intent)
        }


    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri?, context: Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }


    private fun sendCupImage(body: MultipartBody.Part) {
        retrofit.sendCupImage(body).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CupActivity, "이미지 전송 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CupActivity, "이미지 전송 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println(t.message.toString())
            }

        })
        mProgressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            mProgressDialog.dismiss()//Do something

            println("로딩")
            callText()
        }, 35000) //1초 후 실행
    }

    private val mRetrofitCallback = (object : Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            database = FirebaseDatabase.getInstance()
            val uid = Firebase.auth.currentUser?.uid.toString()
            var random_id = ""
            val result = response.body().toString().trim().replace("^\"|\"$".toRegex(), "")
            if (result == "none"){
                val toast = Toast.makeText(this@CupActivity, "!!다회용컵 인식 실패!!", Toast.LENGTH_LONG)
                toast.show()
            }
            else{
                check = 1
                database.getReference("users").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            val item = data.getValue(Friend::class.java)
                            // UID 값을 변수에 저장
                            if (item?.uid.equals(uid)) {
                                random_id = data.key.toString()
                                if (item != null) {
                                    cupCount = check + item.cup!!
                                    scoreCount = ((check * 5) + item.score!!).toDouble()
                                    check = 0
                                    println(scoreCount)
                                }
                                item?.let {
                                    val updateStep = mapOf(
                                        "cup" to cupCount, // name 필드만 업데이트
                                        "score" to scoreCount
                                    )
                                    database.getReference("users").child(random_id).updateChildren(updateStep)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val toast = Toast.makeText(this@CupActivity, "다회용컵 사용이 인증되었습니다!", Toast.LENGTH_LONG)
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

        override fun onFailure(call: Call<String>, t: Throwable) {
            t.printStackTrace()
            Log.d("testt", "에러입니다. ${t.message}")
        }
    })
    private fun callText(){
        mCallTodoList = retrofit.getRes() // RetrofitAPI 에서 JSON 객체를 요청해서 반환하는 메소드 호출
        mCallTodoList.enqueue(mRetrofitCallback) // 응답을 큐에 넣어 대기 시켜놓음. 즉, 응답이 생기면 뱉어낸다.
    }
}