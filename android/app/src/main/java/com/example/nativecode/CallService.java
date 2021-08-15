package com.example.nativecode;

import android.telecom.Call;
import android.telecom.InCallService;
import android.widget.Toast;

public class CallService extends InCallService {
    @Override
    public void onCallAdded(Call call) {
        new OngoingCall().setCall(call);
        Toast.makeText(this, "class to CallService", Toast.LENGTH_SHORT).show();
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        new OngoingCall().setCall(null);
    }
}
