package com.tobe.talyeh3.myapplication.Gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tobe.talyeh3.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 04/09/2017. 04
 */

public class AdapterGallery extends RecyclerView.Adapter<HolderMensaje> {



    private List<MensajeRecibir> listMensaje = new ArrayList<>();//create list
    private Context c;
    String profilePic="";




    public AdapterGallery(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m, String profilePic){
        this.profilePic=profilePic;
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_gallery, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(final HolderMensaje holder, final int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        if(listMensaje.get(position).getType_mensaje().equals("2")){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());
        }else if(listMensaje.get(position).getType_mensaje().equals("1")){
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
        Long codigoHora = listMensaje.get(position).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");//a pm o am
        holder.getHora().setText(sdf.format(d));
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, BigPhoto.class);
                intent.putExtra( "pic",listMensaje.get(position).getUrlFoto() );
                c.startActivity(intent);
            }
        } );
    }


    public class Item extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView mensajeFoto;
        public  Item(View itemView)
        {
            super(itemView);
            textView=(TextView)itemView.findViewById( R.id.nombreMensaje );
            mensajeFoto=(ImageView) itemView.findViewById( R.id.mensajeFoto );
        }
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

}
