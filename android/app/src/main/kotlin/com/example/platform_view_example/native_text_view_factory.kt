package com.example.platform_view_example

import android.content.Context
import android.util.Log
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class NativeTextViewFactory(private val messenger: BinaryMessenger) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val methodChannel = MethodChannel(messenger, "com.example.native_text_view")

    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        Log.d("Factory", "Native TextView Factory $args")
        val creationParams = args as? Map<String?, Any?>?
        return NativeTextView(context!!, viewId, creationParams, methodChannel)
    }
}

