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
import com.example.areyouokay.Model.*
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
    private lateinit var soal_1: TextView
    private lateinit var soal_2: TextView
    private lateinit var soal_3: TextView
    private lateinit var soal_4: TextView
    private lateinit var soal_5: TextView
    private lateinit var soal_6: TextView
    private lateinit var soal_7: TextView
    private lateinit var soal_8: TextView
    private lateinit var soal_9: TextView
    private lateinit var soal_10: TextView
    private lateinit var soal_11: TextView
    private lateinit var soal_12: TextView
    private lateinit var soal_13: TextView
    private lateinit var soal_14: TextView
    private lateinit var soal_15: TextView
    private lateinit var soal_16: TextView
    private lateinit var soal_17: TextView
    private lateinit var soal_18: TextView
    private lateinit var soal_19: TextView
    private lateinit var soal_20: TextView
    private lateinit var Selesai: Button
    private var rg = arrayOfNulls<RadioGroup>(20)
    private var jawaban = Bobot.JAWABAN
    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater
    private lateinit var btnSelesai: Button
    val cfPakar = ArrayList<Double>()
    val idsoal = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)

        handlingUI()



        api.getPertanyaan().enqueue(object :
            Callback<List<getPertanyaanModel>> {
            override fun onResponse(call: Call<List<getPertanyaanModel>>, response: Response<List<getPertanyaanModel>>) {
                if (response.isSuccessful) {

                    val listData = response.body()!!
                    val data2 = listData.sortedBy { it.id.toInt() }

                    data2.forEach {
                        cfPakar.add(it.bobot!!.toDouble())
                        idsoal.add(it.id!!)

                    }


                }
            }

            override fun onFailure(call: Call<List<getPertanyaanModel>>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getPertanyaan().enqueue(object :
                        Callback<List<getPertanyaanModel>> {
                        override fun onResponse(call: Call<List<getPertanyaanModel>>, response: Response<List<getPertanyaanModel>>) {
                            if (response.isSuccessful) {
                                val listData = response.body()!!
                                val data2 = listData.sortedByDescending { it.id.toInt() }

                                listData.forEach {
                                    cfPakar.add(it.bobot!!.toDouble())
                                    idsoal.add(it.id!!)
                                }

                            }
                        }

                        override fun onFailure(call: Call<List<getPertanyaanModel>>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@DeteksiActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }


                    })
                }
            }


        })

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

    private fun getPertanyaan(){

    }

    @SuppressLint("SetTextI18n")
    private fun handlingUI() {

        var page = 0

        val groupPertanyaan = ArrayList<String>()

        soal_1 = findViewById(R.id.soal_1)
        soal_2 = findViewById(R.id.soal_2)
        soal_3 = findViewById(R.id.soal_3)
        soal_4 = findViewById(R.id.soal_4)
        soal_5 = findViewById(R.id.soal_5)
        soal_6 = findViewById(R.id.soal_6)
        soal_7 = findViewById(R.id.soal_7)
        soal_8 = findViewById(R.id.soal_8)
        soal_9 = findViewById(R.id.soal_9)
        soal_10 = findViewById(R.id.soal_10)
        soal_11 = findViewById(R.id.soal_11)
        soal_12 = findViewById(R.id.soal_12)
        soal_13 = findViewById(R.id.soal_13)
        soal_14 = findViewById(R.id.soal_14)
        soal_15 = findViewById(R.id.soal_15)
        soal_16 = findViewById(R.id.soal_16)
        soal_17 = findViewById(R.id.soal_17)
        soal_18 = findViewById(R.id.soal_18)
        soal_19 = findViewById(R.id.soal_19)
        soal_20 = findViewById(R.id.soal_20)

        val soal = arrayOf(
                soal_1,
                soal_2,
                soal_3,
                soal_4,
                soal_5,
                soal_6,
                soal_7,
                soal_8,
                soal_9,
                soal_10,
                soal_11,
                soal_12,
                soal_13,
                soal_14,
                soal_15,
                soal_16,
                soal_17,
                soal_18,
                soal_19,
                soal_20

        )

        api.getPertanyaan().enqueue(object :
                Callback<List<getPertanyaanModel>> {
            override fun onResponse(call: Call<List<getPertanyaanModel>>, response: Response<List<getPertanyaanModel>>) {
                if (response.isSuccessful) {

                    val listData = response.body()!!
                    val data = listData.sortedBy { it.id.toInt() }

                    data.forEach {
                        groupPertanyaan.add(it.pertanyaan!!)
                    }
                    soal[0].setText("1. "+groupPertanyaan[0])


                }
            }

            override fun onFailure(call: Call<List<getPertanyaanModel>>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getPertanyaan().enqueue(object :
                            Callback<List<getPertanyaanModel>> {
                        override fun onResponse(call: Call<List<getPertanyaanModel>>, response: Response<List<getPertanyaanModel>>) {
                            if (response.isSuccessful) {
                                val groupPertanyaan = ArrayList<String>()
                                val listData = response.body()!!
                                val data = listData.sortedBy { it.id.toInt() }

                                data.forEach {
                                    groupPertanyaan.add(it.pertanyaan!!)
                                }
                                soal[0].setText("1. "+groupPertanyaan[0])


                            }
                        }

                        override fun onFailure(call: Call<List<getPertanyaanModel>>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@DeteksiActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }


                    })
                }
            }


        })



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
                    val nomor = page + 1
                    soal[page].setText(nomor.toString() +". "+groupPertanyaan[page])
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
                val nomor2 = page + 1
                soal[page].setText(nomor2.toString() +". "+groupPertanyaan[page])
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