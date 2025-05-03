package com.example.unieats.frontend.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.frontend.login.loginFragment

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var spinner = view.findViewById<Spinner>(R.id.designReg)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = ".{6,}"
        val options = listOf("---Select Any---", "Admin", "Student", "Staff")
        lateinit var design: String

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                design = parent?.getItemAtPosition(position).toString()
                if (design == "---Select Any---")
                    design =""
                Toast.makeText(context,"Designation assigned as: " + design, Toast.LENGTH_SHORT ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                design = ""
                Toast.makeText(context, "Designation assigned as: " + design, Toast.LENGTH_SHORT ).show()
            }

        }
        var logBtn = view.findViewById<Button>(R.id.logreg)

        logBtn?.let{
                btn -> btn.setOnClickListener(){
            val trans = parentFragmentManager.beginTransaction()
            trans.replace(R.id.fragment_container, loginFragment()) // Make sure container ID matches
            trans.commit()
        }
        }
    }
}