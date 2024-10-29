package com.example.platform_view_example

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

class NativeTextView(
    context: Context,
    id: Int,
    creationParams: Map<String?, Any?>?,
    private val methodChannel: MethodChannel
) : PlatformView {

    private val linearLayout: LinearLayout = LinearLayout(context)
    private val titleTextView: TextView = TextView(context)
    private val counterTextView: TextView = TextView(context)
    private val button: Button = Button(context)
    private var counter: Int = creationParams?.get("counter")?.toString()?.toIntOrNull() ?: 0

    init {

        linearLayout.apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
            background = createBorderDrawable()
        }

        titleTextView.apply {
            text = "Native Counter View"
            setTypeface(null, Typeface.BOLD)
            textSize = 24F
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setTextColor(Color.parseColor("#333333"))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 0, 0, 16)
        }

        counterTextView.apply {
            text = "Counter: $counter"
            setTypeface(null, Typeface.BOLD)
            textSize = 25F
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setTextColor(Color.parseColor("#1E88E5"))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 0, 0, 20)
        }

        button.apply {
            text = "Increment"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#1E88E5"))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(25, 25, 25, 25)
            setOnClickListener { incrementCounter() }
        }

        linearLayout.apply {
            addView(titleTextView)
            addView(counterTextView)
            addView(button)
        }

        methodChannel.setMethodCallHandler { call, result ->
            if (call.method == "setCounter") {
                counter = call.argument<Int>("counter") ?: 0
                updateCounterText()
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun createBorderDrawable(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor("#FFFFFF"))
            setStroke(2, Color.parseColor("#5b8fe3"))
            cornerRadius = 10f
        }
    }

    private fun incrementCounter() {
        counter++
        updateCounterText()

        methodChannel.invokeMethod("updateCounter", counter)
    }

    private fun updateCounterText() {
        counterTextView.text = "Counter: $counter"
    }

    override fun getView(): View = linearLayout
    override fun dispose() {}
}
