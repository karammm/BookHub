package com.karam.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar:Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem:MenuItem?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setUpToolBar()

        openDashboard()//to open dashboard by default

        val actionBarDrawerToggle=ActionBarDrawerToggle(
            this@MainActivity ,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        //this will set a click listner on the hambugger icon whc is the action bar toggle
        actionBarDrawerToggle.syncState()

        //On click listner on each navigation vier items
        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem!=null){
                previousMenuItem?.isChecked=false

            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){
                R.id.dashboard->{
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,FauvouriesFragment())
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()

                }
                R.id.profile->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,ProfileFragment())
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()

                }
                R.id.aboutApp->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,AboutAppFragment())
                        .commit()
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }


    }
    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Hub"//this ?mark is a null safety operator
        //since the title bar can be null as sometime we dont have a toolbar on the screen
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    //functionto open dashboard by default
    fun openDashboard(){
        val fragment=DashboardFragment()
        val transition=supportFragmentManager.beginTransaction()
        transition.replace(R.id.frameLayout,fragment)
        transition.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    //functon for back press
    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)//its value is the frame that frame layout creates
        when(frag){
            !is DashboardFragment -> openDashboard()//when frame is not the dashboard thn open the dashboard
            else -> super.onBackPressed()//else it wil exit the app default funtionality
        }
    }
}
