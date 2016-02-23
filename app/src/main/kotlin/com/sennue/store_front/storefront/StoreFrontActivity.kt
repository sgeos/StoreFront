package com.sennue.store_front.storefront

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.exceptions.OnErrorThrowable
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.text.NumberFormat
import java.util.*

class StoreFrontActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_front)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_store_front, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        fillTable(R.raw.male)
    }

    fun fillTable(@RawRes id : Int) {

        val fileStream : InputStream = resources.openRawResource(id)
        val reader : BufferedReader = BufferedReader(InputStreamReader(fileStream, "UTF8"))
        val gson : Gson = Gson()
        val response = gson.fromJson<InventoryResponse>(reader)


        val table = findViewById(R.id.inventoryTable) as TableLayout
        table.removeAllViews()

        var i = 0
        while (i < response.data.size) {
            val tableRow = addRow(table)
            displayInventory(tableRow, R.id.inventoryItemLeft, response.data[i++])
            if (i < response.data.size) {
                displayInventory(tableRow, R.id.inventoryItemRight, response.data[i++])
            }
        }
    }

    fun addRow(table : TableLayout) : TableRow {
        val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tableRow = inflater.inflate(R.layout.inventory_row, null) as TableRow
        table.addView(tableRow)
        return tableRow
    }

    fun displayInventory(tableRow : TableRow, @RawRes id : Int, item : InventoryItem) {
        val itemView = tableRow.findViewById(id) as LinearLayout
        val image : TextView = itemView.findViewById(R.id.inventoryImage) as TextView
        image.height = image.width
        image.text = item.name
        val comments : TextView = itemView.findViewById(R.id.inventoryCommentText) as TextView
        comments.text = "${item.comments} "
        val likes : TextView = itemView.findViewById(R.id.inventoryLikeText) as TextView
        likes.text = "${item.likes} "
        val price : TextView = itemView.findViewById(R.id.inventoryPriceText) as TextView
        price.text = "$ ${NumberFormat.getNumberInstance(Locale.US).format(item.price)} "

        rx.Observable.just(URL(item.photo))
                .map(Func1<URL, Drawable> { url ->
                    var bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    return@Func1 BitmapDrawable(resources, bitmap)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Subscriber<Drawable>() {
                    override fun onNext(drawable : Drawable?) {
                        image.background = drawable
                    }

                    override fun onError(error: Throwable) {
                        System.err.println("Error: " + error.message)
                    }

                    override fun onCompleted() {
                    }
                }
                )
    }
}
