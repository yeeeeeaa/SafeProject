package com.example.safeproject

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safeproject.databinding.ActivitySigninBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database




class SigninActivity : AppCompatActivity() {
    private lateinit var create: Button
    private lateinit var nickname: EditText
    private lateinit var email: EditText
    private lateinit var pw: EditText
    private lateinit var auth : FirebaseAuth

    private lateinit var binding: ActivitySigninBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        //초기화
        create = binding.createBtn
        nickname = binding.nickname
        email = binding.email
        pw= binding.pw

        create.setOnClickListener() {
            val nickname = nickname.text.toString()
            val email = email.text.toString()
            val pw = pw.text.toString()
            init(nickname, email, pw, auth)
        }
    }
    //
    private fun init(nickname: String, email: String, pw: String, auth: FirebaseAuth){
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")
        if(nickname.isNullOrEmpty() || email.isNullOrEmpty() || pw.isNullOrEmpty()) {
            Toast.makeText(this, "이메일 혹인 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = Friend(
                        uid, nickname, 0, 0, 0, 0.0, 0, 0, 0, 0.0
                    )
                    myRef.push().setValue(user)
                    Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "이메일 형식인지 확인 또는 비밀번호 12자리 이상 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
    }
}