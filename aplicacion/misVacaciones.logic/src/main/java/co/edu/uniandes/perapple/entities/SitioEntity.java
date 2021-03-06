package co.edu.uniandes.perapple.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
public class SitioEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nombre;

    @Column
    private String detalles;

    @Column
    private String imagen;

    @PodamExclude
    @ManyToOne
    private CiudadEntity ciudad;

    @PodamExclude
    @OneToMany(mappedBy = "sitio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SitioItinerarioEntity> sitiosItinerario;

    public void setSitiosItinerario(List<SitioItinerarioEntity> sitiosItinerario) {
        this.sitiosItinerario = sitiosItinerario;
    }

    public List<SitioItinerarioEntity> getSitiosItinerario() {
        return sitiosItinerario;
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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public CiudadEntity getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadEntity ciudad) {
        this.ciudad = ciudad;
    }
}
