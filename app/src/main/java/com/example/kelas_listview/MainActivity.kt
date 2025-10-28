package com.example.kelas_listview

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        data.addAll(listOf("1","2","3","4","5","6"))

        val lvadapter : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,data)


        val lv1 : ListView = findViewById<ListView>(R.id.lv1)
        lv1.adapter = lvadapter

        val btnTambah = findViewById<Button>(R.id.btnTambah)
        btnTambah.setOnClickListener {
            var dtAkhir = Integer.parseInt(data.get(data.size-1)) +1
            data.add("$dtAkhir")
            lvadapter.notifyDataSetChanged()
        }
        lv1.setOnItemClickListener { parent, view, position,id ->
            Toast.makeText(this, data[position], Toast.LENGTH_SHORT).show()
        }

        val gestureDetector = GestureDetector (
            this,
            object : GestureDetector.SimpleOnGestureListener(){
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if (position!= ListView.INVALID_POSITION){
                        val selecteditem = data[position]
                        showActionDialog(position, selecteditem, data, lvadapter)
                    }
                    return true
                }
            }
        )
        lv1.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        lvadapter.notifyDataSetChanged()


    }

    private fun showActionDialog(
        position: Int,
        selectedItem  : String,
        data : MutableList<String>,
        adapter : ArrayAdapter<String>
    ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ITEM $selectedItem")
        builder.setMessage("Pilih tindakan yang ingin dilakukan")

        builder.setPositiveButton("Update") { _,_ ->
            showUpdateDialog(position,selectedItem,data,adapter)
        }
        builder.setNegativeButton("Hapus") { _, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                this,
                "Hapus teks $selectedItem",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Batal"){dialog ,_ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


    private fun showUpdateDialog(
        position: Int,
        oldvalue  : String,
        data : MutableList<String>,
        adapter : ArrayAdapter<String>
    ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Data")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50,40,50,10)

        val textviewOld = TextView(this)
        textviewOld.text = "Data Lama : $oldvalue"
        textviewOld.textSize = 16f

        val editext = EditText(this)
        editext.hint = "Masukan data baru"
        editext.setText(oldvalue)

        layout.addView(textviewOld)
        layout.addView(editext)

        builder.setView(layout)

        builder.setPositiveButton("Simpan"){ dialog, _ ->
            val newvalue = editext.text.toString().trim()
            if (newvalue.isNotEmpty()){
                data[position] = newvalue
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    this,
                    "Data Diupdate jadi $newvalue",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    this,
                    "Data baru tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        builder.setNegativeButton("Batal"){ dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}