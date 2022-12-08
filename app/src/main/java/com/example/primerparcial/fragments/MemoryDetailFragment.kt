package com.example.primerparcial.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.primerparcial.R
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.entities.MemoryDB
import java.text.SimpleDateFormat
import java.util.*

class MemoryDetailFragment : Fragment() {
    lateinit var v : View
    private val args : MemoryDetailFragmentArgs by navArgs()

    //Vistas
    lateinit var txtCountryDetail : TextView
    lateinit var txtCityDetail : TextView
    lateinit var txtDateDetail : TextView
    lateinit var txtDescriptionDetail : TextView
    lateinit var imgMemoryDetailImage : ImageView
    lateinit var btnMemoryDetailDelete : Button
    lateinit var btnMemoryDetailUpdate : Button

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var memoryDao : memoryDao? = null

    companion object {
        fun newInstance() = MemoryDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_memory_detail, container, false)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        memoryDao = db?.memoryDao()

        //Busco los id de las vistas
        txtCountryDetail = v.findViewById(R.id.txtCountryDetail)
        txtCityDetail = v.findViewById(R.id.txtCityDetail)
        txtDateDetail = v.findViewById(R.id.txtDateDetail)
        txtDescriptionDetail = v.findViewById(R.id.txtDescriptionDetail)
        imgMemoryDetailImage = v.findViewById(R.id.imgMemoryImageDetail)
        btnMemoryDetailDelete = v.findViewById(R.id.btnMemoryDetailDelete)
        btnMemoryDetailUpdate = v.findViewById(R.id.btnMemoryDetailUpdate)



        return v
    }

    override fun onStart() {
        super.onStart()

        val memory = memoryDao?.loadMemoryFromId(args.selectedMemory.id)
        //Cargo la informacion que me vino como argumento
        txtCountryDetail.text = memory?.country
        txtCityDetail.text = memory?.city
        val formater = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val strDate = formater.format(memory?.date)
        txtDateDetail.text = strDate
        txtDescriptionDetail.text = memory?.description

        //Me fijo si tengo permisos de lectura del almacenamiento interno
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED ) {

            //Si no tengo, los pido
            Log.d("M_DETAIL","No read permission")
        }else{
            Glide.with(v)
                .load(memory?.urlImage)
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgMemoryDetailImage)
        }

        btnMemoryDetailDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this.requireContext())
            alertDialogBuilder.setMessage(resources.getString(R.string.deleteMemoryAlertBoxStr))
            alertDialogBuilder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { _, _ ->
                    val memoryDB : MemoryDB? = memoryDao?.loadMemoryFromId(args.selectedMemory.id)
                    memoryDao?.delete(memoryDB)
                    v.findNavController().popBackStack()
                } )
            alertDialogBuilder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { _, _ ->
                    //No hacemos nada
                } )
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

        btnMemoryDetailUpdate.setOnClickListener {
            val action = MemoryDetailFragmentDirections.actionMemoryDetailFragmentToMemoryUpdateFragment(args.selectedMemory.id)
            v.findNavController().navigate(action)
        }
    }
}