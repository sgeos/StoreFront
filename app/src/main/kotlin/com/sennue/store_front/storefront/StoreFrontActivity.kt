package com.sennue.store_front.storefront

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow

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
        fillTable()
    }

    fun fillTable() {
        val table = findViewById(R.id.inventoryTable) as TableLayout
        table.removeAllViews()

        addRow(table)
        addRow(table)
        addRow(table)
    }

    fun addRow(table : TableLayout) {
        val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tableRow = inflater.inflate(R.layout.inventory_row, null) as TableRow

        val itemLeft = tableRow.findViewById(R.id.inventoryItemLeft) as LinearLayout
        itemLeft.id = 0
        val itemRight = tableRow.findViewById(R.id.inventoryItemRight) as LinearLayout
        itemRight.id = 1

        table.addView(tableRow)
    }
}
