package com.example.platform_view_example

import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        Log.d("MainActivity", "Registering NativeTextViewFactory")
        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory(
                "native-text-view",
                NativeTextViewFactory(flutterEngine.dartExecutor.binaryMessenger)
            )
    }
}