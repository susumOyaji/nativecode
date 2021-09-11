package com.example.nativecode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import android.widget.LinearLayout;

public class CallActivity extends  Activity {

    private CompositeDisposable disposables = new CompositeDisposable();
    private String number;
    private static Button answer, hangup;
    private static TextView callInfo;
    public  static String PhoneState;
    private CallStateString callstatestring;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(CallActivity.this, "CallActivity@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
        



        // R.layout.activity_call を渡して表示する
        setContentView(R.layout.activity_noncall);
        answer = findViewById(R.id.answer);
        hangup = findViewById(R.id.hangup);
        callInfo = findViewById(R.id.callInfo);
       
        number = getIntent().getData().getSchemeSpecificPart();
    }




    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();

        answer.setOnClickListener(v -> OngoingCall.answer());
        hangup.setOnClickListener(v -> OngoingCall.hangup());

        // Subscribe to state change -> call updateUi when change    
        new OngoingCall();
        Disposable disposable = OngoingCall.state.subscribe(this::updateUi);
        disposables.add(disposable);
        
        // Subscribe to state change (only when disconnected) -> call finish to close phone call
        new OngoingCall();
        Disposable disposable2 = OngoingCall.state
                .filter(state -> state == Call.STATE_DISCONNECTED)
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe(this::finish);

        disposables.add(disposable2);
       
    }

    // Call to Activity finish
    void finish(Integer state){
        finish();
    }


    // Set the UI for the call
    @SuppressLint("SetTextI18n")
    public void updateUi(Integer state) {
                
        // Set callInfo text by the state
        callInfo.setText(CallStateString.asString(state).toLowerCase() +"   "+number);
        PhoneState = callInfo.getText().toString();
        

        if (state == Call.STATE_RINGING){
            answer.setVisibility(View.VISIBLE);// ボタンを表示する
        }else{
            answer.setVisibility(View.GONE);// ボタンを非表示にする
        }
            
        if (state == Call.STATE_DIALING || state == Call.STATE_RINGING || state == Call.STATE_ACTIVE){
            hangup.setVisibility(View.VISIBLE);// ボタンを表示する
        }else{
            hangup.setVisibility(View.GONE);// ボタンを非表示にする
        }    
        //Toast.makeText(CallActivity.this, "updateUi state    " + state, Toast.LENGTH_SHORT).show();
        //Toast.makeText(CallActivity.this, "state    " + callstatestring.asString(state), Toast.LENGTH_SHORT).show(); 
        //return PhoneState;//null;  
    }

    public static String phoneState(){
        return PhoneState;
    }


    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
    }

    @SuppressLint("NewApi")
    public static void start(Context context, Call call) {
        context.startActivity(new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle()));
    }
 

   


}
