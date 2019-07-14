package finalproject.ggcfinal

import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import android.R.attr.fragment
import android.R.attr.stateListAnimator
import android.app.SearchManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.SearchView
import android.view.View

import android.support.v7.widget.Toolbar
import android.support.v4.view.MenuItemCompat
import android.R.menu
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Vibrator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reg_institute.*
import kotlinx.android.synthetic.main.content_drawer.*
import kotlinx.android.synthetic.main.nav_header_drawer.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


@Suppress("DEPRECATION")
class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var fname: String = ""
    var uname: String = ""
    var lname: String = ""
    var id: Int = 0
    var insid: Int = 0
    var inscode: Int = 0
    var per: Float = 0F
    var ei: Float = 0F
    var upper: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fname = intent.extras.getString("FNAME")
        uname = intent.extras.getString("UNAME")
        lname = intent.extras.getString("LNAME")
        id = intent.extras.getInt("ID")
        insid = intent.extras.getInt("INSID")
        inscode = intent.extras.getInt("CODE")
        //toast("$id")
        val header = nav_view.getHeaderView(0)
        val userfullname = header.findViewById<TextView>(R.id.fullname)
        val username = header.findViewById<TextView>(R.id.username)

        loadData()
        loadGraphData()


        userfullname.text=fname+" "+lname
        username.text = uname
        fab.setOnClickListener { view ->
            val intent = Intent(this@DrawerActivity,AddActivity::class.java)
            intent.putExtra("UID",id)
            intent.putExtra("INSID",insid)
            v.vibrate(25)
            intent.putExtra("UNAME",uname)
            intent.putExtra("FNAME",fname)
            intent.putExtra("LNAME",lname)
            intent.putExtra("ID",id)
            intent.putExtra("INSID",insid)
            intent.putExtra("CODE",inscode)
            startActivity(intent)
            finish()
        }

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
                moveTaskToBack(true)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Retrieve the SearchView and plug it into SearchManager
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_help -> return true
            R.id.action_search ->  {
                v.vibrate(25)
                return true
            }
            R.id.action_quit -> {
                moveTaskToBack(true)
                return true
            }
            R.id.action_logout ->{
                val pref = SharedPrefManager.getInstance(this@DrawerActivity)
                pref.logout()
                finish()
                toast("Goodbye, cruel world...")
                v.vibrate(25)
                v.vibrate(25)
                v.vibrate(25)

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_dashboard -> {
            }
            R.id.nav_spectate -> {
                val spIntent = Intent(this@DrawerActivity,SpectateActivity::class.java)
                spIntent.putExtra("UNAME",uname)
                spIntent.putExtra("FNAME",fname)
                spIntent.putExtra("LNAME",lname)
                spIntent.putExtra("ID",id)
                spIntent.putExtra("INSID",insid)
                spIntent.putExtra("UP",upper)
                startActivity(spIntent)
                overridePendingTransition(0, 0);
                finish()
            }
            R.id.nav_settings -> {
                val sIntent = Intent(this@DrawerActivity,SettingsActivity::class.java)
                startActivity(sIntent)
            }

        }
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment)
            ft.commit()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun loadData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val url = "http://192.168.43.147/trash.php"
        val q = Volley.newRequestQueue(this@DrawerActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    val dashCard = ArrayList<DashCard>()
                    try{
                        val obj = JSONObject(response)
                        val jsnobj = obj.getJSONArray("response")
                        for (i in 0 until jsnobj.length()) {
                            val jsonObject = jsnobj.getJSONObject(i)

                            val qty = jsonObject.getInt("Quantity")
                            val wtype = jsonObject.getString("Wtype")
                            val note = jsonObject.getString("Note")
                            val convention = jsonObject.getString("Wconvention")
                            val day = jsonObject.getString("TimeDay")
                            val month = jsonObject.getString("TimeMonth")
                            val year = jsonObject.getString("TimeYear")
                            val vis = jsonObject.getInt("Plevel")
                            val  vol = qty*1000
                            dashCard.add(DashCard("Collected "+"$qty"+" liters of "+"$wtype","$note","Item is "+"$convention","$day"+"/"+"$month"+"/"+"$year","$vol"+" cm³","$vis"+"KM"))
                            val adapter = CustomAdapter (dashCard)

                            recyclerView.adapter = adapter


                        }
                    }catch(e: JSONException){
                        toast("Please check your credentials and try again")
                    }
                }, Response.ErrorListener { error -> toast("The server could not process your request")}) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("uid", id.toString())
                return params
            }


        }
        q.add(stringRequest)
    }

    fun loadGraphData() {

        val url = "http://192.168.43.147/gdata.php"
        val q = Volley.newRequestQueue(this@DrawerActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    try{
                        val obj = JSONObject(response)
                        val num = obj.getInt("num")
                        val rec = obj.getInt("rec")
                        val total = obj.getInt("total")
                        ei = (rec.toFloat()/num.toFloat())*5
                        ei = (Math.floor((ei*10).toDouble())/10
                                ).toFloat()
                        per = ((num*0.83).toFloat())
                        graph()
                        val rper = (num.toFloat()/total.toFloat())*100
                        upper = rper
                        updateGi()
                        goal.text = "Goal: "+per.toString()+"%"+" | Environmental Index: "+"$ei"+"/5.0 (↑"+"$rper"+"%)"
                    }catch(e: JSONException){
                        toast("$e")
                    }
                }, Response.ErrorListener { error -> toast("The server could not process your request") }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("uid", id.toString())
                return params
            }


        }
        q.add(stringRequest)
    }
    fun updateGi() {

        val url = "http://192.168.43.147/giupdate.php"
        val q = Volley.newRequestQueue(this@DrawerActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                }, Response.ErrorListener { error -> toast("The server could not process your request") }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("code", inscode.toString())
                params.put("gi",ei.toString())
                return params
            }


        }
        q.add(stringRequest)
    }

    fun graph(){
        var graphview : GraphView = findViewById<GraphView>(R.id.graph)

        val series = LineGraphSeries<DataPoint>()
        series.setColor(Color.rgb(38,166,154))
        series.setThickness(11)
        graphview.viewport.setMaxX(83.0)
        graphview.viewport.setMinX(0.0)
        graphview.viewport.setMinY(0.0)
        graphview.viewport.setMaxY(100.0)
        val gridlabel = graphview.gridLabelRenderer
        //gridlabel.setVerticalAxisTitle("Progress")
        graphview.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        graphview.viewport.isScalable = true
        graphview.viewport.setScalableY(true)
        graphview.viewport.setYAxisBoundsManual(true)
        graphview.viewport.setXAxisBoundsManual(true)
       var x = 0.0
        for (i in 0..100)
        {
            if(i>Math.abs(per).toInt()){
                break;
            }
            x = i.toDouble()
           val y = x
            series.appendData(DataPoint(x,y), true, 500)
        }


        graphview.addSeries(series)
    }
}
