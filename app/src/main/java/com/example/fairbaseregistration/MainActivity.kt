package com.example.fairbaseregistration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        auth = FirebaseAuth.getInstance()
        //db = FirebaseDatabase.getInstance().getReference("UserInfo")

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "ავტორიზაცია უკვე გავლილია", Toast.LENGTH_LONG).show()
        }

        setContentView(R.layout.activity_main)

        textView.text = auth.currentUser?.email

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        updatePasswordBtn.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        init()

        saveBtn.setOnClickListener {

            val n: String = inputFullName.text.toString()
            val p: String = inputPhone.text.toString()

            if (TextUtils.isEmpty(n)) {
                Toast.makeText(this, "Empty data!", Toast.LENGTH_LONG).show()
            } else if  (TextUtils.isEmpty(p)) {Toast.makeText(this, "Empty data!", Toast.LENGTH_LONG).show()
            } else {
                contactInfo(n, p)
            }

        }


    }


    private fun init() {

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")



        addUserInfoChangeListener()
    }
    private fun contactInfo(name: String, phone: String?) {
        val userInfo = UserInfo(name, phone)
        db.child(auth.currentUser?.uid!!).setValue(userInfo)
    }
    private fun addUserInfoChangeListener() {

        db.child(auth.currentUser?.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(snap: DataSnapshot) {

                    val userInfo: UserInfo = snap.getValue(UserInfo::class.java) ?: return

                    showFullName.text = userInfo.name
                    showPhone.text = userInfo.age ?: ""

                    inputFullName.setText("")
                    inputPhone.setText("")
                }
            })
    }



}
