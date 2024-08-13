package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.Factory;
import beans.User;
import dao.ChocolateDAO;
import dao.FactoryDAO;
import dao.UserDAO;

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
        if (ctx.getAttribute("userDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("userDAO", new UserDAO(contextPath));
        }
        if (ctx.getAttribute("chocolateDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factory> searchFactories(@QueryParam("name") String name, 
                                          @QueryParam("chocolateName") String chocolateName,
                                          @QueryParam("location") String location,
                                          @QueryParam("averageRating") Double averageRating) {
        FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        return dao.searchFactories(name, chocolateName, location, averageRating);
    }
    @GET
    @Path("/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factory> getFactoriesByUser(@PathParam("userId") String userId) {
        FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        UserDAO userdao = (UserDAO) ctx.getAttribute("userDAO");
        User user = userdao.getById(userId);
        // Note: Ensure the below return statement fetches the correct factories by user
        return dao.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Factory getById(@PathParam("id") int id) {
        FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        return dao.getById(id);
    }

    @GET
    @Path("/{factoryId}/chocolates/{chocolateId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChocolateById(@PathParam("factoryId") int factoryId, @PathParam("chocolateId") int chocolateId) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        Factory factory = factoryDAO.getById(factoryId);

        if (factory != null) {
            for (Chocolate chocolate : factory.getChocolates()) {
                if (chocolate.getId() == chocolateId) {
                    return Response.status(Response.Status.OK).entity(chocolate).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Chocolate not found").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory not found").build();
        }
    }

    @PUT
    @Path("/{factoryId}/addChocolate/{chocolateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChocolateToFactory(@PathParam("factoryId") int factoryId, @PathParam("chocolateId") int chocolateId) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");

        Factory factory = factoryDAO.getById(factoryId);
        Chocolate chocolate = chocolateDAO.getById(chocolateId);

        if (factory != null && chocolate != null) {
            factory.getChocolates().add(chocolate);
            factoryDAO.updateFactory(factory);
            return Response.status(Response.Status.OK).entity("Chocolate added to factory.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory or chocolate not found.").build();
        }
    }
    @DELETE
    @Path("/{factoryId}")
    public Response deleteFactory(@PathParam("factoryId") int factoryId) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        factoryDAO.deleteFactory(factoryId);
        return Response.ok().build();
    }
    @DELETE
    @Path("/{factoryId}/{chocolateId}")
    public Response deleteChocolateFromFactory(@PathParam("factoryId") int factoryId, @PathParam("chocolateId") int chocolateId) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        factoryDAO.deleteChocolateFromFactory(chocolateId, factoryId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{factoryId}/chocolates/{chocolateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateChocolateInFactory(@PathParam("factoryId") int factoryId, @PathParam("chocolateId") int chocolateId, Chocolate updatedChocolate) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        Factory factory = factoryDAO.getById(factoryId);

        if (factory != null) {
            Chocolate existingChocolate = null;
            for (Chocolate chocolate : factory.getChocolates()) {
                if (chocolate.getId() == chocolateId) {
                    existingChocolate = chocolate;
                    break;
                }
            }

            if (existingChocolate != null) {
                existingChocolate.setName(updatedChocolate.getName());
                existingChocolate.setPrice(updatedChocolate.getPrice());
                existingChocolate.setChocolateKind(updatedChocolate.getChocolateKind());
                existingChocolate.setChocolateType(updatedChocolate.getChocolateType());
                existingChocolate.setWeight(updatedChocolate.getWeight());
                existingChocolate.setDescription(updatedChocolate.getDescription());
                existingChocolate.setImage(updatedChocolate.getImage());
                factoryDAO.updateFactory(factory);
                return Response.status(Response.Status.OK).entity(existingChocolate).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Chocolate not found").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory not found").build();
        }
    }
    
    @PUT
    @Path("/{factoryId}/chocolatequantity/{chocolateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateChocolateQuantity(@PathParam("factoryId") int factoryId, @PathParam("chocolateId") int chocolateId, Chocolate updatedChocolate) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        Factory factory = factoryDAO.getById(factoryId);

        if (factory != null) {
            Chocolate existingChocolate = null;
            for (Chocolate chocolate : factory.getChocolates()) {
                if (chocolate.getId() == chocolateId) {
                    existingChocolate = chocolate;
                    break;
                }
            }
            if (existingChocolate != null) {
                existingChocolate.setQuantity(updatedChocolate.getQuantity());
                factoryDAO.updateFactory(factory);
                return Response.status(Response.Status.OK).entity(existingChocolate).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Chocolate not found").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory not found").build();
        }
    }

    @POST
    @Path("/addFactory")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFactory(Factory factory) {
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        Factory createdFactory = factoryDAO.create(factory);
        if (createdFactory != null) {
            return Response.status(Response.Status.CREATED).entity(createdFactory).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating factory.").build();
        }
    }
}
