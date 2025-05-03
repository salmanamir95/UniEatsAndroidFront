package com.example.unieats.frontend.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.frontend.register.RegisterFragment

class loginFragment: Fragment() {
    private lateinit var email : String
    private lateinit var pass : String
    private lateinit var logBtn : Button
    private lateinit var regBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logBtn = view.findViewById(R.id.loginBtn)
        regBtn = view.findViewById(R.id.regBtn)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = ".{6,}"

        logBtn?.let { button ->
            button.setOnClickListener {
                email = view.findViewById<EditText>(R.id.email).text.toString()
                pass = view.findViewById<EditText>(R.id.pass).text.toString()

                if (email.isNullOrEmpty() || pass.isNullOrEmpty())
                    Toast.makeText(context, "Complete the Required Fields", Toast.LENGTH_SHORT).show()
                else {
                    if (email.matches(emailPattern.toRegex())) {
                        if (pass.matches((passwordPattern.toRegex()))) {
                            Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "Password Pattern is wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, "Email Pattern is wrong",Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }

        regBtn?.let{
            btn -> btn.setOnClickListener(){
            val trans = parentFragmentManager.beginTransaction()
            trans.replace(R.id.fragment_container, RegisterFragment()) // Make sure container ID matches
            trans.commit()
            }
        }
    }
}