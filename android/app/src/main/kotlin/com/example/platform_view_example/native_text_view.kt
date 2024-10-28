package com.example.platform_view_example

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
            setPadding(16, 16, 16, 16)
            background = createBorderDrawable(context)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        titleTextView.apply {
            text = "Native Counter View"
            setTypeface(null, Typeface.BOLD)
            textSize = 24F
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(0, 0, 0, 16)
        }

        counterTextView.apply {
            text = "Counter: $counter"
            setTypeface(null, Typeface.BOLD)
            textSize = 30F
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(0, 0, 0, 20)
        }

        button.apply {
            text = creationParams?.get("buttonText") as? String ?: "Increment"
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

    private fun createBorderDrawable(context: Context): GradientDrawable {
        return GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, android.R.color.white))
            setStroke(4, ContextCompat.getColor(context, android.R.color.darker_gray))
            cornerRadius = 16f
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
