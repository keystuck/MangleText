package com.example.mangletext.mangling

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/* Adapted from
     Copyright (c) 2019 Nathan Esquenazi
/ https://gist.github.com/nesquena/ed58f34791da00da9751
*/
open class OnSwipeListener(c: Context): View.OnTouchListener {

    companion object {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
    }

    private val gestureDetector: GestureDetector

    init{
        gestureDetector = GestureDetector(c, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener: GestureDetector.SimpleOnGestureListener(){

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val result = false
            try {
                val diffY = e2!!.getY() - e1!!.getY()
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if (diffY > 0){
                        onSwipeDown()

                    } else
                        onSwipeUp()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
            return result
        }
    }
    open fun onSwipeDown(){}
    open fun onSwipeUp() {}
}