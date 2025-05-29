public class ClientesDTO {
        private int id;
        private String nombre;
        private String email;
        private String telefono;

    public ClientesDTO() {}

    public ClientesDTO(  int id,  String nombre,  String email,  String telefono ) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.telefono = telefono;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }


    @Override
    public String toString() {
    return 
        "id=" + getId() + 
    
        "nombre=" + getNombre() + 
    
        "email=" + getEmail() + 
    
        "telefono=" + getTelefono()
    ;
    }
}