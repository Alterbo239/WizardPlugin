public class ArticulosDTO {

    private Object id;
    private String nombre;
    private String descripcion;
    private double precioProveedor;
    private double precioCliente;
    private int stock;
    private Object idProveedor;
    private Object idOptica;

    public ArticulosDTO() {}

    public ArticulosDTO(
            Object id,
            String nombre,
            String descripcion,
            double precioProveedor,
            double precioCliente,
            int stock,
            Object idProveedor,
            Object idOptica
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioProveedor = precioProveedor;
        this.precioCliente = precioCliente;
        this.stock = stock;
        this.idProveedor = idProveedor;
        this.idOptica = idOptica;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioProveedor() {
        return precioProveedor;
    }

    public void setPrecioProveedor(double precioProveedor) {
        this.precioProveedor = precioProveedor;
    }

    public double getPrecioCliente() {
        return precioCliente;
    }

    public void setPrecioCliente(double precioCliente) {
        this.precioCliente = precioCliente;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Object getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Object idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Object getIdOptica() {
        return idOptica;
    }

    public void setIdOptica(Object idOptica) {
        this.idOptica = idOptica;
    }


    @Override
    public String toString() {
        return 
            "id=" + getId() + 
        
            "nombre=" + getNombre() + 
        
            "descripcion=" + getDescripcion() + 
        
            "precioProveedor=" + getPrecioProveedor() + 
        
            "precioCliente=" + getPrecioCliente() + 
        
            "stock=" + getStock() + 
        
            "idProveedor=" + getIdProveedor() + 
        
            "idOptica=" + getIdOptica()
        ;
    }
}