package com.example.primerparcial.fragments

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.primerparcial.R
import com.example.primerparcial.adapters.MemoryAdapter
import com.example.primerparcial.database.appDatabase
import com.example.primerparcial.database.memoryDao
import com.example.primerparcial.database.userDao
import com.example.primerparcial.entities.*
import java.util.*

class MainViewFragment : Fragment() {
    private lateinit var v :View
    private val PREF_NAME = "myPreferences"

    private var memoriesList : MutableList<Memory> = mutableListOf()
    private lateinit var adapter: MemoryAdapter
    lateinit var memoriesRecyclerView : RecyclerView

    //Variable de la base de datos
    private var db : appDatabase? = null
    private var userDao : userDao? = null
    private var memoryDao : memoryDao? = null

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                loadMemoriesAfterPermission()
            } else {
                Log.d("MAIN", "Permission Denied")
                val alertDialogBuilder = AlertDialog.Builder(this.requireContext())
                alertDialogBuilder.setMessage(resources.getString(R.string.noPermissionGrantedStr))
                alertDialogBuilder.setPositiveButton("Ok",DialogInterface.OnClickListener { dialogInterface, i ->
                    finishAffinity(this.requireActivity())
                } )
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }


    companion object {
        fun newInstance() = MainViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main_view, container, false)

        //Asignacion de las vistas
        memoriesRecyclerView = v.findViewById(R.id.memoriesRecycler)

        //Linkeo con la base de datos
        db = appDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()
        memoryDao = db?.memoryDao()

        //Me fijo si tengo permisos de lectura del almacenamiento interno
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED ) {
            //Si no tengo, los pido
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }else{
            loadMemoriesAfterPermission()
        }

        return v
    }

    override fun onStart() {
        super.onStart()

        adapter = MemoryAdapter(memoriesList){
            val action = MainViewFragmentDirections.actionMainViewFragmentToMemoryDetailFragment(memoriesList[it])
            v.findNavController().navigate(action)

        }
        memoriesRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        memoriesRecyclerView.adapter = adapter
    }

    private fun loadMemoriesAfterPermission(){
        //Recupero el usuario desde las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val user_id : Int = sharedPref.getInt("user_id", 0)
        Log.d("MAIN", "Usuario: $user_id")

        memoriesList.clear()

        //Consulto las memories del usuario
        memoryDao?.loadAllMemoriesFromUser(user_id)?.forEach {
            memoriesList.add(Memory(it!!))
        }
    }

    private fun loadDummyMemories(user_id : Int){
        Log.d("MAIN", "Lista de recuerdos vacia")
        memoryDao?.insertMemory(
            MemoryDB(
                0,
                user_id,
                "Francia",
                "Paris",
                "Ultimo viaje con la familia completa",
                Date(2015,12,21),
                "https://parisando.b-cdn.net/wp-content/uploads/sites/11/2022/03/la-torre-eiffel-de-paris.jpg",
                0.0,
                0.0
            )
        )
        memoryDao?.insertMemory(
            MemoryDB(
                0,
                user_id,
                "Myanmar",
                "Bagan",
                "Vacaciones con los abuelos",
                Date(2012,1,30),
                "https://www.travelandleisure.com/thmb/pYt8_bhj-4Xgw-P3vgYAgGQQEys=/650x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/bagan-myanmar-MOSTBEAUTIFUL0921-7de032dd5a894dbc970896d65fc0283b.jpg",
                0.0,
                0.0
            )
        )
        memoryDao?.insertMemory(
            MemoryDB(
                0,
                user_id,
                "Estados Unidos",
                "Arizona",
                "De visita por el gran cañon",
                Date(2020,4,12),
                "https://www.travelandleisure.com/thmb/Dg4Du0m30xeAKRKrSd_LWNkCmKo=/1800x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grand-canyon-arizona-MOSTBEAUTIFUL0921-60bb5a40dbfe4e4b8c28175074cd07dc.jpg",
                0.0,
                0.0
            )
        )
    }
}