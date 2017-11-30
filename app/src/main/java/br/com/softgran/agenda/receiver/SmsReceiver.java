package br.com.softgran.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.softgran.agenda.R;
import br.com.softgran.agenda.dao.ContatoDAO;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];

        String format = (String) intent.getSerializableExtra("format");

        SmsMessage message = SmsMessage.createFromPdu(pdu, format);
        String telefone = message.getDisplayOriginatingAddress();

        ContatoDAO dao = new ContatoDAO(context);

        if(dao.ehContato(telefone)){
            Toast.makeText(context, "Chegou um SMS de um Contato!", Toast.LENGTH_SHORT).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }


}
