package com.signaturemaker.app.Nucleo;

public class ItemFile {

    private String nombre;
    private String fecha;
    private String tamaño;

    public static final int TYPE_LARGE = 0;
    public static final int TYPE_SHORT = 1;

    public ItemFile(String nombre, String fecha, String tamaño) {
        super();
        this.nombre = nombre;
        this.fecha = fecha;
        this.tamaño = tamaño;
    }

    public ItemFile() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }


}