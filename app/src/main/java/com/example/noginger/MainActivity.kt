package com.example.noginger

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noginger.Feature.MemberList.MemberDataSource
import com.example.noginger.Feature.MemberList.MembersAdapter
import com.example.noginger.Feature.RecipeSearch.RecipeSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val DIET = "com.example.noginger.DIET"
const val CANT_INCLUDE = "com.example.noginger.CANT_INCLUDE"

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var dataSource: MemberDataSource

    lateinit var adapter: MembersAdapter
    lateinit var membersView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        membersView = findViewById<View>(R.id.member_view) as RecyclerView
        adapter = MembersAdapter(emptyList())
        dataSource.getAllMembers().asLiveData(Dispatchers.Main).observe(this) {
            adapter.members = it
        }
        membersView.adapter = adapter
        membersView.layoutManager = LinearLayoutManager(this)

        val addPersonfab: View = findViewById(R.id.add_person_fab)
        addPersonfab.setOnClickListener {
            createNewAddPersonDialog()
        }

        val addMealfab: View = findViewById(R.id.add_meal_fab)
        addMealfab.setOnClickListener { view ->
            createNewMealDialog(membersView)
        }
    }

    private fun createNewAddPersonDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val addNewPersonView = layoutInflater.inflate(R.layout.add_person_dialog, null)

        dialogBuilder.setView(addNewPersonView)
        val dialog = dialogBuilder.create()
        dialog.show()

        val nameInput: EditText = addNewPersonView.findViewById(R.id.nameTextUserInput)
        val cantEatInput: EditText = addNewPersonView.findViewById(R.id.cantEatUserInput)

        val submitButton: Button = addNewPersonView.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            CoroutineScope(IO).launch {
                var dietInputCheckBoxes = ArrayList<CheckBox>()
                dietInputCheckBoxes.add(addNewPersonView.findViewById<CheckBox>(R.id.checkbox_gluten_free))
                dietInputCheckBoxes.add(addNewPersonView.findViewById<CheckBox>(R.id.checkbox_vegetarian))
                dietInputCheckBoxes.add(addNewPersonView.findViewById<CheckBox>(R.id.checkbox_vegan))
                dietInputCheckBoxes.add(addNewPersonView.findViewById<CheckBox>(R.id.checkbox_paleo))
                dietInputCheckBoxes.add(addNewPersonView.findViewById<CheckBox>(R.id.checkbox_low_fodmap))

                var memberDietaries = ArrayList<String>()
                for (i in 0 until dietInputCheckBoxes.size) {
                    if (dietInputCheckBoxes.get(i).isChecked) {
                        memberDietaries += dietInputCheckBoxes.get(i).text.toString()
                    }
                }
                val memberDietariesString = memberDietaries.joinToString(", ")
                addMember(nameInput.text.toString(), cantEatInput.text.toString(), memberDietariesString)
            }
            dialog.hide()
        }

        val cancelButton: Button = addNewPersonView.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.hide()
        }
    }

    private fun createNewMealDialog(membersView: RecyclerView) {
        var cantInclude = ArrayList<String>()
        var diet = ArrayList<String>()

        membersView.forEach { view ->
            if (view.findViewById<CheckBox>(R.id.member_select_button).isChecked) {
                cantInclude.add(view.findViewById<TextView>(R.id.member_cant_eat).text.toString())
                diet.add(view.findViewById<TextView>(R.id.member_diet).text.toString())
            }
        }

        val dialogBuilder = AlertDialog.Builder(this)
        val createNewMealView = layoutInflater.inflate(R.layout.create_meal_dialog, null)

        var cantIncludeString = cantInclude.joinToString(", ")
        var dietString = diet.joinToString(", ")

        if (cantIncludeString == null) {
            cantIncludeString = ""
        }
        if (dietString == null) {
            dietString = ""
        }

        val cantIncludeTextView = createNewMealView.findViewById<TextView>(R.id.dontIncludeTextView)
        cantIncludeTextView.setText(cantIncludeString)

        dialogBuilder.setView(createNewMealView)
        val dialog = dialogBuilder.create()
        dialog.show()

        val mealIdeasButton: Button = createNewMealView.findViewById(R.id.mealsButton)
        mealIdeasButton.setOnClickListener {
            val intent = Intent(this, RecipeSearchActivity::class.java).apply {
                putExtra(DIET, dietString)
                putExtra(CANT_INCLUDE, cantIncludeString)
            }
            startActivity(intent)
        }

        val closeButton: Button = createNewMealView.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.hide()
        }
    }

    suspend fun addMember(nameInput: String, cantEatInput: String?, dietInput: String?) {
        withContext(IO) {
            dataSource.insertMember(nameInput, cantEatInput, dietInput)
        }
    }
}