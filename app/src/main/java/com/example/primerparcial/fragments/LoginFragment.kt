package com.example.primerparcial.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.User
import com.example.primerparcial.entities.UserDB
import com.google.android.material.snackbar.Snackbar
import java.util.*

class LoginFragment : Fragment() {
    lateinit var v : View
    private val PREF_NAME = "myPreferences"

    //Vistas
    lateinit var btnLogin : Button
    lateinit var txtUsername : EditText
    lateinit var txtUserPass : EditText
    lateinit var txtNotUserYet : TextView

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    companion object {
        fun newInstance() = LoginFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        //Asignacion de objetos por id
        btnLogin = v.findViewById(R.id.btnLogin)
        txtUsername = v.findViewById(R.id.txtUserName)
        txtUserPass = v.findViewById(R.id.txtUserPass)
        txtNotUserYet = v.findViewById(R.id.txtNotUserYet)

        return v
    }

    override fun onStart() {
        super.onStart()

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        //Consulto la cantidad de usuarios registrados
        val uCount: Int = userDao?.getUserCount() as Int

        //Si no hay nadie registrado creo un usuario de prueba
        if (uCount == 0){
            var user : UserDB = UserDB(
                0,
                "hernss",
                "Hernan",
                "Travado",
                "htravado@frba.utn.edu.ar",
                "1234",
                "541122334455",
                Date(),
                Date(),
                0,
                0,
                1
            )
            userDao?.insertPerson(user)
        }

        btnLogin.setOnClickListener {
            //Hago las comprobaciones de los campos
            if(txtUsername.length() == 0){
                Snackbar.make(it, resources.getText(R.string.noUserName), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(txtUserPass.length() == 0){
                Snackbar.make(it, resources.getText(R.string.noPassword), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Chequeo si coincide usuario y contraseña con algun usuario
            if(userDao?.checkUserAndPass(txtUsername.text.toString(), txtUserPass.text.toString()) == 1){
                //Cargo el usuario desde la base de datos
                val selectedUserDB : UserDB? = userDao?.loadUserByUsername(txtUsername.text.toString())

                val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())


                //Modifico el ultimo tiempo de login
                if(prefs.getBoolean("keepLogin",true)){
                    selectedUserDB?.is_logged_in = 1
                }else{
                    selectedUserDB?.is_logged_in = 0
                }
                selectedUserDB?.last_logged_in = Date()

                //Primero deslogueo todos los usuarios
                userDao?.logoutAllUsers()

                //Actualizo el usuario
                userDao?.updatePerson(selectedUserDB)

                //Guardo el user_id en las shared preference para poder levantarlos en el activity
                val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("user_id", selectedUserDB?.id ?: 0)
                editor.apply()

                //Preparo lo action para cambiar de pantalla
                val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
                v.findNavController().navigate(action)
                activity?.finish()
            }else{
                Snackbar.make(it, resources.getText(R.string.invalidUserName), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
        txtNotUserYet.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToNewUserFragment()
            v.findNavController().navigate(action)
            txtNotUserYet.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}

