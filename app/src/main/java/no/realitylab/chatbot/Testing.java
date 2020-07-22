package no.realitylab.chatbot;

import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import java.io.InputStream;
import java.util.UUID;

public class Testing extends AppCompatActivity {
SessionsClient sessionsClient;
SessionName session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);


       kud();



    }


    public  void kud()
    {
       // Initiate Dialogflow Agent

        try {
            InputStream stream = getResources().openRawResource(R.raw.chatbotcred);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId,UUID.randomUUID().toString()); // uuid = UUID.randomUUID().toString()
        } catch (Exception e) {
            e.printStackTrace();
        }

        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("Hi can i order a pizza?").setLanguageCode("en-US")).build();
        new RequestJavaV2Task(Testing.this, session, sessionsClient, queryInput).execute();

    }

    public void callbackV2(DetectIntentResponse response) {

        if (response != null) {

            // process aiResponse here

            String botReply = response.getQueryResult().getFulfillmentText();

            TextView tv=findViewById(R.id.fulfillmentText);
            tv.setText(botReply);

        } else {
            TextView tv=findViewById(R.id.fulfillmentText);
            tv.setText("There was some communication issue. Please Try again!");
            //showTextView("There was some communication issue. Please Try again!", BOT);

        }

    }

}