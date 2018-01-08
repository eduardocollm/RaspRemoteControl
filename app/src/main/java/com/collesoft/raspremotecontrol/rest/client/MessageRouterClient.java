package com.collesoft.raspremotecontrol.rest.client;

import android.util.Log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eduardocoll on 12/26/17.
 */

public class MessageRouterClient {

    public static final String TAG = "MessageRouterClient";

    private static final String url = "http://192.168.0.62:8080/message";

    private static final String target = "LOGGER";

    private class MessageRouterRequest {
        String target;
        String message;
    }

    private static class MessageRouterResponse {
        String response;
        MessageRouterResponse() {
            super();
        }
    }

    public String sendMessage(String message) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        MessageRouterRequest request = new MessageRouterRequest();
        request.target = target;
        request.message = message;
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Log.d(TAG, "sending message to message router");
        ResponseEntity<MessageRouterResponse> response = restTemplate.postForEntity(url, request, MessageRouterResponse.class);

        if (response != null) {
            return response.getStatusCode().equals(HttpStatus.OK) ? "success" : "fail";
        } else {
            Log.e(TAG, "rest call failed: null response");
            return "fail";
        }
    }

}
