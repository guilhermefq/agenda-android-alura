package br.com.softgran.agenda.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by guilhermefq on 26/12/17.
 */

public class ContatoPreferences {


    private Context context;
    private String VERSAO_DO_DADO = "versao_do_dados";
    private String CONTATO_PREFERENCES = "br.com.softgran.agenda.preferences.ContatoPreferences";

    public ContatoPreferences(Context context) {
        this.context = context;
    }

    public void salvarVersao(String versao) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(CONTATO_PREFERENCES, context.MODE_PRIVATE);
    }

    public String getVersao() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(VERSAO_DO_DADO, "");
    }

    public boolean temVersao() {
        return !getVersao().isEmpty();
    }


}
