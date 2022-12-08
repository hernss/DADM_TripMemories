package com.example.primerparcial.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.User
import com.example.primerparcial.entities.UserDB
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class NewUserFragment : Fragment() {

    //Vistas
    lateinit var v : View
    lateinit var txtNewAccountUsername : EditText
    lateinit var txtNewAccountPassword : EditText
    lateinit var btnNewAccountNext : Button

    //Variable de la base de datos
    private val PREF_NAME = "myPreferences"
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    companion object {
        fun newInstance() = NewUserFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_new_user, container, false)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        //Asigno las vistas
        txtNewAccountUsername = v.findViewById(R.id.txtNewAccountUsername)
        txtNewAccountPassword = v.findViewById(R.id.txtNewAccountPassword)
        btnNewAccountNext = v.findViewById(R.id.btnNewAccountNext)

        //Click listener
        btnNewAccountNext.setOnClickListener {
            val username : String = txtNewAccountUsername.text.toString()
            val pass : String = txtNewAccountPassword.text.toString()

            //Chequeo que el usuario este disponible
            val exist = userDao?.checkUsernameAvailable(username)!!
            if(exist > 0){
                Toast.makeText(requireContext(), resources.getString(R.string.usernameTakenText), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Chequeo que el password cumpla con el estandar
            if(!isValidPassword(pass)){
                Toast.makeText(requireContext(), resources.getString(R.string.invalidPasswordStr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Inserto el usuario en la DB
            var user_db : UserDB = UserDB(0,username,"","","",pass,"", Date(),Date(),0,0,0)
            userDao?.insertPerson(user_db)

            user_db = userDao?.loadUserByUsername(username)!!

            //Convierto el user a parcelable y se lo envio por parametro al proximo fragment
            val user_parameter : User = User(user_db)
            val action = NewUserFragmentDirections.actionNewUserFragmentToNewAccountCheckEmailFragment(user_parameter)
            v.findNavController().navigate(action)
        }

        txtNewAccountPassword.doOnTextChanged { text, start, before, count ->
            val pass : String = txtNewAccountPassword.text.toString()
            if(isValidPassword(pass)){
                txtNewAccountPassword.setError(null)
            }else{
                txtNewAccountPassword.setError(resources.getString(R.string.invalidPasswordStr))
            }
        }

        txtNewAccountUsername.doOnTextChanged { text, start, before, count ->
            val username = txtNewAccountUsername.text.toString()

            //Si el textbox esta vacio limpio por las dudas y salgo
            if(username == ""){
                txtNewAccountUsername.setError(null)
                return@doOnTextChanged
            }

            //Me fijo si el username esta disponible
            val exist = userDao?.checkUsernameAvailable(username)!!
            if(exist > 0){
                txtNewAccountUsername.setError(resources.getString(R.string.usernameTakenText))
            }else{
                txtNewAccountUsername.setError(null)
            }
        }

        return v
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}