package br.com.softgran.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.softgran.agenda.R;
import br.com.softgran.agenda.modelo.Contato;


public class ContatosAdapter extends BaseAdapter {

    private final List<Contato> contatos;
    private final Context context;

    public ContatosAdapter(Context context, List<Contato> contatos) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Contato contato  = contatos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView campoNome = view.findViewById(R.id.item_nome);
        campoNome.setText(contato.getNome());

        TextView campoTelefone = view.findViewById(R.id.item_telefone);
        campoTelefone.setText(contato.getTelefone());

        ImageView campoFoto = view.findViewById(R.id.item_foto);
        String caminhoFoto = contato.getCaminhoFoto();
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
