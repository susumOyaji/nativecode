import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter NativeData Test'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _platformVersion = 'Unknown version level.';
  String _platformTelephony = 'Unknown telephony';
  String _platformBattery = 'Unknown battery Level';
  String _platformHangup = 'Unknown Connect';
  String _platformAnswer = 'Unknown Connect';
  String _platformTelephony_state = 'Unknown Connect';

  static const platform = const MethodChannel('samples.flutter.dev/battery');

  @override
  void initState() {
    super.initState();
    initPlatformState();
    _Androidphone_state();

    //_getBatteryLevel();
    //initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    //Future<void> _getBatteryLevel() async {
    // Get battery level.
    String batteryLevel;
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      batteryLevel = 'Battery level at $result % .';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    setState(() {
      _platformBattery = batteryLevel;
    });
    //}

    //Future<void> _getPiatformVersion() async {
    // Get battery level.
    String platformVersion;
    try {
      final String result = await platform.invokeMethod('getPlatformVersion');
      platformVersion = result;
    } on PlatformException catch (e) {
      platformVersion = "Failed to get android version: '${e.message}'.";
    }

    setState(() {
      _platformVersion = platformVersion;
    });
    //}
  }

  Future<void> _Androidphone_state() async {
    // Get battery level.
    String formTelephony_state;

    try {
      final String result =
          await platform.invokeMethod('Androidphone_getState');
      formTelephony_state = result;
    } on PlatformException catch (e) {
      formTelephony_state = "Failed : '${e.message}'.";
    }
    setState(() {
      _platformTelephony_state = formTelephony_state;
    });
  }

  Future<void> _getAndroidphone() async {
    // Get battery level.
    String platformTelephony;

    try {
      final String result =
          await platform.invokeMethod('getAndroidphone', "1345678");
      platformTelephony = result;
    } on PlatformException catch (e) {
      platformTelephony = "Failed : '${e.message}'.";
    }

    setState(() {
      _platformTelephony = platformTelephony;
    });
  }

  Future<void> _getPiatformAnswer() async {
    // Get battery level.
    String platformAnswer;

    try {
      final bool result = await platform.invokeMethod('answer', true);
      platformAnswer = result.toString();
      print(platformAnswer);
    } on PlatformException catch (e) {
      platformAnswer = "Failed : '${e.message}'.";
    }

    setState(() {
      _platformAnswer = platformAnswer;
    });
  }

  final EventChannel _channel =
      const EventChannel('samples.flutter.dev/battery');
  Stream<int> _onVolumeChanged;

  Stream<int> get onVolumeChanged {
    if (_onVolumeChanged == null) {
      _onVolumeChanged = _channel
          .receiveBroadcastStream()
          .map((dynamic event) => event as int);
    }
    return _onVolumeChanged;
  }
//Streamは保持する。
//新たにStreamを生成すると以前
//生成したStreamはCancelされる。
//シングルトンにするのはStreamを複数生成させないため。

  Future<void> _getPiatformHagup() async {
    // Get battery level.
    String platformHangup;

    try {
      final bool result = await platform.invokeMethod('hangup', true);
      platformHangup = result.toString();
      print(platformHangup);
    } on PlatformException catch (e) {
      platformHangup = "Failed : '${e.message}'.";
    }

    setState(() {
      _platformHangup = platformHangup;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            //RaisedButton(
            //  child: Text('Get Battery Level'),
            //  onPressed: //_getBatteryLevel,
            //),
            Text(_platformBattery),

            Text(_platformVersion),
            RaisedButton(
              child: Text(_platformTelephony_state),
              onPressed: _getAndroidphone,
            ),
            //Text(_platformTelephony),
            RaisedButton(
              child: Text('Answer the pnone'),
              onPressed: _getPiatformAnswer,
            ),
            Text(_platformAnswer),
            RaisedButton(
              child: Text('HangUp to Redial the pnone'),
              onPressed: _getPiatformHagup,
            ),
            Text(_platformHangup),

            //Center(child: Text('Running on Version: $_platformVersion\n')),
            //Center(child: Text('Running on Battery: $_platformBattery\n')),
            //Center(child: Text('Running on Telephony: $_platformTelephony\n')),
          ],
        ),
      ),
    );
  }
}
