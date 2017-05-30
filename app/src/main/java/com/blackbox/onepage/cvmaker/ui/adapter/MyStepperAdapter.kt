package com.blackbox.onepage.cvmaker.ui.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.ui.fragments.BasicFragment
import com.blackbox.onepage.cvmaker.ui.fragments.ContactFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


/**
 * Created by umair on 30/05/2017.
 */
class MyStepperAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {


    val CURRENT_STEP_POSITION_KEY: String = "position"

    override fun createStep(position: Int): Step {

        var step: Step
        val b = Bundle()

        when (position) {
            0 -> step = BasicFragment()

            1 -> step = ContactFragment()

            else -> throw IllegalArgumentException("Unsupported position: " + position)
        }
        b.putInt(CURRENT_STEP_POSITION_KEY, position)
        //step.setArguments(b)
        return step
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel(position: Int): StepViewModel {
        val builder = StepViewModel.Builder(context)
                .setTitle("TAB")
        when (position) {
            0 -> builder
                    .setNextButtonLabel("Next")
                    .setBackButtonLabel("Cancel")
                    .setNextButtonEndDrawableResId(R.drawable.ic_right_arrow)
                    .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE)
            1 -> builder
                    .setNextButtonLabel("Next")
                    .setBackButtonLabel("Go to first")
                    .setBackButtonStartDrawableResId(R.drawable.ic_left_arrow)

            else -> throw IllegalArgumentException("Unsupported position: " + position)
        }
        return builder.create()
    }
}