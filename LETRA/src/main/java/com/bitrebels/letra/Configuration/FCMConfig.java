package com.bitrebels.letra.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Configuration
public class FCMConfig {
    @PostConstruct
    public void initFCM() {

        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("FCMcredentials.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://letra-9d28d.firebaseio.com")
                    .build();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);


            System.out.println(firebaseApp);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
