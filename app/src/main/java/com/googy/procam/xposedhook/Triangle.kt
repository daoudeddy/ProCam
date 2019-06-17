package com.googy.procam.xposedhook

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * Created by ginev on 10/04/2014.
 *
 *
 * A custom drawable class that draws a isosceles triangle using the
 * bottom edge of the bounds of this drawable as a base and the centre of the top edge of
 * the bounds as
 * .
 */
class Triangle(private val rotation: Int) : Drawable() {

    private val paint: Paint
    var strokeWidth = 0f
    var strokeColor = Color.TRANSPARENT
    var color = Color.WHITE
    private var alpha: Int = 0

    init {
        this.paint = Paint()
    }

    override fun getOpacity(): Int {
        return 1
    }

    override fun draw(canvas: Canvas) {
        val bounds = this.bounds

        val triangle = Path()
        triangle.moveTo(bounds.left.toFloat(), bounds.top.toFloat())
        triangle.lineTo(bounds.right.toFloat(), bounds.top.toFloat())
        triangle.lineTo((bounds.left + (bounds.right - bounds.left) / 2).toFloat(), bounds.bottom.toFloat())
        triangle.lineTo(bounds.left.toFloat(), bounds.top.toFloat())

        canvas.save(); //Saving the canvas and later restoring it so only this image will be rotated.
        canvas.rotate(rotation.toFloat())

        this.paint.color = this.color
        this.paint.style = Paint.Style.FILL
        canvas.drawPath(triangle, this.paint)
        this.paint.style = Paint.Style.STROKE
        this.paint.color = this.strokeColor
        this.paint.strokeWidth = this.strokeWidth
        this.paint.isAntiAlias = true
        canvas.drawPath(triangle, this.paint)
        canvas.restore()
    }


    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    override fun getAlpha(): Int {
        return this.alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {

    }
}