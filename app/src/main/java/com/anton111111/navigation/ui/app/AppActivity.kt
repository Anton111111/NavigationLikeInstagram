package com.anton111111.navigation.ui.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.anton111111.navigation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @TODO test with Don't keep activities
 * @TODO add ui tests
 */
class AppActivity : AppCompatActivity(R.layout.app_activity) {

    private lateinit var rootNavHostFragment: NavHostFragment
    private val backPressedOnce: AtomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_nav_host) as NavHostFragment

    }

    override fun onBackPressed() {
        if (isTaskRoot
            && countEntriesInBackStack(rootNavHostFragment) == 0
        ) {
            if (backPressedOnce.get()) {
                //Fix leak issue https://issuetracker.google.com/issues/139738913
                finishAfterTransition()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please click BACK again to exit.", Toast.LENGTH_SHORT
                ).show()
                backPressedOnce.set(true)
                lifecycleScope.launch {
                    delay(2000)
                    backPressedOnce.set(false)
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun countEntriesInBackStack(fragment: Fragment): Int {
        var count = fragment.childFragmentManager.backStackEntryCount
        fragment.childFragmentManager.primaryNavigationFragment?.let { primary ->
            count += countEntriesInBackStack(primary)
        }
        return count
    }
}
