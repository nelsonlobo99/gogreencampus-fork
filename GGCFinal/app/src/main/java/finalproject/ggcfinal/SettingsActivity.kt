package finalproject.ggcfinal

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.ActionBar
import android.content.Intent
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
