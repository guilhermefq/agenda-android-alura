package br.com.softgran.agenda;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.modelo.Contato;

/**
 * Created by guilhermefq on 06/12/17.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng posicaoInicial = pegaCoordenadaDoEndereco("Avenida Presidente Vargas 1235, Jardim Progresso, Dourados");

        if(posicaoInicial != null) {
            centralizaEm(posicaoInicial);
        }

        ContatoDAO contatoDAO = new ContatoDAO(getContext());
        for(Contato contato: contatoDAO.getContatos()) {
            LatLng coordenada = pegaCoordenadaDoEndereco(contato.getEndereco());
            if(coordenada != null) {
                marcarnoMapa(googleMap, coordenada,contato.getNome(), contato.getTelefone());

            }
        }
        contatoDAO.close();

        Localizador localizador = new Localizador(getContext(), googleMap);
        LatLng posicaoAtual = localizador.getCoordenada();


        if(posicaoAtual != null){
            Toast.makeText(getContext(),"Posição recebida: " + String.valueOf(posicaoAtual), Toast.LENGTH_SHORT).show();
            marcarnoMapa(googleMap,localizador.getCoordenada(), "Sua posição", "AQUI!");
        }

        //new Localizador(getContext(), googleMap);
    }

    public void centralizaEm(LatLng coordenada) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordenada, 17);
        googleMap.moveCamera(update);
    }

    public void marcarnoMapa(GoogleMap mapa, LatLng coordenada, String title, String snippet){
        MarkerOptions marcador = new MarkerOptions();
        marcador.position(coordenada);
        marcador.title(title);
        marcador.snippet(snippet);
        mapa.addMarker(marcador);
    }


    private LatLng pegaCoordenadaDoEndereco(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());//String que transforma Um endereço em Lat e Long

            List<Address> resultados =
                    geocoder.getFromLocationName(endereco, 1);
            if(!resultados.isEmpty()){
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
