package com.example.primerparcial.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.UserDB

class NewAccountPersonalInfoFragment : Fragment() {

    //Argumentos
    private val args : NewAccountPersonalInfoFragmentArgs by navArgs()
    private val PREF_NAME = "myPreferences"

    //Vistas
    lateinit var txtNewAccountName : EditText
    lateinit var txtNewAccountLastname : EditText
    lateinit var txtNewAccountPhone : EditText
    lateinit var btnNewAccountFinish : Button

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    companion object {
        fun newInstance() = NewAccountPersonalInfoFragment()
    }

    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_new_account_personal_info, container, false)

        //Asignacion de las vistas
        txtNewAccountName = v.findViewById(R.id.txtNewAccountName)
        txtNewAccountLastname = v.findViewById(R.id.txtNewAccountLastname)
        txtNewAccountPhone = v.findViewById(R.id.txtNewAccountPhoneNumber)
        btnNewAccountFinish = v.findViewById(R.id.btnNewAccountFinish)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        btnNewAccountFinish.setOnClickListener {
            var user: UserDB = userDao?.loadUserByUsername(args.newUser.username)!!
            user.name = txtNewAccountName.text.toString()
            user.lastname = txtNewAccountLastname.text.toString()
            user.phone = txtNewAccountPhone.text.toString()

            userDao?.updatePerson(user)

            //Guardo el user_id en las shared preference para poder levantarlos en el activity
            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("user_id", user.id)
            editor.apply()

            val action = NewAccountPersonalInfoFragmentDirections.actionNewAccountPersonalInfoFragmentToMainActivity()
            v.findNavController().navigate(action)
            activity?.finish()
        }





        return v
    }

}