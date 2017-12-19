package br.com.softgran.agenda.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by GUIFA on 19/12/2017.
 */

public class AgendaMessaginService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> menssagem = remoteMessage.getData();

        Log.i("Mensagem:", String.valueOf(menssagem));
    }
}
