package finalproject.ggcfinal

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_spectate.*
import kotlinx.android.synthetic.main.app_bar_spectate.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SpectateActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var fname: String = ""
    var uname: String = ""
    var lname: String = ""
    var id: Int = 0
    var insid: Int = 0
    var inscode: Int = 0
    var up: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spectate)
        setSupportActionBar(toolbar)

        fname = intent.extras.getString("FNAME")
        uname = intent.extras.getString("UNAME")
        lname = intent.extras.getString("LNAME")
        id = intent.extras.getInt("ID")
        insid = intent.extras.getInt("INSID")
        inscode = intent.extras.getInt("CODE")
        up = intent.extras.getFloat("UP")

        loadData()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            //super.onBackPressed()
            moveTaskToBack(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.spectate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {

            R.id.action_help -> return true
            R.id.action_quit -> {
                moveTaskToBack(true)
                return true
            }
            R.id.action_logout ->{
                val pref = SharedPrefManager.getInstance(this@SpectateActivity)
                pref.logout()
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dash -> {
                val spIntent = Intent(this@SpectateActivity,DrawerActivity::class.java)
                spIntent.putExtra("UNAME",uname)
                spIntent.putExtra("FNAME",fname)
                spIntent.putExtra("LNAME",lname)
                spIntent.putExtra("ID",id)
                spIntent.putExtra("INSID",insid)
                spIntent.putExtra("CODE",inscode)
                startActivity(spIntent)
                overridePendingTransition(0, 0)
                finish()
            }
            R.id.nav_spec -> {

            }
            R.id.nav_set -> {
                val sIntent = Intent(this@SpectateActivity,SettingsActivity::class.java)
                startActivity(sIntent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true

    }
    fun loadData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerspectate) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val url = "http://192.168.43.147/spectate.php"
        val q = Volley.newRequestQueue(this@SpectateActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    val specCard = ArrayList<SpectateCard>()
                    try{
                        val obj = JSONObject(response)
                        val jsnobj = obj.getJSONArray("response")
                        for (i in 0 until jsnobj.length()) {
                            val jsonObject = jsnobj.getJSONObject(i)

                            val gindex = jsonObject.getString("GreenIndex")
                            val insname = jsonObject.getString("InsName")
                            var rating: String = ""

                            val a=i+1
                            val leedindex=gindex.toFloat()*20
                            if(leedindex<=50){
                                rating = "Certified"
                            }
                            else if(leedindex<60 && leedindex>=50){
                                rating = "Silver"
                            }
                            else if(leedindex<80 && leedindex>=60){
                                rating="Gold"
                            }
                            else if(leedindex>=80 && leedindex<100){
                                rating="Platinum"
                            }
                            specCard.add(SpectateCard("#"+"$a","$insname"," "+"$leedindex"+"/100"+" (↑"+"$up"+"%)","  "+"$rating"))
                            val adapter = SpectateAdapter (specCard)

                            recyclerView.adapter = adapter


                        }
                    }catch(e: JSONException){
                        toast("Please check your credentials and try again")
                    }
                }, Response.ErrorListener { error -> toast("The server could not process your request")
        }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("uid", id.toString())
                return params
            }


        }
        q.add(stringRequest)
    }
}
