package com.example.safeproject

import RetrofitInstance
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safeproject.databinding.ActivityFoodBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class FoodActivity : AppCompatActivity() {

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult

            val file = File(absolutelyPath(imageUri, this))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("food", file.name, requestFile)

            Log.d("testt", file.name)

            sendImage(body)

            binding.imageGallery.setImageURI(imageUri)

        }
    }

    companion object {
        const val REQ_GALLERY = 1
        //const val RESULT_FILE = "res.txt"
        //"D:/yolov5/SafeProject/app/src/main/assets/res.txt"
    }

    private lateinit var binding: ActivityFoodBinding
    private val retrofit = RetrofitInstance.getInstance().create(SafeApi::class.java)
    private lateinit var mCallTodoList: retrofit2.Call<String>
    private lateinit var mProgressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
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


    private fun sendImage(body: MultipartBody.Part) {
        retrofit.sendImage(body).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FoodActivity, "이미지 전송 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FoodActivity, "이미지 전송 실패", Toast.LENGTH_SHORT).show()
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

    private val mRetrofitCallback = (object : retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            val result = response.body()
            Log.d("testt", "결과는 ${result}")

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
        /*Handler(Looper.getMainLooper()).postDelayed({
            mProgressDialog.dismiss()//Do something
            println("로딩")
            try {
                val inputStream = resources.openRawResource(R.raw.res)
                val inputStreamReader = InputStreamReader(inputStream,"UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val stringBuffer = StringBuffer()
                println("니가 왜 나와")
                stringBuffer.append(bufferedReader.readLine())

                bufferedReader.close()

                Toast.makeText(this, stringBuffer, Toast.LENGTH_SHORT).show()
            }catch (e : IOException){
                Toast.makeText(this, "파일 없음", Toast.LENGTH_SHORT).show()
            }
        }, 35000) //1초 후 실행*/
