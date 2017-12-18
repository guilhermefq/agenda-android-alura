package br.com.softgran.agenda.firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by guilhermefq on 18/12/17.
 */

public class AgendaInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Toast.makeText(this, "Procurando token!", Toast.LENGTH_SHORT).show();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token firebase", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }


}
