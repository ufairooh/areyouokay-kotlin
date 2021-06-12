package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.Bobot
import com.example.areyouokay.Model.postDeteksiModel
import com.example.areyouokay.Model.postPertanyaanJawabanModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class DeteksiActivity : AppCompatActivity() {

    private lateinit var frame_soal_1: FrameLayout
    private lateinit var frame_soal_2: FrameLayout
    private lateinit var frame_soal_3: FrameLayout
    private lateinit var frame_soal_4: FrameLayout
    private lateinit var frame_soal_5: FrameLayout
    private lateinit var frame_soal_6: FrameLayout
    private lateinit var frame_soal_7: FrameLayout
    private lateinit var frame_soal_8: FrameLayout
    private lateinit var frame_soal_9: FrameLayout
    private lateinit var frame_soal_10: FrameLayout
    private lateinit var frame_soal_11: FrameLayout
    private lateinit var frame_soal_12: FrameLayout
    private lateinit var frame_soal_13: FrameLayout
    private lateinit var frame_soal_14: FrameLayout
    private lateinit var frame_soal_15: FrameLayout
    private lateinit var frame_soal_16: FrameLayout
    private lateinit var frame_soal_17: FrameLayout
    private lateinit var frame_soal_18: FrameLayout
    private lateinit var frame_soal_19: FrameLayout
    private lateinit var frame_soal_20: FrameLayout
    private lateinit var btn_prev: Button
    private lateinit var btn_next: Button
    private lateinit var txt_indicator: TextView
    private lateinit var rg1: RadioGroup
    private lateinit var rg2: RadioGroup
    private lateinit var rg3: RadioGroup
    private lateinit var rg4: RadioGroup
    private lateinit var rg5: RadioGroup
    private lateinit var rg6: RadioGroup
    private lateinit var rg7: RadioGroup
    private lateinit var rg8: RadioGroup
    private lateinit var rg9: RadioGroup
    private lateinit var rg10: RadioGroup
    private lateinit var rg11: RadioGroup
    private lateinit var rg12: RadioGroup
    private lateinit var rg13: RadioGroup
    private lateinit var rg14: RadioGroup
    private lateinit var rg15: RadioGroup
    private lateinit var rg16: RadioGroup
    private lateinit var rg17: RadioGroup
    private lateinit var rg18: RadioGroup
    private lateinit var rg19: RadioGroup
    private lateinit var rg20: RadioGroup
    private lateinit var Selesai: Button
    private var rg = arrayOfNulls<RadioGroup>(20)
    private var cfPakar = Bobot.PAKAR
    private var jawaban = Bobot.JAWABAN
    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater
    private lateinit var btnSelesai: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)

        handlingUI()

        Selesai = findViewById(R.id.Selesai)
        Selesai.setOnClickListener{
            val builder = AlertDialog.Builder(this@DeteksiActivity)
            builder.setTitle("KONFIRMASI")
            builder.setMessage("Apakah kamu sudah menjawab semua pernyataan dengan benar?")
            builder.setPositiveButton("Iya",{ dialogInterface: DialogInterface, i: Int -> onSubmit() })
            builder.setNegativeButton("Tidak",{ dialogInterface: DialogInterface, i: Int -> })
            builder.show()
        }

        rg.indices.forEach { i ->
            val rID = resources.getIdentifier("rg" + (i + 1), "id", this.baseContext.packageName)
            rg[i] = findViewById(rID)
        }
    }

    internal fun onSubmit() {
        // cf sekuensial
        val groupTidakDepresiList = ArrayList<Double>()
        val groupDepresiRinganList = ArrayList<Double>()
        val groupDepresiSedangList = ArrayList<Double>()
        val groupDepresiBeratList = ArrayList<Double>()


        // hasil cf
        val finalCf = ArrayList<Double>(4)

        // radio button untuk deteksi depresi
        val nilaiRB = arrayOfNulls<Double>(rg.size)
        val nilai = arrayOfNulls<Int>(rg.size)

        rg.indices.forEach { i ->
            val jwbA = rg[i]?.getChildAt(0) as RadioButton
            val jwbB = rg[i]?.getChildAt(1) as RadioButton
            val jwbC = rg[i]?.getChildAt(2) as RadioButton
            val jwbD = rg[i]?.getChildAt(3) as RadioButton
            val jwbE = rg[i]?.getChildAt(4) as RadioButton

            when {
                jwbA.isChecked -> {
                    nilaiRB[i] = jawaban[0]
                    nilai[i] = 1
                }
                jwbB.isChecked -> {
                    nilaiRB[i] = jawaban[1]
                    nilai[i] = 2
                }
                jwbC.isChecked -> {
                    nilaiRB[i] = jawaban[2]
                    nilai[i] = 3
                }
                jwbD.isChecked -> {
                    nilaiRB[i] = jawaban[3]
                    nilai[i] = 4
                }
                jwbE.isChecked -> {
                    nilaiRB[i] = jawaban[4]
                    nilai[i] = 5
                }
            }

            when (i) {
                // Tidak Depresi
                0, 3, 6, 8, 11 -> {
                    val txtTotalTidakDepresi = cfPakar[i] * nilaiRB[i]!!.toDouble()
                    groupTidakDepresiList.add(txtTotalTidakDepresi)
                }
                // Depresi Ringan
                5, 15, 16, 18, 19 -> {
                    val txtTotalDepresiRingan = cfPakar[i] * nilaiRB[i]!!.toDouble()
                    groupDepresiRinganList.add(txtTotalDepresiRingan)
                }
                // Depresi Sedang
                4, 9, 12, 14, 17-> {
                    val txtTotalDepresiSedang = cfPakar[i] * nilaiRB[i]!!.toDouble()
                    groupDepresiSedangList.add(txtTotalDepresiSedang)
                }
                // Depresi Berat
                1, 2, 7, 10, 13 -> {
                    val txtTotalDepresiBerat = cfPakar[i] * nilaiRB[i]!!.toDouble()
                    groupDepresiBeratList.add(txtTotalDepresiBerat)
                }
            }
        }


        var cfCombineTidakDepresi = getCfCombine(groupTidakDepresiList)
        var cfCombineDepresiRingan = getCfCombine(groupDepresiRinganList)
        var cfCombineDepresiSedang = getCfCombine(groupDepresiSedangList)
        var cfCombineDepresiBerat = getCfCombine(groupDepresiBeratList)

        finalCf.add(cfCombineTidakDepresi)
        finalCf.add(cfCombineDepresiRingan)
        finalCf.add(cfCombineDepresiSedang)
        finalCf.add(cfCombineDepresiBerat)

        val largestValue = finalCf.max()
        val hasilDepresi = finalCf.indexOf(largestValue)
        val id = hasilDepresi + 1

        saveHasil(id, largestValue!!.toDouble())





    }

    private fun saveHasil(id_depresi: Int, getFinalValues: Double){
        val finalJawab = ArrayList<Int?>(20)

        val builder = AlertDialog.Builder(this@DeteksiActivity)
        inflater = this@DeteksiActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        val idUser = intent.getStringExtra("id_user")
        val tanggal = Timestamp(System.currentTimeMillis())

        api.createHasilDeteksi(
                "" + idUser + "",
                ""  + tanggal + "",
                "" + getFinalValues + "",
                "" + id_depresi + ""
        ).enqueue(object : Callback<postDeteksiModel> {
            override fun onResponse(call: Call<postDeteksiModel>, response: Response<postDeteksiModel>) {

                    val id_deteksi = "${response.body()?.id}"

                    for (i in 0..19){
                        //val id_pertanyaan = i + 1
                        val nilai = arrayOfNulls<Int>(rg.size)

                        rg.indices.forEach { i ->
                            val jwbA = rg[i]?.getChildAt(0) as RadioButton
                            val jwbB = rg[i]?.getChildAt(1) as RadioButton
                            val jwbC = rg[i]?.getChildAt(2) as RadioButton
                            val jwbD = rg[i]?.getChildAt(3) as RadioButton
                            val jwbE = rg[i]?.getChildAt(4) as RadioButton

                            when {
                                jwbA.isChecked -> {
                                    nilai[i] = 1
                                }
                                jwbB.isChecked -> {
                                    nilai[i] = 2
                                }
                                jwbC.isChecked -> {
                                    nilai[i] = 3
                                }
                                jwbD.isChecked -> {
                                    nilai[i] = 4
                                }
                                jwbE.isChecked -> {
                                    nilai[i] = 5
                                }
                            }
                        }

                        finalJawab.add(nilai[i])

                    }
                                        api.createPertanyaanJawaban(
                                                "" + finalJawab[0].toString() + "",
                                                "" + finalJawab[1].toString() + "",
                                                "" + finalJawab[2].toString() +"",
                                                "" + finalJawab[3].toString() +"",
                                                "" + finalJawab[4].toString() +"",
                                                "" + finalJawab[5].toString() +"",
                                                "" + finalJawab[6].toString() +"",
                                                "" + finalJawab[7].toString() +"",
                                                "" + finalJawab[8].toString() +"",
                                                "" + finalJawab[9].toString() +"",
                                                "" + finalJawab[10].toString() +"",
                                                "" + finalJawab[11].toString() +"",
                                                "" + finalJawab[12].toString() +"",
                                                "" + finalJawab[13].toString() +"",
                                                "" + finalJawab[14].toString() +"",
                                                "" + finalJawab[15].toString() +"",
                                                "" + finalJawab[16].toString() +"",
                                                "" + finalJawab[17].toString() +"",
                                                "" + finalJawab[18].toString() +"",
                                                "" + finalJawab[19].toString() +"",
                                                "" + id_deteksi +""
                                        ).enqueue(object : Callback<postPertanyaanJawabanModel>{
                            override fun onResponse(call: Call<postPertanyaanJawabanModel>, response: Response<postPertanyaanJawabanModel>) {
                                if(response.isSuccessful){
                                    //intent(idUser, getFinalValues, id_depresi, tanggal)
                                }
                            }

                            override fun onFailure(call: Call<postPertanyaanJawabanModel>, t: Throwable) {
                                api.createPertanyaanJawaban("" + finalJawab[0].toString() + "",
                                        "" + finalJawab[1].toString() + "",
                                        "" + finalJawab[2].toString() +"",
                                        "" + finalJawab[3].toString() +"",
                                        "" + finalJawab[4].toString() +"",
                                        "" + finalJawab[5].toString() +"",
                                        "" + finalJawab[6].toString() +"",
                                        "" + finalJawab[7].toString() +"",
                                        "" + finalJawab[8].toString() +"",
                                        "" + finalJawab[9].toString() +"",
                                        "" + finalJawab[10].toString() +"",
                                        "" + finalJawab[11].toString() +"",
                                        "" + finalJawab[12].toString() +"",
                                        "" + finalJawab[13].toString() +"",
                                        "" + finalJawab[14].toString() +"",
                                        "" + finalJawab[15].toString() +"",
                                        "" + finalJawab[16].toString() +"",
                                        "" + finalJawab[17].toString() +"",
                                        "" + finalJawab[18].toString() +"",
                                        "" + finalJawab[19].toString() +"",
                                        "" + id_deteksi +"").enqueue(object : Callback<postPertanyaanJawabanModel>{
                                    override fun onResponse(call: Call<postPertanyaanJawabanModel>, response: Response<postPertanyaanJawabanModel>) {
                                        if(response.isSuccessful){
                                            //intent(idUser, getFinalValues, id_depresi, tanggal)
                                        }
                                    }

                                    override fun onFailure(call: Call<postPertanyaanJawabanModel>, t: Throwable) {
                                        Toast.makeText(this@DeteksiActivity,t.toString(), Toast.LENGTH_LONG).show()
                                    }

                                })
                            }

                        })
//                val builders = AlertDialog.Builder(this@DeteksiActivity)
//                    builders.setMessage(finalJawab[0].toString())
//                            .setPositiveButton("jawab1", {
//                                dialogInterface, i ->
//                            })
//                    val dialog = builders.create()
//                    dialog.show()
//                val builder2 = AlertDialog.Builder(this@DeteksiActivity)
//                builder2.setMessage(finalJawab[1].toString())
//                        .setPositiveButton("jawab2", {
//                            dialogInterface, i ->
//                        })
//                val dialog2 = builder2.create()
//                dialog2.show()
//                val builder3 = AlertDialog.Builder(this@DeteksiActivity)
//                builder3.setMessage(finalJawab[2].toString())
//                        .setPositiveButton("jawab3", {
//                            dialogInterface, i ->
//                        })
//                val dialog3 = builder3.create()
//                dialog3.show()
//                val builder4 = AlertDialog.Builder(this@DeteksiActivity)
//                builder4.setMessage(finalJawab[3].toString())
//                        .setPositiveButton("jawab4", {
//                            dialogInterface, i ->
//                        })
//                val dialog4 = builder4.create()
//                dialog4.show()
//                val builder5 = AlertDialog.Builder(this@DeteksiActivity)
//                builder5.setMessage(finalJawab[4].toString())
//                        .setPositiveButton("jawab5", {
//                            dialogInterface, i ->
//                        })
//                val dialog5 = builder5.create()
//                dialog5.show()
//                val builder6 = AlertDialog.Builder(this@DeteksiActivity)
//                builder6.setMessage(finalJawab[5].toString())
//                        .setPositiveButton("jawab6", {
//                            dialogInterface, i ->
//                        })
//                val dialog6 = builder6.create()
//                dialog6.show()
//                val builder7 = AlertDialog.Builder(this@DeteksiActivity)
//                builder7.setMessage(finalJawab[6].toString())
//                        .setPositiveButton("jawab7", {
//                            dialogInterface, i ->
//                        })
//                val dialog7 = builder7.create()
//                dialog7.show()
//                val builder8 = AlertDialog.Builder(this@DeteksiActivity)
//                builder8.setMessage(finalJawab[7].toString())
//                        .setPositiveButton("jawab8", {
//                            dialogInterface, i ->
//                        })
//                val dialog8 = builder8.create()
//                dialog8.show()
//                val builder9 = AlertDialog.Builder(this@DeteksiActivity)
//                builder9.setMessage(finalJawab[8].toString())
//                        .setPositiveButton("jawab9", {
//                            dialogInterface, i ->
//                        })
//                val dialog9 = builder9.create()
//                dialog9.show()
//                val builder10 = AlertDialog.Builder(this@DeteksiActivity)
//                builder10.setMessage(finalJawab[9].toString())
//                        .setPositiveButton("jawab10", {
//                            dialogInterface, i ->
//                        })
//                val dialog10 = builder10.create()
//                dialog10.show()
//                val builder11 = AlertDialog.Builder(this@DeteksiActivity)
//                builder11.setMessage(finalJawab[10].toString())
//                        .setPositiveButton("jawab11", {
//                            dialogInterface, i ->
//                        })
//                val dialog11 = builder11.create()
//                dialog11.show()
//                val builder12 = AlertDialog.Builder(this@DeteksiActivity)
//                builder12.setMessage(finalJawab[11].toString())
//                        .setPositiveButton("jawab12", {
//                            dialogInterface, i ->
//                        })
//                val dialog12 = builder12.create()
//                dialog12.show()
//                val builder13 = AlertDialog.Builder(this@DeteksiActivity)
//                builder13.setMessage(finalJawab[12].toString())
//                        .setPositiveButton("jawab13", {
//                            dialogInterface, i ->
//                        })
//                val dialog13 = builder13.create()
//                dialog13.show()
//                val builder14 = AlertDialog.Builder(this@DeteksiActivity)
//                builder14.setMessage(finalJawab[13].toString())
//                        .setPositiveButton("jawab14", {
//                            dialogInterface, i ->
//                        })
//                val dialog14 = builder14.create()
//                dialog14.show()
//                val builder15 = AlertDialog.Builder(this@DeteksiActivity)
//                builder15.setMessage(finalJawab[14].toString())
//                        .setPositiveButton("jawab15", {
//                            dialogInterface, i ->
//                        })
//                val dialog15 = builder15.create()
//                dialog15.show()
//                val builder16 = AlertDialog.Builder(this@DeteksiActivity)
//                builder16.setMessage(finalJawab[15].toString())
//                        .setPositiveButton("jawab16", {
//                            dialogInterface, i ->
//                        })
//                val dialog16 = builder16.create()
//                dialog16.show()
//                val builder17 = AlertDialog.Builder(this@DeteksiActivity)
//                builder17.setMessage(finalJawab[16].toString())
//                        .setPositiveButton("jawab17", {
//                            dialogInterface, i ->
//                        })
//                val dialog17 = builder17.create()
//                dialog17.show()
//                val builder18 = AlertDialog.Builder(this@DeteksiActivity)
//                builder18.setMessage(finalJawab[17].toString())
//                        .setPositiveButton("jawab18", {
//                            dialogInterface, i ->
//                        })
//                val dialog18 = builder18.create()
//                dialog18.show()
//                val builder19 = AlertDialog.Builder(this@DeteksiActivity)
//                builder19.setMessage(finalJawab[18].toString())
//                        .setPositiveButton("jawab19", {
//                            dialogInterface, i ->
//                        })
//                val dialog19 = builder19.create()
//                dialog19.show()
//                val builder20 = AlertDialog.Builder(this@DeteksiActivity)
//                builder20.setMessage(finalJawab[19].toString())
//                        .setPositiveButton("jawab20", {
//                            dialogInterface, i ->
//                        })
//                val dialog20 = builder20.create()
//                dialog20.show()




                //intent(idUser, getFinalValues, id_depresi, tanggal)

            }

            override fun onFailure(call: Call<postDeteksiModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.createHasilDeteksi(
                            "" + idUser + "",
                            ""  + tanggal + "",
                            "" + getFinalValues + "",
                            "" + id_depresi + ""
                    ).enqueue(object : Callback<postDeteksiModel> {
                        override fun onResponse(call: Call<postDeteksiModel>, response: Response<postDeteksiModel>) {

                            val id_deteksi = "${response.body()?.id}"
                            for (i in 0..19){
                                val id_pertanyaan = i + 1
                                val nilai = arrayOfNulls<Int>(rg.size)

                                rg.indices.forEach { i ->
                                    val jwbA = rg[i]?.getChildAt(0) as RadioButton
                                    val jwbB = rg[i]?.getChildAt(1) as RadioButton
                                    val jwbC = rg[i]?.getChildAt(2) as RadioButton
                                    val jwbD = rg[i]?.getChildAt(3) as RadioButton
                                    val jwbE = rg[i]?.getChildAt(4) as RadioButton

                                    when {
                                        jwbA.isChecked -> {
                                            nilai[i] = 1
                                        }
                                        jwbB.isChecked -> {
                                            nilai[i] = 2
                                        }
                                        jwbC.isChecked -> {
                                            nilai[i] = 3
                                        }
                                        jwbD.isChecked -> {
                                            nilai[i] = 4
                                        }
                                        jwbE.isChecked -> {
                                            nilai[i] = 5
                                        }
                                    }
                                }
                                api.createPertanyaanJawaban("" + finalJawab[0].toString() + "",
                                        "" + finalJawab[1].toString() + "",
                                        "" + finalJawab[2].toString() +"",
                                        "" + finalJawab[3].toString() +"",
                                        "" + finalJawab[4].toString() +"",
                                        "" + finalJawab[5].toString() +"",
                                        "" + finalJawab[6].toString() +"",
                                        "" + finalJawab[7].toString() +"",
                                        "" + finalJawab[8].toString() +"",
                                        "" + finalJawab[9].toString() +"",
                                        "" + finalJawab[10].toString() +"",
                                        "" + finalJawab[11].toString() +"",
                                        "" + finalJawab[12].toString() +"",
                                        "" + finalJawab[13].toString() +"",
                                        "" + finalJawab[14].toString() +"",
                                        "" + finalJawab[15].toString() +"",
                                        "" + finalJawab[16].toString() +"",
                                        "" + finalJawab[17].toString() +"",
                                        "" + finalJawab[18].toString() +"",
                                        "" + finalJawab[19].toString() +"",
                                        "" + id_deteksi +"").enqueue(object : Callback<postPertanyaanJawabanModel>{
                                    override fun onResponse(call: Call<postPertanyaanJawabanModel>, response: Response<postPertanyaanJawabanModel>) {
                                        if(response.isSuccessful){
                                            //intent(idUser, getFinalValues, id_depresi, tanggal)
                                        }
                                    }

                                    override fun onFailure(call: Call<postPertanyaanJawabanModel>, t: Throwable) {
                                        if(t is SocketTimeoutException){
                                            api.createPertanyaanJawaban("" + finalJawab[0].toString() + "",
                                                    "" + finalJawab[1].toString() + "",
                                                    "" + finalJawab[2].toString() +"",
                                                    "" + finalJawab[3].toString() +"",
                                                    "" + finalJawab[4].toString() +"",
                                                    "" + finalJawab[5].toString() +"",
                                                    "" + finalJawab[6].toString() +"",
                                                    "" + finalJawab[7].toString() +"",
                                                    "" + finalJawab[8].toString() +"",
                                                    "" + finalJawab[9].toString() +"",
                                                    "" + finalJawab[10].toString() +"",
                                                    "" + finalJawab[11].toString() +"",
                                                    "" + finalJawab[12].toString() +"",
                                                    "" + finalJawab[13].toString() +"",
                                                    "" + finalJawab[14].toString() +"",
                                                    "" + finalJawab[15].toString() +"",
                                                    "" + finalJawab[16].toString() +"",
                                                    "" + finalJawab[17].toString() +"",
                                                    "" + finalJawab[18].toString() +"",
                                                    "" + finalJawab[19].toString() +"",
                                                    "" + id_deteksi +"").enqueue(object : Callback<postPertanyaanJawabanModel>{
                                                override fun onResponse(call: Call<postPertanyaanJawabanModel>, response: Response<postPertanyaanJawabanModel>) {
                                                    if(response.isSuccessful){
                                                        //intent(idUser, getFinalValues, id_depresi, tanggal)
                                                    }
                                                }

                                                override fun onFailure(call: Call<postPertanyaanJawabanModel>, t: Throwable) {
                                                    if(t is SocketTimeoutException){
                                                        dialog.dismiss()
                                                        Toast.makeText(this@DeteksiActivity,"timeout", Toast.LENGTH_LONG).show()
                                                    }
                                                }

                                            })
                                        }
                                    }

                                })
                            }

                            //intent(idUser, getFinalValues, id_depresi, tanggal)

                        }

                        override fun onFailure(call: Call<postDeteksiModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@DeteksiActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        })



        intent(idUser, getFinalValues, id_depresi, tanggal)
    }

    private fun intent (id_user: String, getFinalValues: Double, id_depresi: Int, tanggal: Timestamp){
        val intent = Intent(this, HasilDeteksiActivity::class.java)
        intent.putExtra("id_user", id_user)
        intent.putExtra("hasil_hitung", getFinalValues.toString())
        intent.putExtra("id_depresi", id_depresi.toString())
        intent.putExtra("tanggal", tanggal.toString())
        startActivity(intent)
    }

    private fun getCfCombine(param: ArrayList<Double>): Double {
        var CF = 0.0
        for (i in 0 until param.size) {
            when (i) {
                0 -> {
                    CF = param[i]
                }

                // CF Selanjutnya
                else -> {
                    CF += (param[i] * (1 - CF))
                }
            }
        }

        return CF
    }

    @SuppressLint("SetTextI18n")
    private fun handlingUI() {

        var page = 0

        frame_soal_1 = findViewById(R.id.frame_soal_1)
        frame_soal_2 = findViewById(R.id.frame_soal_2)
        frame_soal_3 = findViewById(R.id.frame_soal_3)
        frame_soal_4 = findViewById(R.id.frame_soal_4)
        frame_soal_5 = findViewById(R.id.frame_soal_5)
        frame_soal_6 = findViewById(R.id.frame_soal_6)
        frame_soal_7 = findViewById(R.id.frame_soal_7)
        frame_soal_8 = findViewById(R.id.frame_soal_8)
        frame_soal_9 = findViewById(R.id.frame_soal_9)
        frame_soal_10 = findViewById(R.id.frame_soal_10)
        frame_soal_11 = findViewById(R.id.frame_soal_11)
        frame_soal_12 = findViewById(R.id.frame_soal_12)
        frame_soal_13 = findViewById(R.id.frame_soal_13)
        frame_soal_14 = findViewById(R.id.frame_soal_14)
        frame_soal_15 = findViewById(R.id.frame_soal_15)
        frame_soal_16 = findViewById(R.id.frame_soal_16)
        frame_soal_17 = findViewById(R.id.frame_soal_17)
        frame_soal_18 = findViewById(R.id.frame_soal_18)
        frame_soal_19 = findViewById(R.id.frame_soal_19)
        frame_soal_20 = findViewById(R.id.frame_soal_20)


        val frameList = arrayOf(
                frame_soal_1,
                frame_soal_2,
                frame_soal_3,
                frame_soal_4,
                frame_soal_5,
                frame_soal_6,
                frame_soal_7,
                frame_soal_8,
                frame_soal_9,
                frame_soal_10,
                frame_soal_11,
                frame_soal_12,
                frame_soal_13,
                frame_soal_14,
                frame_soal_15,
                frame_soal_16,
                frame_soal_17,
                frame_soal_18,
                frame_soal_19,
                frame_soal_20
        )

        btn_prev = findViewById(R.id.btn_prev)
        btn_prev.visibility = View.INVISIBLE

        btn_next = findViewById(R.id.btn_next)
        btn_next.setOnClickListener {
            val pass = pageLayoutChecking(page)
            when {
                pass -> {

                    // change page
                    page++
                    btn_prev.visibility = View.VISIBLE
                    when(page){
                        frameList.size - 1 -> btn_next.visibility = View.GONE
                    }
                    frameList.forEach { i ->
                        i.visibility = View.GONE
                    }
                    frameList[page].visibility = View.VISIBLE
                }

                else -> Toast.makeText(this,"Jawaban tidak boleh kosong", Toast.LENGTH_LONG).show()

            }

            btn_prev.setOnClickListener {
                page--

                btn_next.visibility = View.VISIBLE
                when (page) {
                    0 -> btn_prev.visibility = View.INVISIBLE
                }
                frameList.forEach { i ->
                    i.visibility = View.GONE
                }
                frameList[page].visibility = View.VISIBLE
            }
        }
    }

    private fun pageLayoutChecking(index: Int): Boolean {
        rg1 = findViewById(R.id.rg1)
        rg2 = findViewById(R.id.rg2)
        rg3 = findViewById(R.id.rg3)
        rg4 = findViewById(R.id.rg4)
        rg5 = findViewById(R.id.rg5)
        rg6 = findViewById(R.id.rg6)
        rg7 = findViewById(R.id.rg7)
        rg8 = findViewById(R.id.rg8)
        rg9 = findViewById(R.id.rg9)
        rg10 = findViewById(R.id.rg10)
        rg11 = findViewById(R.id.rg11)
        rg12 = findViewById(R.id.rg12)
        rg13 = findViewById(R.id.rg13)
        rg14 = findViewById(R.id.rg14)
        rg15 = findViewById(R.id.rg15)
        rg16 = findViewById(R.id.rg16)
        rg17 = findViewById(R.id.rg17)
        rg18 = findViewById(R.id.rg18)
        rg19 = findViewById(R.id.rg19)
        rg20 = findViewById(R.id.rg20)


        return when (index) {
            0 -> rg1.checkedRadioButtonId != -1
            1 -> rg2.checkedRadioButtonId != -1
            2 -> rg3.checkedRadioButtonId != -1
            3 -> rg4.checkedRadioButtonId != -1
            4 -> rg5.checkedRadioButtonId != -1
            5 -> rg6.checkedRadioButtonId != -1
            6 -> rg7.checkedRadioButtonId != -1
            7 -> rg8.checkedRadioButtonId != -1
            8 -> rg9.checkedRadioButtonId != -1
            9 -> rg10.checkedRadioButtonId != -1
            10 -> rg11.checkedRadioButtonId != -1
            11 -> rg12.checkedRadioButtonId != -1
            12 -> rg13.checkedRadioButtonId != -1
            13 -> rg14.checkedRadioButtonId != -1
            14 -> rg15.checkedRadioButtonId != -1
            15 -> rg16.checkedRadioButtonId != -1
            16 -> rg17.checkedRadioButtonId != -1
            17 -> rg18.checkedRadioButtonId != -1
            18 -> rg19.checkedRadioButtonId != -1
            19 -> rg20.checkedRadioButtonId != -1
            else -> false

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val id_user = intent.getStringExtra("id_user")
        val builder = AlertDialog.Builder(this@DeteksiActivity)
        builder.setMessage("Apakah kamu yakin tidak ingin melanjutkan deteksi dini depresi?")
                .setPositiveButton("IYA", {
                    dialogInterface, i -> val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("id_user", id_user)
                    startActivity(intent)
                    finishAffinity()
                })
                .setNegativeButton("TIDAK", {
                    dialogInterface, i ->
                })
        val dialog = builder.create()
        dialog.show()
    }
}