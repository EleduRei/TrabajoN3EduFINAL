package cl.ipvg.trabajon3edu;

public class ListadoModel {
    private String idAutor;
    private String nombre;
    private String producto; // Cambiado a "producto"

    public ListadoModel() {
        this.idAutor = "";
        this.nombre = "";
        this.producto = "";
    }

    public ListadoModel(String idAutor, String nombre, String producto) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.producto = producto;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto() { // Cambiado a "getProducto"
        return producto;
    }

    public void setProducto(String producto) { // Cambiado a "setProducto"
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "ListadoModel{" + // Ajustado el nombre de la clase
                "idAutor='" + idAutor + '\'' +
                ", nombre='" + nombre + '\'' +
                ", producto='" + producto + '\'' + // Cambiado a "producto"
                '}';
    }
}
