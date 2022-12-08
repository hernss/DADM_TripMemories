package com.example.primerparcial.fragments

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.primerparcial.R
import com.example.primerparcial.adapters.MemoryAdapter
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.Memory
import com.example.primerparcial.entities.MemoryDB
import java.text.SimpleDateFormat

class MemoryUpdateFragment : Fragment() {
    lateinit var v : View
    //Vistas
    lateinit var txtUpdateMemoryCountry : EditText
    lateinit var txtUpdateMemoryCity : EditText
    lateinit var txtUpdateMemoryDescription : EditText
    lateinit var txtUpdateMemoryDate : EditText
    lateinit var btnUpdateMemoryUpdate : Button
    lateinit var imgUpdateMemoryImage : ImageView

    private val PREF_NAME = "myPreferences"
    //Argumentos
    private val args : MemoryUpdateFragmentArgs by navArgs()

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var memoryDao : memoryDao? = null
    private var memory: MemoryDB? = null

    companion object {
        fun newInstance() = MemoryUpdateFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_memory_update, container, false)
        txtUpdateMemoryCity = v.findViewById(R.id.txtUpdateMemoryCity)
        txtUpdateMemoryCountry = v.findViewById(R.id.txtUpdateMemoryCountry)
        txtUpdateMemoryDate = v.findViewById(R.id.txtUpdateMemoryDate)
        txtUpdateMemoryDescription = v.findViewById(R.id.txtUpdateMemoryDescription)
        imgUpdateMemoryImage = v.findViewById(R.id.imgUpdateMemoryImage)
        btnUpdateMemoryUpdate = v.findViewById(R.id.btnUpdateMemoryUpdate)
        return v
    }

    override fun onStart() {
        super.onStart()

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        memoryDao = db?.memoryDao()

        val memoryId = args.memoryId

        if(memoryId == 0){
            Toast.makeText(requireContext(), resources.getString(R.string.updateMemoryErrorStr), Toast.LENGTH_SHORT).show()
            return
        }

        var memory: MemoryDB? = memoryDao?.loadMemoryFromId(memoryId)

        if (memory == null){
            Toast.makeText(requireContext(), resources.getString(R.string.updateMemoryErrorStr), Toast.LENGTH_SHORT).show()
            return
        }

        txtUpdateMemoryDescription.setText(memory.description)
        txtUpdateMemoryCity.setText(memory.city)
        txtUpdateMemoryCountry.setText(memory.country)
        val formater = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val strDate = formater.format(memory.date)
        txtUpdateMemoryDate.setText(strDate)

        //Me fijo si tengo permisos de lectura del almacenamiento interno
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED ) {

            //Si no tengo, los pido
            Log.d("M_DETAIL","No read permission")
        }else{
            Glide.with(v)
                .load(memory.urlImage)
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgUpdateMemoryImage)
        }

        btnUpdateMemoryUpdate.setOnClickListener {
            memory.city = txtUpdateMemoryCity.text.toString()
            memory.description  = txtUpdateMemoryDescription.text.toString()
            memory.country = txtUpdateMemoryCountry.text.toString()
            memoryDao?.updateMemory(memory)
            v.findNavController().popBackStack()
        }

    }

}