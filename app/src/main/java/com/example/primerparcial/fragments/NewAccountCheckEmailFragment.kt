package com.example.primerparcial.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.User
import com.example.primerparcial.entities.UserDB
import java.util.*

class NewAccountCheckEmailFragment : Fragment() {

    //Vistas
    lateinit var v : View
    lateinit var txtNewAccountEmail : EditText
    lateinit var txtNewAccountEmailCode : EditText
    lateinit var btnNewAccountGetCode : Button
    lateinit var btnNewAccountValidateCode : Button
    lateinit var btnNewAccountNext : Button

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    private val args : NewAccountCheckEmailFragmentArgs by navArgs()

    companion object {
        fun newInstance() = NewAccountCheckEmailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_new_account_check_email, container, false)

        //Asignacion de vistas
        txtNewAccountEmail = v.findViewById(R.id.txtNewAcountEmail)
        txtNewAccountEmailCode = v.findViewById(R.id.txtNewAccountVerifiyCode)
        btnNewAccountGetCode = v.findViewById(R.id.btnNewAccountGetCode)
        btnNewAccountValidateCode = v.findViewById(R.id.btnNewAccountVerify)
        btnNewAccountNext = v.findViewById(R.id.btnNewAccountEmailNext)

        btnNewAccountNext.visibility = View.INVISIBLE

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        btnNewAccountGetCode.setOnClickListener {
            val email : String = txtNewAccountEmail.text.toString()

            if(isEmailValid(email)){
                //Genero un codigo random
                var email_code : Int = (100000..999999).shuffled().last()

                //Actualizo la DB con el email y el codigo
                var user_db : UserDB = userDao?.loadUserByUsername(args.newUser.username)!!
                user_db.email_code = email_code
                user_db.email = email
                userDao?.updatePerson(user_db)

                //""Mando"" el codigo por email
                txtNewAccountEmailCode.setText(email_code.toString())
            }else{
                Toast.makeText(requireContext(), resources.getString(R.string.invalidEmailStr), Toast.LENGTH_SHORT).show()
            }
        }

        btnNewAccountValidateCode.setOnClickListener {
            var user_db : UserDB = userDao?.loadUserByUsername(args.newUser.username)!!

            val email_code_entry : String = txtNewAccountEmailCode.text.toString()

            if(email_code_entry == user_db.email_code.toString()){
                //Deslogueo a todos los usuarios
                userDao?.logoutAllUsers()

                //Activo la cuenta y dejo al usuario logueado
                user_db.activated = 1
                user_db.account_created_at = Date()
                user_db.last_logged_in = Date()
                user_db.is_logged_in = 1
                userDao?.updatePerson(user_db)

                btnNewAccountNext.visibility = View.VISIBLE
                Toast.makeText(requireContext(), resources.getString(R.string.accountActivatedStr), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), resources.getString(R.string.invalidEmailCodeStr), Toast.LENGTH_SHORT).show()
            }
        }

        btnNewAccountNext.setOnClickListener {
            //Cheqeo por las dudas que la cuenta este activada
            val user_db = userDao?.loadUserByUsername(args.newUser.username)!!
            if(user_db.activated == 1){
                var user : User = User(user_db)
                val action = NewAccountCheckEmailFragmentDirections.actionNewAccountCheckEmailFragmentToNewAccountPersonalInfoFragment(user)
                v.findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(), resources.getString(R.string.accountNotActivated), Toast.LENGTH_SHORT).show()
            }
        }
        return v
    }


    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}