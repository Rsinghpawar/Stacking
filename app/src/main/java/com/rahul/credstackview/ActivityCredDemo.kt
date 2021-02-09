package com.rahul.credstackview

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rahul.credstackview.databinding.*
import java.util.*
import kotlin.math.abs


class ActivityCredDemo : AppCompatActivity() {
    private var switch: Boolean = true
    private var mealTypeChip: String = ""
    private var dietTypeChip: String = ""
    private lateinit var sheet1: BottomSheetBehavior<LinearLayout>
    private lateinit var sheet2: BottomSheetBehavior<LinearLayout>
    private lateinit var sheet3: BottomSheetBehavior<LinearLayout>
    private lateinit var binding: ActivityCredDemoBinding
    lateinit var vpAdapter: VPAdapter
    private lateinit var sheet1Binding: Sheet1Binding
    private lateinit var sheet2Binding: Sheet2Binding
    private lateinit var sheet3Binding: Sheet3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCredDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSheets()
        getChipValues()
        setClickListeners()
        setSheetStateChangeListeners()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        vpAdapter = VPAdapter()
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))
            page.alpha = 0.25f + (1 - abs(position))
        }
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.sheetTwo.vpSheet2.apply {
            adapter = vpAdapter
            offscreenPageLimit = 1
            setPageTransformer(pageTransformer)
            addItemDecoration(itemDecoration)

        }


    }

    private fun getChipValues() {

        sheet1Binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString()
            mealTypeChip = selectedMealType
            buttonColorListener(sheet1Binding.btnSheet1)
        }
        sheet1Binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            buttonColorListener(sheet1Binding.btnSheet1)
        }

    }

    private fun buttonColorListener(view: ImageView) {
        if (mealTypeChip.isNotBlank() && dietTypeChip.isNotBlank() && switch) {
            switch = false
            (view.background as TransitionDrawable).startTransition(500)
        }
    }


    override fun onBackPressed() {
        when {
            sheet3.state == BottomSheetBehavior.STATE_EXPANDED -> {
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            sheet1.state == BottomSheetBehavior.STATE_SETTLING -> {
                sheet1.state = BottomSheetBehavior.STATE_COLLAPSED
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
        binding.btnShowSheet1.setOnClickListener {
            if (sheet1.state == BottomSheetBehavior.STATE_COLLAPSED)
                sheet1.state = BottomSheetBehavior.STATE_EXPANDED

            binding.constraintLayout.transitionToStart()
        }

        sheet1Binding.btnSheet1.setOnClickListener {
            if (mealTypeChip.isNotBlank() && dietTypeChip.isNotBlank()) {
                sheet2.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                Toast.makeText(this, "Please choose Meal and Diet Type", Toast.LENGTH_SHORT).show()
            }
        }

        sheet2Binding.btnSheet2.setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_COLLAPSED)
                sheet3.state = BottomSheetBehavior.STATE_EXPANDED
        }

        sheet3Binding.btnSheet3.setOnClickListener {
            Toast.makeText(this, "Payment method is not integrated 😅", Toast.LENGTH_SHORT).show()
        }

        sheet2Binding.viewGetSheet1ClickSheet2.setOnClickListener {
            if (sheet2.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet2.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        sheet3Binding.viewGetSheet1ClickSheet3.setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
            if (sheet2.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet2.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        sheet3Binding.viewGetSheet2ClickSheet3.setOnClickListener {
            if (sheet3.state == BottomSheetBehavior.STATE_EXPANDED)
                sheet3.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setSheetStateChangeListeners() {
        sheet1.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_SETTLING , BottomSheetBehavior.STATE_COLLAPSED -> binding.constraintLayout.transitionToEnd()

                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        sheet2.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    if (dietTypeChip.isNotBlank())
                        sheet1Binding.tvDietResult.text = dietTypeChip
                    if (mealTypeChip.isNotBlank())
                        sheet1Binding.tvMealResult.text = mealTypeChip

                    sheet1Binding.viewSheet1.transitionToEnd()
                } else if (newState == BottomSheetBehavior.STATE_SETTLING)
                    sheet1Binding.viewSheet1.transitionToStart()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        sheet3.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    val plates = (sheet2Binding.vpSheet2.currentItem + 1)
                    sheet3Binding.apply {
                        tvPrice.text =
                            getString(R.string.plates_price, String.format("%.2f", plates * 200.45))
                        constraintLayout2.transitionToEnd()

                    }
                    sheet2Binding.apply {
                        txtSelectedMeal.text = getString(R.string.plates_quantity, plates)
                        viewSheet2.transitionToEnd()
                    }

                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    sheet2Binding.apply {
                        viewSheet2.transitionToStart()
                    }
                    sheet3Binding.constraintLayout2.transitionToStart()

                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    private fun initSheets() {
        sheet1Binding = binding.sheetOne
        sheet2Binding = binding.sheetTwo
        sheet3Binding = binding.sheetThree
        sheet1 = BottomSheetBehavior.from(sheet1Binding.bottomSheet1)
        sheet2 = BottomSheetBehavior.from(sheet2Binding.bottomSheet2)
        sheet3 = BottomSheetBehavior.from(sheet3Binding.bottomSheet3)
    }


}