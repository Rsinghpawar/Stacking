package com.rahul.credstackview

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


class MainActivity : AppCompatActivity() {
    private var switch: Boolean = true
    private var mealTypeChip: String = ""
    private var dietTypeChip: String = ""
    private lateinit var sheet1: BottomSheetBehavior<LinearLayout>
    private lateinit var sheet2: BottomSheetBehavior<LinearLayout>
    private lateinit var sheet3: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cred_demo)
        initSheets()
        getChipValues()
        setClickListeners()
        setSheetStateChangeListeners()
//        findViewById<CoordinatorLayout>(R.id.col_root).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun getChipValues() {
        findViewById<ChipGroup>(R.id.mealType_chipGroup).setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString()
            mealTypeChip = selectedMealType
            buttonColorListener()
        }
        findViewById<ChipGroup>(R.id.dietType_chipGroup).setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            buttonColorListener()
        }

    }

    private fun buttonColorListener() {
        if (mealTypeChip.isNotBlank() && dietTypeChip.isNotBlank() && switch) {
            switch = false
            (findViewById<ImageView>(R.id.btn_sheet_1).background as TransitionDrawable).startTransition(
                300
            )

        }
    }

    override fun onBackPressed() {
        when {
            sheet3.state == BottomSheetBehavior.STATE_EXPANDED -> {
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            sheet2.state == BottomSheetBehavior.STATE_EXPANDED -> {
                sheet2.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            sheet1.state == BottomSheetBehavior.STATE_EXPANDED -> {
                sheet1.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            else -> super.onBackPressed()
        }
    }

    private fun setClickListeners() {
        findViewById<AppCompatButton>(R.id.btn_show_sheet_1).setOnClickListener {
            if (sheet1.state == BottomSheetBehavior.STATE_COLLAPSED)
                sheet1.state = BottomSheetBehavior.STATE_EXPANDED
        }

        findViewById<ImageView>(R.id.btn_sheet_1).setOnClickListener {
            if (mealTypeChip.isNotBlank() && dietTypeChip.isNotBlank()) {
                sheet2.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                Toast.makeText(this, "Please choose Meal and Diet Type", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.btn_sheet_2).setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_COLLAPSED)
                sheet3.state = BottomSheetBehavior.STATE_EXPANDED
        }

        findViewById<ImageView>(R.id.btn_sheet_3).setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        findViewById<View>(R.id.view_get_sheet_1_click_sheet2).setOnClickListener {
            if (sheet2.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet2.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        findViewById<View>(R.id.view_get_sheet_1_click_sheet3).setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
            if (sheet2.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet2.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        findViewById<View>(R.id.view_get_sheet_2_click_sheet3).setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setSheetStateChangeListeners() {
        sheet1.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    findViewById<View>(R.id.view_root).visibility = View.VISIBLE
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    findViewById<View>(R.id.view_root).visibility = View.GONE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        sheet2.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    if (dietTypeChip.isNotBlank())
                        findViewById<TextView>(R.id.tv_diet_result).text = dietTypeChip
                    if (mealTypeChip.isNotBlank())
                        findViewById<TextView>(R.id.tv_meal_result).text = mealTypeChip

                    findViewById<View>(R.id.view_sheet_1).apply {
                        toggleOn(this)
                    }
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    findViewById<View>(R.id.view_sheet_1).apply {
                        toggleOff(this)
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        sheet3.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    findViewById<View>(R.id.view_sheet_2).visibility = View.VISIBLE
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    findViewById<View>(R.id.view_sheet_2).visibility = View.GONE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    private fun initSheets() {
        sheet1 = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_1))
        sheet2 = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_2))
        sheet3 = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_3))
    }

    private fun toggleOn(view: View) {
        val transition: TransitionSet = TransitionSet().apply {
            addTransition(Fade())
            addTransition(Slide())
        }
        transition.apply {
            duration = 500
            addTarget(view)
            TransitionManager.beginDelayedTransition(view as ViewGroup)
        }

        view.visibility = View.VISIBLE
    }

    private fun toggleOff(view: View) {
        val transition: TransitionSet = TransitionSet().apply {
            addTransition(Fade())
            addTransition(Slide())
        }
        transition.apply {
            duration = 500
            addTarget(view)
            TransitionManager.beginDelayedTransition(view as ViewGroup)
        }
        view.visibility = View.GONE
    }
}