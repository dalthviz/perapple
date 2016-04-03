/*
 * CityResource.java
 * Clase que representa el recurso "/cities"
 * Implementa varios métodos para manipular las ciudades
 */
package co.edu.uniandes.misVacaciones.rest.resources;


import co.edu.uniandes.misVacaciones.rest.converters.CiudadItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.converters.CiudadConverter;
import co.edu.uniandes.misVacaciones.rest.converters.ItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.dtos.CiudadDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.CiudadItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.EventoDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.ItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.SitioDTO;
import co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.EventoLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.ItinerarioLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.SitioLogicException;
import co.edu.uniandes.perapple.api.IItinerarioLogic;
import co.edu.uniandes.perapple.entities.CiudadEntity;
import co.edu.uniandes.perapple.entities.CiudadItinerarioEntity;
import co.edu.uniandes.perapple.entities.ItinerarioEntity;
import co.edu.uniandes.perapple.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Clase que implementa el recurso REST correspondiente a "itinerarios".
 *
 * Note que la aplicación (definida en RestConfig.java) define la ruta
 * "/api" y este recurso tiene la ruta "itinerarios".
 * Al ejecutar la aplicación, el recurse será accesibe a través de la
 * ruta "/api/cities"
 *
 * @author Perapple
 */
@Path("itinerarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItinerarioResource {
        private static final Logger logger = Logger.getLogger(CiudadResource.class.getName());
    
	@Inject
	IItinerarioLogic itinerarioLogic;

      /**
       * El itinerario que actualmente se este manejando
       * @return el itinerario que actualmente se esta manejando
       * @throws ItinerarioLogicException
       */

    @GET
    @Path("current")
    public ItinerarioDTO getCurrentItinerario(){
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.getCurrentItinerario());
    }

    @PUT
    @Path("current")
    public ItinerarioDTO setCurrentItinerario(ItinerarioDTO nuevoCurrent){
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(nuevoCurrent);
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.setCurrentItinerario(entity));
    }

	/**
	 * Obtiene el listado de Itinerarios.
	 * @return lista de itinerarios
	 * @throws ItinerarioLogicException excepción retornada por la lógica
	 */
    @GET
    public List<ItinerarioDTO> getItinerarios() {
        return ItinerarioConverter.listEntity2DTO(itinerarioLogic.getItinerarios());
    }

    /**
     * Obtiene un itinerario
     * @param id identificador del itinerario
     * @return itinerario encontrado
     * @throws ItinerarioLogicException cuando el itineraio no existe
     */
    @GET
    @Path("{id: \\d+}")
    public ItinerarioDTO getItinerario(@PathParam("id") Long id) {
        try {
            return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.getItinerario(id));
        } catch (BusinessLogicException ex){
            logger.log(Level.SEVERE, "La ciudad no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
    }

    /**
     * Agrega un itinerario
     * @param itinerario itinerario a agregar
     * @return datos del itinerario a agregar
     * @throws ItinerarioLogicException cuando ya existe una ciudad con el id suministrado
     */
    @POST
    public ItinerarioDTO createItinerario(ItinerarioDTO itinerario) {
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(itinerario);
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.createItinerario(entity));
    }

    /**
     * Actualiza los datos de un itineario
     * @param id identificador del itinerario a modificar
     * @param itinerario itinerario a modificar
     * @return datos del itinerario a modificada
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @PUT
    @Path("{id: \\d+}")
    public ItinerarioDTO updateItinerario(@PathParam("id") Long id, ItinerarioDTO itinerario) {
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(itinerario);
        entity.setId(id);
        try {
            ItinerarioEntity oldEntity = itinerarioLogic.getItinerario(id);
            // TODO
            //entity.setCiudades(oldEntity.getCiudades());
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, "El itinerario no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.updateItinerario(entity));
    }

    /**
     * Elimina los datos de un itinerario
     * @param id identificador del itinerario a eliminar
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteItinerario(@PathParam("id") Long id) {
    	itinerarioLogic.deleteItinerario(id);
    }

    /**
     * Crea ciudad en el itinerario con id dado
     * @param id identificador del itinerario a agregar ciudad
     * @param ciudad dto de ciudad a agregar
     */
    @POST
    @Path("{id: \\d+}/ciudades")
    public CiudadItinerarioDTO createCiudad(@PathParam("id")Long id, CiudadItinerarioDTO ciudad) {
        CiudadItinerarioEntity entity = CiudadItinerarioConverter.fullDTO2Entity(ciudad);
        return CiudadItinerarioConverter.fullEntity2DTO(itinerarioLogic.addCiudad(entity, id));
    }
    /**
     * Actualiza los datos de una ciudad de un itineario
     * @param id identificador del itinerario a modificar
     * @param ciudad datos modificados de la ciudad
     * @param idciudad identificadot de la ciudad
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @PUT
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}")
    public void updateCiudades(@PathParam("id") Long id, CiudadDTO ciudad, @PathParam("idciudad") Long idciudad) {

        itinerarioLogic.updateCiudad(id, ciudad, idciudad);
    }

    /**
     * Elimina los datos de un itinerario
     * @param id identificador del itinerario a eliminar
     * @param idciudad
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}")
    public void deleteCiudad(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad) {
    	itinerarioLogic.deleteCiudad(id, idciudad);
    }

    /**
     * Obtiene las ciudades del itinerario
     * @param id identificador del itinerario
     * @return lista de ciudades del itinerario
     * @throws ItinerarioLogicException cuando el itineraio no existe
     */
    @GET
    @Path("{id: \\d+}/ciudades")
    public List<CiudadDTO> getCiudades(@PathParam("id") Long id) {
        return itinerarioLogic.getCiudades(id);
    }

    /**
     * Obtiene la ciudad con el idciudad dado, del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificadot de la ciudad
     * @return ciudad buscada
     * @throws ItinerarioLogicException si no existe itinerario
     * @throws CiudadLogicException  si no existe ciudad
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad:\\d+}")
    public CiudadDTO getCiudad(@PathParam("id") Long id, @PathParam("idciudad") Long idciudad) {
        return itinerarioLogic.getCiudad(id, idciudad);
    }


    /**
     * Agrega un sitio de interes en una ciudad con el id dado del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @param sitio el sitio a agregar
     * @return el sitio que agregó
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @POST
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios")
    public SitioDTO createSitioInteres(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad, SitioDTO sitio) {
        return itinerarioLogic.createSitioDeInteres(id, idciudad, sitio);
    }

     /**
     * Elimina los datos de un sitio de interes en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idsitio identificador del sitio a eliminar
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws SitioLogicException cuando no existe un sitio con el id sumunistrado
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios/{idsitio: \\d+}")
    public void deleteSitioDeInteres(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idsitio") Long idsitio) {
    	itinerarioLogic.deleteSitioDeInteres(id, idciudad, idsitio);
    }

     /**
     * Obtiene el listado de sitios de una ciudad en un itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @return
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios")
    public ArrayList<SitioDTO> fetchSitiosInteres(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad) throws {
        return itinerarioLogic.fetchSitiosDeInteres(id, idciudad);
    }

         /**
     * Obtiene los datos de un sitio de interes en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idsitio identificador del sitio a buscar
     * @return
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws SitioLogicException cuando no existe un sitio con el id sumunistrado
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios/{idsitio: \\d+}")
    public SitioDTO fetchSitioDeInteres(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idsitio") Long idsitio) {
    	return itinerarioLogic.fetchSitioDeInteres(id, idciudad, idsitio);
    }

     /**
     * Agrega un evento en una ciudad con el id dado del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @param evento el evento a agregar
     * @return el evento que agregó
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @POST
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos")
    public EventoDTO createEvento(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad, EventoDTO evento) throws {
        return itinerarioLogic.createEvento(id, idciudad, evento);
    }

     /**
     * Elimina los datos de un evento en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idevento identificador del evento a eliminar
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws EventoLogicException cuando no existe un evento con el id sumunistrado
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos/{idevento: \\d+}")
    public void deleteEvento(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idevento") Long idevento) {
    	itinerarioLogic.deleteEvento(id, idciudad, idevento);
    }

     /**
     * Obtiene el listado de eventos de una ciudad en un itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @return lista de eventos de la ciudad con el id dado del itinerario con el id dado
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos")
    public ArrayList<EventoDTO> fetchEventos(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad) throws {
        return itinerarioLogic.fetchEventos(id, idciudad);
    }

    /**
     * Obtiene los datos de un evento en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idevento identificador del evento a buscar
     * @return evento buscado
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws EventoLogicException cuando no existe un sitio con el id sumunistrado
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos/{idevento: \\d+}")
    public EventoDTO fetchEvento(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idevento") Long idevento) {
    	return itinerarioLogic.fetchEvento(id, idciudad, idevento);
    }

    /**
     * Retorna la lista de itinerarios asociados a un viajero particular
     * @param id del viajero del que se quieren obtener sus itienrarios
     * @return lista de itinerarios
     */
    @GET
    @Path("viajero/{idviajero: }")
    public ArrayList<ItinerarioDTO> getItinerariosViajero(@PathParam("idviajero") String id){
    	return itinerarioLogic.getItinerariosViajero(id);
    }
}