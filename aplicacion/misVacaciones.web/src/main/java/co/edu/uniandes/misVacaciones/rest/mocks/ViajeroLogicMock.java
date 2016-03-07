
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.misVacaciones.rest.mocks;

/**
 *
 * @author Asistente
 */
import co.edu.uniandes.misVacaciones.rest.dtos.CiudadDTO;
import co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException;
import static co.edu.uniandes.misVacaciones.rest.mocks.CiudadLogicMock.ciudades;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.inject.Singleton;


/**
 * Mock del recurso Ciudades (Mock del servicio REST)
 */
@Named
@Singleton
public class ViajeroLogicMock {

	// objeto para presentar logs de las operaciones
	private final static Logger logger = Logger.getLogger(ViajeroLogicMock.class.getName());

	// listado de ciudades
    public static ArrayList<CiudadDTO> ciudades;

    /**
     * Constructor. Crea los datos de ejemplo.
     */
    public ViajeroLogicMock() {

    	if (ciudades == null) {
            ciudades = new ArrayList<>();
             ciudades.add(new CiudadDTO(1L, "Bogota", "Bogotá es la capital de la República de Colombia", "http://aiesec.org.mx/wp-content/uploads/2015/08/bogota.jpg","07/05/2016", " 08/05/2016" ));
            ciudades.add(new CiudadDTO(2L, "Cali", "Sucursal del cielo", "http://static.panoramio.com/photos/large/43907931.jpg", "15/06/2016", "17/06/2016"));
            ciudades.add(new CiudadDTO(3L, "Bucaramanga", "Ciudad de los paques", "https://c1.staticflickr.com/3/2724/4176942891_3f6d1f1dcf_b.jpg", "18/07/2016", "19/07/2016"));
        }

    	// indica que se muestren todos los mensajes
    	logger.setLevel(Level.INFO);

    	// muestra información
    	logger.info("Inicializa la lista de ciudades");
    	logger.info("ciudades" + ciudades );
    }

	/**
	 * Obtiene el listado de personas.
	 * @return lista de ciudades
	 * @throws CiudadLogicException cuando no existe la lista en memoria
	 */
    public List<CiudadDTO> getCities() throws CiudadLogicException {
    	if (ciudades == null) {
    		logger.severe("Error interno: lista de ciudades no existe.");
    		throw new CiudadLogicException("Error interno: lista de ciudades no existe.");
    	}

    	logger.info("retornando todas las ciudades");
    	return ciudades;
    }

    /**
     * Obtiene una ciudad
     * @param id identificador de la ciudad
     * @return ciudad encontrada
     * @throws CiudadLogicException cuando la ciudad no existe
     */
    public CiudadDTO getCity(Long id) throws CiudadLogicException {
    	logger.info("recibiendo solicitud de ciudad con id " + id);

    	// busca la ciudad con el id suministrado
        for (CiudadDTO city : ciudades) {
            if (Objects.equals(city.getId(), id)){
            	logger.info("retornando ciudad " + city);
                return city;
            }
        }

        // si no encuentra la ciudad
        logger.severe("No existe ciudad con ese id");
        throw new CiudadLogicException("No existe ciudad con ese id");
    }

    /**
     * Agrega una ciudad a la lista.
     * @param newCity ciudad a adicionar
     * @throws CiudadLogicException cuando ya existe una ciudad con el id suministrado
     * @return ciudad agregada
     */
    public CiudadDTO createCity(CiudadDTO newCity) throws CiudadLogicException {
    	logger.info("recibiendo solicitud de agregar ciudad " + newCity);

    	// la nueva ciudad tiene id ?
    	if ( newCity.getId() != null ) {
	    	// busca la ciudad con el id suministrado
	        for (CiudadDTO city : ciudades) {
	        	// si existe una ciudad con ese id
	            if (Objects.equals(city.getId(), newCity.getId())){
	            	logger.severe("Ya existe una ciudad con ese id");
	                throw new CiudadLogicException("Ya existe una ciudad con ese id");
	            }
	        }

	    // la nueva ciudad no tiene id ?
    	} else {

    		// genera un id para la ciudad
    		logger.info("Generando id paa la nueva ciudad");
    		long newId = 1;
	        for (CiudadDTO city : ciudades) {
	            if (newId <= city.getId()){
	                newId =  city.getId() + 1;
	            }
	        }
	        newCity.setId(newId);
    	}

        // agrega la ciudad
    	logger.info("agregando ciudad " + newCity);
        ciudades.add(newCity);
        return newCity;
    }

    /**
     * Actualiza los datos de una ciudad
     * @param id identificador de la ciudad a modificar
     * @param city ciudad a modificar
     * @return datos de la ciudad modificada
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     */
    public CiudadDTO updateCity(Long id, CiudadDTO updatedCity) throws CiudadLogicException {
    	logger.info("recibiendo solictud de modificar ciudad " + updatedCity);

    	// busca la ciudad con el id suministrado
        for (CiudadDTO city : ciudades) {
            if (Objects.equals(city.getId(), id)) {

            	// modifica la ciudad
            	city.setId(updatedCity.getId());
                city.setNombre(updatedCity.getNombre());

                // retorna la ciudad modificada
            	logger.info("Modificando ciudad " + city);
                return city;
            }
        }

        // no encontró la ciudad con ese id ?
        logger.severe("No existe una ciudad con ese id");
        throw new CiudadLogicException("No existe una ciudad con ese id");
    }

    /**
     * Elimina los datos de una ciudad
     * @param id identificador de la ciudad a eliminar
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     */
    public void deleteCity(Long id) throws CiudadLogicException {
    	logger.info("recibiendo solictud de eliminar ciudad con id " + id);

    	// busca la ciudad con el id suministrado
        for (CiudadDTO city : ciudades) {
            if (Objects.equals(city.getId(), id)) {

            	// elimina la ciudad
            	logger.info("eliminando ciudad " + city);
                ciudades.remove(city);
                return;
            }
        }

        // no encontró la ciudad con ese id ?
        logger.severe("No existe una ciudad con ese id");
        throw new CiudadLogicException("No existe una ciudad con ese id");
    }
}
