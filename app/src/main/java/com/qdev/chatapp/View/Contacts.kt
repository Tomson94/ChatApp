package com.qdev.chatapp.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qdev.chatapp.R
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.ViewModel.ContactsViewModel
import kotlinx.android.synthetic.main.activity_contacts.*

class Contacts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        val uselist = this.userlist
        val textView  =this.nousers
        var viewModel = getViewModel()
        var pd=ProgressDialog(this)
        pd.setMessage("Loading  contacts...")
        pd.show()
        viewModel.getAllContacts(pd)

        viewModel.getData().observe(this, Observer { result ->


            doInUpdates(uselist,textView,result,pd)

            uselist.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                Utils.LoggedInUser.chatWith=result.get(position)
                Utils.setSharedPrefference(getApplication(),"chatwith",result.get(position))
                var intent = Intent(this,ChatWith::class.java)
                startActivity(intent)

            }

        })


    }

    private fun doInUpdates(uselist: ListView?, textView: TextView?, result: List<String>?, pd: ProgressDialog) {

        if (result != null) {
            if (result.size <= 1) {

                textView!!.visibility = View.VISIBLE
                if (uselist != null) {
                    uselist.setVisibility(View.GONE)
                }

            }else{
                if (uselist != null) {

                    uselist.setAdapter(
                        ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1, result))
                    textView!!.visibility = View.GONE

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id==R.id.logout){
            Utils.setSharedPrefference(applicationContext,"status","0")
            var  intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    fun  getViewModel(): ContactsViewModel {
        var viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        return viewModel
    }
}
