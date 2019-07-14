package finalproject.ggcfinal

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.text.method.TextKeyListener.clear
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE



/**
 * Created by nelson on 15/3/18.
 */
class SharedPrefManager (context: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_USERNAME, null) != null
        }

    fun getUser(): String {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val user = sharedPreferences.getString(KEY_USERNAME, null)
        return user
    }
    fun getFname(): String {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString(KEY_FNAME, null)
        return fname
    }
    fun getLname(): String {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val lname = sharedPreferences.getString(KEY_LNAME, null)
        return lname
    }

    fun getId(): Int {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val id = sharedPreferences.getString(KEY_ID, null)
        return id as Int
    }

    fun getInsId(): Int {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val insid = sharedPreferences.getString(KEY_INSID, null)
        return insid as Int
    }

    init {
        mCtx = context
    }

    fun userLogin(username: String,fname: String,lname: String,id: Int,insId: Int) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_FNAME,fname)
        editor.putString(KEY_LNAME,lname)
        editor.putInt(KEY_ID, id)
        editor.putString(KEY_USERNAME, username)
        editor.putInt(KEY_INSID, insId)
        editor.apply()
    }


    fun logout() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        mCtx.startActivity(Intent(mCtx, MainActivity::class.java))
    }

    companion object {
        private val KEY_FNAME = "firstname"
        private val KEY_LNAME = "lastname"
        private val SHARED_PREF_NAME = "mysharedpref"
        private val KEY_USERNAME = "username"
        private val KEY_INSID = "institute_id"
        private val KEY_ID = "id"

        private var mInstance: SharedPrefManager? = null
        private lateinit var mCtx: Context

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}