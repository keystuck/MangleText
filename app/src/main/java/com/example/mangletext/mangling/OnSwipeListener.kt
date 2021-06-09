package com.example.mangletext.mangling

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class OnSwipeListener: View.OnTouchListener {

    private lateinit var gestureDetector: GestureDetector

    fun OnSwipeListener(c: Context){
        gestureDetector = GestureDetector(c, GestureListener)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private class GestureListener: GestureDetector.SimpleOnGestureListener{
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                var diffY = e2!!.getY() - e1!!.getY()
                var diffX = e2!!.getX() - e1!!.getX()
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if (diffY > 0){
                        onSwipeDown()
                    } else
                        onSwipeUp()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
}