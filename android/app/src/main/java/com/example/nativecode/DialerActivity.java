package com.example.nativecode;

import android.os.Bundle;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.embedding.engine.FlutterEngine;
//import io.flutter.app.FlutterActivity;
import io.flutter.embedding.android.FlutterActivity;//推奨

//import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;

//import android.support.v4.app.ActivityCompat;
import androidx.core.app.ActivityCompat;

//import android.support.v4.app.FragmentActivity;
import androidx.fragment.app.FragmentActivity;

//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.telecom.TelecomManager;
import android.telecom.InCallService;
import android.widget.EditText;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;
import com.example.nativecode.R;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.ContextWrapper;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;




import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;





public class DialerActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/battery";
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    private static  final String EXTRA_STRING = "extra_string";
    CallActivity callActivity = new CallActivity();
     
    //EditText phoneNumberInput;
    String parameters;
    TelecomManager telecomManager;

    private static final int REQUEST_PERMISSION = 0;
    static final int REQUEST_CODE = 1;
    private static final int REQUEST_ID = 1;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);    
        GeneratedPluginRegistrant.registerWith(new FlutterEngine(this));


        
        
        
        //ユーザがDialerユーザインターフェイスを経由せずに通話を開始し、通話を確認することをアプリケーションに許可します。
        //ActivityCompat.requestPermissions(this, new String[]{
        //    Manifest.permission.CALL_PHONE
        //}, REQUEST_CODE);

        //setContentView(R.layout.activity_dialer);
      
        //phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);
        
        //get Intent data (tel number)
        //if (getIntent().getData() != null)
        //  phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());
        
     

          
      new App();
      Toast.makeText(DialerActivity.this, "Started the DialerActivity.app", Toast.LENGTH_SHORT).show();
      new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
        new MethodCallHandler() {
                  @Override
                  public void onMethodCall(MethodCall call, Result result) {//TODO
                  Toast.makeText(DialerActivity.this, "Started theMethodChannel ", Toast.LENGTH_SHORT).show();
                  if (call.method.equals("getAndroidphone")) {
                      Toast.makeText(DialerActivity.this, "Started getAndroidphone!!! ", Toast.LENGTH_SHORT).show();
                      
                      // invokeMethod(Dart)の第二引数で指定したパラメータを取得できます
                      parameters = call.arguments.toString();
                      String phonestate = makeCall(parameters);
                      Toast.makeText(DialerActivity.this, "Started theMethodChannel to makeCall", Toast.LENGTH_SHORT).show();
                      
                      if (phonestate != null) {
                        result.success(phonestate);//return to Flutter
                      } else {
                        result.error("UNAVAILABLE", "Dialer-AndroidPhone not available.", null);
                      }
                    } else if (call.method.equals("hangup")) {
                      Toast.makeText(DialerActivity.this, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show();
                      // invokeMethod(Dart)の第二引数で指定したパラメータを取得できます
                      boolean hangupparameters = (boolean)call.arguments;
                      boolean returnhangup = hangup(hangupparameters);
              
                      if (returnhangup != true) {
                        result.success(returnhangup);
                      } else {
                        result.error("UNAVAILABLE", "Hangup not available.", null);
                      }
                    } else if (call.method.equals("answer")) {
                      Toast.makeText(DialerActivity.this, "Started theMethodChannel to Anser ", Toast.LENGTH_SHORT).show();
                      // invokeMethod(Dart)の第二引数で指定したパラメータを取得できます
                      boolean answerparameters = (boolean)call.arguments;
                      boolean returnanser = answer(answerparameters);
              
                      if (returnanser != true) {
                        result.success(returnanser);
                      } else {
                        result.error("UNAVAILABLE", "Anser not available.", null);
                      }        
                    } else if (call.method.equals("getBatteryLevel")) {
                      int batteryLevel = getBatteryLevel();
                      if (batteryLevel != -1) {
                        result.success(batteryLevel);
                      } else {
                        result.error("UNAVAILABLE", "Battery level not available.", null);
                      }
                    } else if (call.method.equals("getPlatformVersion")) {
                      result.success("Android " + android.os.Build.VERSION.RELEASE);                    
                    } else {
                      result.notImplemented();//該当するメソッドが実装されていない
                    } // TOD
                  }
              }
        );
    }

    @Override
    public void onStart() {
      super.onStart();
      offerReplacingDefaultDialer(); 
    }



    
  

    




  private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).
          registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
          intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    return batteryLevel;
  }






    @SuppressLint("MissingPermission")
    public String makeCall(String _phone) {
      Toast.makeText(DialerActivity.this, "Started makeCall!!! ", Toast.LENGTH_SHORT).show();  
        // If permission to call is granted
        if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED) {
        // Create the Uri from phoneNumberInput
          Uri uri = Uri.parse("tel:"+ _phone);
            
            // Start call to the number in input
            //別のアクティビティを開始する 
            //Intent は、異なるコンポーネント（2 つのアクティビティなど）間で実行時バインディングを付与するオブジェクトです。
            //Intent はアプリの「何かを行うという意図」を表します。
            //インテントは幅広いタスクで使用できますが、このレッスンでは別のアクティビティを開始するために使われます
            //ACTION_CALL で電話番号を投げるといきなり発信を開始します。
            //直接電話をかけるには、ACTION_CALL アクションを使用し、以下に定義されている
            //URI スキームを使って電話番号を指定します。
            //電話アプリが開くと呼び出しが始まります。ユーザーが通話ボタンを押す必要はありません。
            //startActivity(new Intent(Intent.ACTION_CALL, uri));
            Intent intent = new Intent(Intent.ACTION_CALL)//暗黙的インテント
                    .setData(Uri.parse("tel:0123456789"));
            try {
              startActivity(intent);
            } catch (SecurityException e){
              Toast.makeText(DialerActivity.this,"Call Error",Toast.LENGTH_SHORT).show();
            }
             
            //startActivity(new Intent(Intent.ACTION_CALL, uri));       
        } else {
            // Request permission to call
            ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, REQUEST_PERMISSION);
        }
             
        String tv = callActivity.PhoneState;
        Toast.makeText(DialerActivity.this, "makeCall  CallActivity.PhoneState" + tv , Toast.LENGTH_SHORT).show();
        
        return tv;
    }




    

   
    public boolean hangup(boolean hangup) {
      Toast.makeText(DialerActivity.this, "hangup to True ", Toast.LENGTH_SHORT).show();
      //CallActivity.onHangup();
      OngoingCall.hangup();// call.disconnect();
      return true;
    }

    public boolean answer(boolean hangup) {
      Toast.makeText(DialerActivity.this, "answer to True ", Toast.LENGTH_SHORT).show();
      //CallActivity.onHangup();
      OngoingCall.answer();// call.disconnect();
      return true;
    }


    //ユーザーが自分のアプリをデフォルトの電話アプリとして設定する
    private void offerReplacingDefaultDialer() {
      Toast.makeText(DialerActivity.this, "offerReplacingDefaultDialer", Toast.LENGTH_SHORT).show();

        TelecomManager systemService = this.getSystemService(TelecomManager.class);

        if (!getPackageName().equals(systemService.getDefaultDialerPackage())) {
            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }

        //if (systemService != null && !systemService.getDefaultDialerPackage().equals(this.getPackageName())) {
        //  startActivity((new Intent(ACTION_CHANGE_DEFAULT_DIALER)).putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName()));
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        Toast.makeText(DialerActivity.this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        
        ArrayList<Integer> grantRes = new ArrayList<>();
        // Add every result to the array
        for (Integer grantResult: grantResults) grantRes.add(grantResult);
        
        if (requestCode == REQUEST_PERMISSION && grantRes.contains(PERMISSION_GRANTED)) {
            makeCall(parameters);
        }

    }



    

   


     


}
