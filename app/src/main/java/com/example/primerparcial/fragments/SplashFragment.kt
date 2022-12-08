package com.example.primerparcial.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.UserDB

class SplashFragment : Fragment() {
    lateinit var v : View
    private val PREF_NAME = "myPreferences"

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_splash, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()
        //Acceso a las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        //Me fijo si me tengo que saltar el splash, por si vengo de un logout
        val skipSplash = sharedPref.getBoolean("skipSplash",false)

        if(skipSplash){
            //Si me lo  tengo que saltar, bajo el flag para que la proxima no me lo salte
            editor.putBoolean("skipSplash",false)
            editor.apply()
            val skipAction = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            v.findNavController().navigate(skipAction)
            return
        }

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        //Me fijo si hay algun usuario logueado.
        var user_db : UserDB? = userDao?.getLogguedUser()

        val user_id = user_db?.id


        //Creo el temporizador del splash
        val timer = object: CountDownTimer(3000, 3000) {
            override fun onTick(p0: Long) {
                return
            }

            override fun onFinish() {
                if(user_id != null) {
                    //Tengo un usuario logueado, asi que me salto la pantalla de login
                    //Guardo el user_id en las shared preference para poder levantarlos en el activity

                    editor.putInt("user_id", user_id)
                    editor.apply()

                    //Preparo lo action para cambiar de pantalla
                    val action = SplashFragmentDirections.actionSplashFragmentToMainActivity()
                    v.findNavController().navigate(action)
                    activity?.finish()
                }else {
                    val actionLogin = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    //val action = SplashFragmentDirections.actionSplashFragmentToNewUserFragment()
                    v.findNavController().navigate(actionLogin)
                }
            }
        }
        timer.start()
    }

}