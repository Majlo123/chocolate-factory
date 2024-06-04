package services;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Factory;
import beans.Location;
import dao.FactoryDAO;

@Path("/factories")
public class FactoryService {

    @Context
    ServletContext ctx;

    public FactoryService() {}

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("factoryDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("factoryDAO", new FactoryDAO(contextPath));
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factory> getAllLocations() {
    	FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        return dao.getAll();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Factory getById(@PathParam("id") int id) {
    	FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        return dao.getById(id);
    }
}
