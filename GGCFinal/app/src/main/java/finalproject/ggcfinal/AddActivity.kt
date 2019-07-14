package finalproject.ggcfinal

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.SeekBar.OnSeekBarChangeListener
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION")
class AddActivity : AppCompatActivity() {
    var liter: Long = 0
    var vol: Long = 0
    var vis: Long = 0
    var wType: String = ""
    var wconvention: String = ""
    var uid: Int = 0
    var insid: Int = 0
    var note: String = ""
    var fname: String = ""
    var uname: String = ""
    var lname: String = ""
    var inscode: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        fname = intent.extras.getString("FNAME")
        uname = intent.extras.getString("UNAME")
        lname = intent.extras.getString("LNAME")
        uid = intent.extras.getInt("ID")
        insid = intent.extras.getInt("INSID")
        inscode = intent.extras.getInt("CODE")
        val seekBar = findViewById<SeekBar>(R.id.seekBar) as SeekBar
        val seekBarValue = findViewById<EditText>(R.id.seekvalue) as EditText
        vis = 0
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                // TODO Auto-generated method stub
                seekBarValue.setText(progress.toString())
                liter = progress.toLong()
                vol = liter * 1000
                textView13.text = vol.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })

        seekv.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // TODO Auto-generated method stub
                vis = progress.toLong()
                visText.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })

        seekBarValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    //Update Seekbar value after entering a number
                    seekBar.setProgress(Integer.parseInt(s.toString()))
                } catch (ex: Exception) {
                }

            }
        })

    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
       menuInflater.inflate(R.menu.done, menu)
       return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        when (item.itemId) {
            R.id.action_done -> {
                addData()
                v.vibrate(25)
                toast("Data added successfully")
                val intent = Intent(this@AddActivity,DrawerActivity::class.java)
                intent.putExtra("UNAME",uname)
                intent.putExtra("FNAME",fname)
                intent.putExtra("LNAME",lname)
                intent.putExtra("ID",uid)
                intent.putExtra("INSID",insid)
                intent.putExtra("CODE",inscode)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {

        AlertDialog.Builder(this, R.style.DialogDark)

                .setMessage("Discard data?")
                .setCancelable(true)
                .setPositiveButton("DISCARD") { dialog, id -> this@AddActivity.finish() }
                .setNegativeButton("CANCEL", null)
                .show()
        val intent = Intent(this@AddActivity,DrawerActivity::class.java)
        intent.putExtra("UNAME",uname)
        intent.putExtra("FNAME",fname)
        intent.putExtra("LNAME",lname)
        intent.putExtra("ID",uid)
        intent.putExtra("INSID",insid)
        startActivity(intent)

    }

    fun addData() {
        val pref = SharedPrefManager.getInstance(this@AddActivity)


        if(raddsolid.isChecked){
            wType = "solid waste"
        }
        else if (raddliquid.isChecked){
            wType = "liquid waste"
        }


        if(raddbiod.isChecked){
            wconvention = "biodegradable"
        }
        else if (radnonbio.isChecked){
            wconvention = "non-biodegradable"
        }

        if(rec.isChecked){
            wconvention = "$wconvention"+" and"+" recyclable"
        }

        note = Note.text.toString()
       // val view: View
        val url = "http://192.168.43.147/add.php"
        val q = Volley.newRequestQueue(this@AddActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
         //           Snackbar.make(view, "Data added successfully",Snackbar.LENGTH_LONG)
           //                 .setAction("OKAY",null).show()
                }, Response.ErrorListener { error -> toast("$error") }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("uid", uid.toString())
                params.put("insid", insid.toString())
                params.put("wtype", wType)
                params.put("wconvention", wconvention)
                params.put("quantity", liter.toString())
                params.put("plevel", vis.toString())
                params.put("note", note)
                return params
            }
        }
        q.add(stringRequest)

    }
}
