package cm.dija.dp.videocompressor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {

    lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navHost = supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {

        if (navHost.navController.currentDestination!!.id == R.id.menuFragment) {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit this application?")
                .setPositiveButton("Yes") { dialog, which -> System.exit(0) }
                .setNeutralButton("Rate Us") { dialog, which ->
                    // TODO Auto-generated method stub
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=cm.dija.dp.videocompressor")
                            )
                        )
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=cm.dija.dp.videocompressor")
                            )
                        )
                    }
                }
                .setNegativeButton("No", null)
                .show()

        } else {
            super.onBackPressed()
        }
    }

}