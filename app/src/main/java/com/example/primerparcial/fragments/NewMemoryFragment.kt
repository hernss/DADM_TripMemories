package com.example.primerparcial.fragments

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.primerparcial.MainActivity
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.MemoryDB
import java.text.SimpleDateFormat
import java.util.*

class NewMemoryFragment : Fragment() {

    private val PREF_NAME = "myPreferences"

    //Vistas
    lateinit var v : View
    lateinit var imgNewMemoryImage : ImageView
    lateinit var btnNewMemoryFromFile : Button
    lateinit var btnNewMemoryFromCamera : Button
    lateinit var btnNewMemoryAdd : Button
    lateinit var txtNewMemoryCountry : EditText
    lateinit var txtNewMemoryCity : EditText
    lateinit var txtNewMemoryDescription : EditText
    lateinit var txtNewMemoryDate : EditText

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var memoryDao : memoryDao? = null
    private lateinit var newMemoryDB : MemoryDB

    companion object {
        fun newInstance() = NewMemoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_new_memory, container, false)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        memoryDao = db?.memoryDao()

        //Asigno las vistas
        btnNewMemoryFromCamera = v.findViewById(R.id.btnNewMemoryFromCamera)
        btnNewMemoryFromFile = v.findViewById(R.id.btnNewMemoryFromFile)
        btnNewMemoryAdd = v.findViewById(R.id.btnNewMemoryAdd)
        imgNewMemoryImage = v.findViewById(R.id.imgNewMemoryImage)
        txtNewMemoryDate = v.findViewById(R.id.txtNewMemoryDate)
        txtNewMemoryDescription = v.findViewById(R.id.txtNewMemoryDescription)
        txtNewMemoryCity = v.findViewById(R.id.txtNewMemoryCity)
        txtNewMemoryCountry = v.findViewById(R.id.txtNewMemoryCountry)

        //OnClick para el agregar desde archivo
        btnNewMemoryFromFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            pickImageResult.launch(intent)
        }

        btnNewMemoryFromCamera.setOnClickListener {

        }
        //Recupero el usuario desde las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val owner_id : Int = sharedPref.getInt("user_id", 0)

        limpiarForm(owner_id)

        //OnClick del agregar memory
        btnNewMemoryAdd.setOnClickListener {
            newMemoryDB.country = txtNewMemoryCountry.text.toString()
            newMemoryDB.city = txtNewMemoryCity.text.toString()
            //newMemoryDB.date = Date() //Date(txtNewMemoryDate.text.toString())
            newMemoryDB.description = txtNewMemoryDescription.text.toString()

            if(newMemoryDB.urlImage == ""){
                Toast.makeText(requireContext(), resources.getString(R.string.notValidImageStr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(newMemoryDB.country == ""){
                Toast.makeText(requireContext(), resources.getString(R.string.emptyCountryStr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(newMemoryDB.city == ""){
                Toast.makeText(requireContext(), resources.getString(R.string.emptyCityStr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(txtNewMemoryDate.text.toString() == ""){
                Toast.makeText(requireContext(), resources.getString(R.string.emptyDateStr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            memoryDao?.insertMemory(newMemoryDB)
            limpiarForm(owner_id)
            val action = NewMemoryFragmentDirections.actionNewMemoryFragment2ToMainViewFragment()
            v.findNavController().navigate(action)
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        //Recupero el usuario desde las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val owner_id : Int = sharedPref.getInt("user_id", 0)

        limpiarForm(owner_id)
    }

    //Handler del result luego de seleccionar la imagen
    private val pickImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val uri : Uri? = result.data?.data
            cargarImageFromUri(uri)
        }
    }

    private fun cargarImageFromUri(uri : Uri?){
        if (uri != null) {
            context?.contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        imgNewMemoryImage.setImageURI(uri)
        newMemoryDB.urlImage = uri.toString()
    }

    private fun limpiarForm(owner_id : Int){
        //Limpio la variable privada de la memory
        newMemoryDB = MemoryDB(0,owner_id,"","","", Date(),"",0.0, 0.0)

        //Limpio el formulario
        imgNewMemoryImage.setImageURI(null)
        txtNewMemoryCity.setText("")
        txtNewMemoryCountry.setText("")

        newMemoryDB.date = Date()
        val formater = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val strDate = formater.format(newMemoryDB.date)

        txtNewMemoryDate.setText(strDate)
        txtNewMemoryDescription.setText("")
    }
}