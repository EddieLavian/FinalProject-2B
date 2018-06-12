package com.tobe.talyeh3.myapplication.Chat;

import java.util.Date;

/**
 * Created by user on 05/09/2017. 05
 */

public class MensajeRecibir extends Mensaje {

    private Long hora;
    private int massageDate;
    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String urlFoto, String nombre, String fotoPerfil, String type_mensaje, Long hora) {
        super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje);
        this.hora = hora;
        massageDate = new Date().getDate();
    }

    public Long getHora() {
        return hora;
    }

    public long getMessageDate() {
        return massageDate;
    }



    public void setHora(Long hora) {
        this.hora = hora;
    }
    public void setMessageDate(int massageDate) {
        this.massageDate = massageDate;
    }
}
