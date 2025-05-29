public class ProductosDTO {
        private int id;
        private String nombre;
        private double precio;
        private int stock;
        private String descripcion;
        private Object fecha_creacion;

    public ProductosDTO() {}

    public ProductosDTO(
        int id,
        String nombre,
        double precio,
        int stock,
        String descripcion,
        Object fecha_creacion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

        public int getId() {
        return id;
        }

        public void setId(int id) {
        this.id = id;
        }

        public String getNombre() {
        return nombre;
        }

        public void setNombre(String nombre) {
        this.nombre = nombre;
        }

        public double getPrecio() {
        return precio;
        }

        public void setPrecio(double precio) {
        this.precio = precio;
        }

        public int getStock() {
        return stock;
        }

        public void setStock(int stock) {
        this.stock = stock;
        }

        public String getDescripcion() {
        return descripcion;
        }

        public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        }

        public Object getFecha_creacion() {
        return fecha_creacion;
        }

        public void setFecha_creacion(Object fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
        }


    @Override
    public String toString() {
    return 
        "id=" + getId() + 
    
        "nombre=" + getNombre() + 
    
        "precio=" + getPrecio() + 
    
        "stock=" + getStock() + 
    
        "descripcion=" + getDescripcion() + 
    
        "fecha_creacion=" + getFecha_creacion()
    ;
    }
}