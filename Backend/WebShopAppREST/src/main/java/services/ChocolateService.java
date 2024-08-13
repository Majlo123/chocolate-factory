package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.Factory;
import dao.ChocolateDAO;
import dao.FactoryDAO;

@Path("/chocolates")
public class ChocolateService {
    @Context
    ServletContext ctx;

    public ChocolateService() {}

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("chocolateDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
        }
        if (ctx.getAttribute("factoryDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("factoryDAO", new FactoryDAO(contextPath));
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chocolate> getAllChocolates() {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        return dao.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createChocolate(Chocolate chocolate) {
        ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        Chocolate createdChocolate = chocolateDAO.create(chocolate);
        if (createdChocolate != null) {
            return Response.status(Response.Status.CREATED).entity(createdChocolate).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating chocolate.").build();
        }
    }

    @POST
    @Path("/createInFactory/{factoryId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createChocolateInFactory(@PathParam("factoryId") int factoryId, Chocolate chocolate) {
        ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");

        Chocolate createdChocolate = chocolateDAO.create(chocolate);
        Factory factory = factoryDAO.getById(factoryId);

        if (createdChocolate != null && factory != null) {
            factory.getChocolates().add(createdChocolate);
            factoryDAO.updateFactory(factory);
            return Response.status(Response.Status.CREATED).entity(createdChocolate).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory not found or error creating chocolate.").build();
        }
    }
}
