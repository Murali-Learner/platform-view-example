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
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 15.0),
        child: Column(
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
            const SizedBox(height: 20),
            Container(
              width: double.infinity,
              alignment: Alignment.center,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: const Color(0xFFF0F0F0),
                border: Border.all(
                  color: Colors.blue,
                  width: 1,
                ),
                borderRadius: BorderRadius.circular(7),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Text(
                    "Flutter Counter View",
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 15),
                  Text(
                    "Counter: $counter",
                    style: const TextStyle(
                      fontSize: 25,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1E88E5),
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 15),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blue,
                      padding: const EdgeInsets.symmetric(
                          horizontal: 10, vertical: 10),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(4),
                      ),
                      elevation: 2,
                    ),
                    onPressed: _incrementCounterInFlutter,
                    child: const Text(
                      "Increment",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
