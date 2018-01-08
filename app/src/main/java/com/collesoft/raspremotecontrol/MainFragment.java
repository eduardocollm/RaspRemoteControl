package com.collesoft.raspremotecontrol;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.collesoft.raspremotecontrol.rest.client.MessageRouterClient;

import java.util.concurrent.ExecutionException;

/**
 * Created by eduardocoll on 1/8/18.
 */

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView messages = rootView.findViewById(R.id.mainMessages);

        ToggleButton onOffButton = rootView.findViewById(R.id.toggleButton);
        onOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggle, boolean isChecked) {
                String response;
                if (isChecked) {
                    Log.d(TAG, "send ON command");
                    response = sendOnCommand();
                } else {
                    Log.d(TAG, "send OFF command");
                    response = sendOffCommand();
                }
                displayMessage(messages, response);
            }
        });

        return rootView;
    }

    private void displayMessage(TextView messages, String response) {
        messages.setText(response);
    }

    private String sendOnCommand() {
        String response = sendMessage("please turn device on");
        return response.equalsIgnoreCase("success") ? "device is now ON" : "device failed to turn ON";
    }

    private String sendOffCommand() {
        String response = sendMessage("please turn device off");
        return response.equalsIgnoreCase("success") ? "device is now OFF" : "device failed to turn OFF";
    }

    public String sendMessage(String message) {
        HttpRequestTask asyncRestCall = new HttpRequestTask();
        String[] params = { message };
        try {
            return asyncRestCall.execute(params).get();
        } catch (Exception e) {
            return new StringBuilder("failed to send message ").append(e.getMessage()).toString();
        }
    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            String message = args[0];
            MessageRouterClient client = new MessageRouterClient();
            try {
                return client.sendMessage(message);
            } catch (Exception e) {
                Log.e(TAG, "call to REST service failed: " + e.getMessage());
                return "restcall failed";
            }
        }

    }
}
