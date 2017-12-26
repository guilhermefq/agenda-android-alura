package br.com.softgran.agenda.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.dto.ContatoSync;
import br.com.softgran.agenda.event.AtualizaListaContatoEvent;

/**
 * Created by GUIFA on 19/12/2017.
 */

public class AgendaMessaginService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> menssagem = remoteMessage.getData();

        Log.i("Mensagem:", String.valueOf(menssagem));

        converteParaContato(menssagem);
    }

    private void converteParaContato(Map<String, String> menssagem) {
        String chaveDeAcesso = "alunoSync";
        if(menssagem.containsKey(chaveDeAcesso)) {//Verifica se a chave está dentro do Map
            String json = menssagem.get(chaveDeAcesso); //Extrai a informação do Map, que é um Json
            ObjectMapper mapper = new ObjectMapper(); //Objeto do Jackson que faz a desserialização do json para um objeto
            try {
                ContatoSync contatoSync = mapper.readValue(json, ContatoSync.class); //O objeto retornado é um contatoSync. Possui a List e uma string momentoDaUltimaModi...
                ContatoDAO dao = new ContatoDAO(this);
                dao.sincroniza(contatoSync.getContatos());//Funcao do dao que recebe uma lista de Contatos e sincroniza com o banco.
                dao.close();

                EventBus eventBus = EventBus.getDefault();//Cria um EventBus para avisar a ListContatosActivity para carreagarnovamente a lista.
                eventBus.post(new AtualizaListaContatoEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
