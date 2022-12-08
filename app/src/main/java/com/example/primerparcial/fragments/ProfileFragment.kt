package com.example.primerparcial.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.User
import com.example.primerparcial.entities.UserDB
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment() {
    lateinit var v : View
    private val PREF_NAME = "myPreferences"

    //Vistas
    lateinit var txtName : TextView
    lateinit var txtLastname : TextView
    lateinit var txtUsername : TextView
    lateinit var txtEmail : TextView
    lateinit var txtPhoneNumber : TextView
    lateinit var txtPassword : TextView
    lateinit var btnUpdate : Button
    lateinit var btnProfileLogout : Button
    lateinit var btnGoToSettings : FloatingActionButton

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_profile, container, false)

        //Asigno las vistas
        txtName = v.findViewById(R.id.txtProfileName)
        txtEmail = v.findViewById(R.id.txtProfileEmail)
        txtLastname = v.findViewById(R.id.txtProfileLastname)
        txtPassword = v.findViewById(R.id.txtProfilePassword)
        txtUsername = v.findViewById(R.id.txtProfileUsername)
        txtPhoneNumber = v.findViewById(R.id.txtProfilePhoneNumber)
        btnUpdate = v.findViewById(R.id.btnProfileUpdate)
        btnProfileLogout = v.findViewById(R.id.btnProfileLogout)
        btnGoToSettings = v.findViewById(R.id.btnGoToSettings)

        return v
    }

    override fun onStart() {

        super.onStart()
        //Recupero el usuario desde las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val user_id : Int = sharedPref.getInt("user_id", 0)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()
        var user : UserDB = userDao?.loadUserById(user_id)!!

        txtName.text = user.name.toString()
        txtLastname.text = user.lastname.toString()
        txtEmail.text = user.email.toString()
        txtPassword.text = user.pass.toString()
        txtUsername.text = user.username.toString()
        txtPhoneNumber.text = user.phone.toString()
        txtUsername.text = user.username.toString()

        btnUpdate.setOnClickListener {

            //Me fijo si cambie el nombre de usuario
            if(txtUsername.text.toString() != user.username){
                //Si lo cambie me fijo si esta disponible
                val usernameUsed = userDao?.checkUsernameAvailable(txtUsername.text.toString())!!
                if(usernameUsed > 0){
                    //El usuario ya existe..
                    Toast.makeText(requireContext(), resources.getText(R.string.usernameTakenText), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            user.name = txtName.text.toString()
            user.lastname = txtLastname.text.toString()
            user.email = txtEmail.text.toString()
            user.pass = txtPassword.text.toString()
            user.username = txtUsername.text.toString()
            user.phone = txtPhoneNumber.text.toString()
            user.username = txtUsername.text.toString()

            userDao?.updatePerson(user)
        }

        btnProfileLogout.setOnClickListener {
            //Activo el flag para saltarme el splash
            editor.putBoolean("skipSplash",true)
            editor.apply()
            //Deslogueo todos los usuarios
            userDao?.logoutAllUsers()

            //Salto al activity del login y skipeo el splash
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginActivity()
            v.findNavController().navigate(action)

            //Mato esta activity
            activity?.finish()
        }

        btnGoToSettings.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            v.findNavController().navigate(action)
        }
    }

}