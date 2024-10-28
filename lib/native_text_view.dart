import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeTextView extends StatefulWidget {
  const NativeTextView({super.key});

  @override
  State<NativeTextView> createState() => _NativeTextViewState();
}

class _NativeTextViewState extends State<NativeTextView> {
  int counter = 0;
  final String viewType = 'native-text-view';
  final MethodChannel _methodChannel =
      const MethodChannel('com.example.native_text_view');
  Map<String, dynamic> creationParams = {};

  @override
  void initState() {
    super.initState();

    creationParams = {
      'text': "Increment",
      'counter': "Counter: $counter",
    };

    _methodChannel.setMethodCallHandler((call) async {
      if (call.method == "updateCounter") {
        setState(() {
          counter = call.arguments as int;
          creationParams["counter"] = "Counter: $counter";
        });
      }
    });
  }

  void _incrementCounterInFlutter() {
    setState(() {
      counter++;
      creationParams["counter"] = "Counter: $counter";
    });

    _methodChannel.invokeMethod(
      "setCounter",
      {"counter": counter},
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(
            height: 200,
            width: double.infinity,
            child: AndroidView(
              viewType: viewType,
              layoutDirection: TextDirection.ltr,
              creationParams: creationParams,
              creationParamsCodec: const StandardMessageCodec(),
              onPlatformViewCreated: (int id) {
                debugPrint('PlatformView created: $id');
              },
            ),
          ),
          Column(
            children: [
              Text(creationParams["counter"]),
              ElevatedButton(
                onPressed: _incrementCounterInFlutter,
                child: const Text("Increment"),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
