package finalproject.ggcfinal

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.text.SpannableString
import android.text.style.UnderlineSpan
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.json.JSONObject
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import org.json.JSONException


@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity() {
    //var uname = UserName.text
  //  var pass = passwordedit.text
//    val url = "http://10.0.0.2/test.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val content=SpannableString("Don't have an account?")
        content.setSpan(UnderlineSpan(),0, content.length, 0)

        val content2=SpannableString("Forgot Password")
        content.setSpan(UnderlineSpan(),0, content2.length, 0)


        textView3.text=content
        button.onClick {

            v.vibrate(25)
            loadData()

        }

      textView3.onClick {
          val intent = Intent(this@MainActivity,SignUpActivity::class.java)
          v.vibrate(25)
          startActivity(intent)
          //return true
      }

      textView6.onClick {
          val intent = Intent(this@MainActivity,IforgotActivity::class.java)
          startActivity(intent)
          v.vibrate(25)
          //return true
      }

    }

    fun loadData() {
        val uname = Username.text
        val pass = passwordedit.text
        val url = "http://192.168.43.147/test.php"
        val q = Volley.newRequestQueue(this@MainActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Listener { response ->
                    // Display the first 500 characters of the response string.
                    try{
                        val obj = JSONObject(response)
                        val jsnobj = obj.getJSONArray("response")
                        for (i in 0 until jsnobj.length()) {
                            val jsonObject = jsnobj.getJSONObject(i)

                            val username = jsonObject.getString("UserName")
                            val fname = jsonObject.getString("FirstName")
                            val lname = jsonObject.getString("LastName")
                            val id = jsonObject.getInt("Id")
                            val insid = jsonObject.getInt("InsId")
                            val inscode = jsonObject.getInt("InsCode")
                            val pref = SharedPrefManager(this@MainActivity)
                            pref.userLogin(username,fname,lname,id,insid)
                            toast("Welcome, "+fname+". :)")
                            val intent=Intent(this@MainActivity,DrawerActivity::class.java)
                            intent.putExtra("UNAME",username)
                            intent.putExtra("FNAME",fname)
                            intent.putExtra("LNAME",lname)
                            intent.putExtra("ID",id)
                            intent.putExtra("INSID",insid)
                            intent.putExtra("CODE",inscode)
                            startActivity(intent)

                        }
                    }catch(e: JSONException){
                        toast("Please check your credentials and try again")
                    }
                }, Response.ErrorListener { error -> toast("The server could not process your request") }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("username", uname.toString())
                params.put("password", pass.toString())
                return params
            }
        }
        q.add(stringRequest)

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
