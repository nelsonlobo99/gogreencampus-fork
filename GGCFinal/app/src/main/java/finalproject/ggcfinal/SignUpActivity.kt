/***
 *
 * GoGreenCampus
Copyright (C) 2018 Owais Shaikh and Nelson Lobo

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

 *
 * ***/

package finalproject.ggcfinal

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import android.widget.Toast
import android.widget.Spinner
import org.jetbrains.anko.sdk25.coroutines.onClick


@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //val adapter = ArrayAdapter(this,R.layout.spinner_item,spinnercountry)
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        regins.onClick {
            val intent = Intent(this@SignUpActivity,RegInstituteActivity::class.java)
            startActivity(intent)
            v.vibrate(25)
        }

        signupbutton.onClick {
            v.vibrate(25)
            putData()
        }

    }
    fun putData() {
        val fname = FirstName.text
        val lname = LastName.text
        val uname = user.text
        val password = pass.text
        val mail = mail.text
        val country: String
        val state: String
        val city: String
        val inscode = institutecode.text

        val spin = findViewById<Spinner>(R.id.spinnercountries) as Spinner
        val st = spin.selectedItem.toString()
        val pos = spin.selectedItemPosition
        if (pos != 0) {
            country = spin.selectedItem.toString()
        } else {
            Toast.makeText(this@SignUpActivity,
                    "Please select a country", Toast.LENGTH_LONG)
                    .show()
            return
        }


        val spinstate = findViewById<Spinner>(R.id.spinnerstates) as Spinner
        val sst = spinstate.selectedItem.toString()
        val spos = spinstate.selectedItemPosition
        if (spos != 0) {
            state = spinstate.selectedItem.toString()
        } else {
            Toast.makeText(this@SignUpActivity,
                    "Please select a state", Toast.LENGTH_LONG)
                    .show()
            return
        }


        val spincity = findViewById<Spinner>(R.id.spinnercities) as Spinner
        val cst = spincity.selectedItem.toString()
        val cpos = spincity.selectedItemPosition
        if (cpos != 0) {
            city = spincity.selectedItem.toString()
        } else {
            Toast.makeText(this@SignUpActivity,
                    "Please select your city", Toast.LENGTH_LONG)
                    .show()
            return
        }
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val url = "http://192.168.43.147/signup.php"
        val q = Volley.newRequestQueue(this@SignUpActivity)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    toast("Account created successfully!")
                    finish()
                    // Display the first 500 characters of the response string.

                }, Response.ErrorListener { error -> toast("$error") }) {
            public override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("username", uname.toString())
                params.put("password", password.toString())
                params.put("fname", fname.toString())
                params.put("lname", lname.toString())
                params.put("country", country.toString())
                params.put("state", state.toString())
                params.put("city", city.toString())
                params.put("inscode",inscode.toString())
                params.put("mail", mail.toString())
                return params
            }
        }
        q.add(stringRequest)

    }
}

