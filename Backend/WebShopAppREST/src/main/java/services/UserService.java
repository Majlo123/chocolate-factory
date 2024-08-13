package services;

import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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
import dao.FactoryDAO;
import dao.UserDAO;

@Path("/users")
public class UserService {

    @Context
    ServletContext ctx;

    public UserService() {}

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("userDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("userDAO", new UserDAO(contextPath));
        }
        if (ctx.getAttribute("factoryDAO") == null) {
            String contextPath = ctx.getRealPath("/");
            ctx.setAttribute("factoryDAO", new FactoryDAO(contextPath));
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> getAllUsers() {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        return dao.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") String id) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        return dao.getById(id);
    }
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @QueryParam("username") String username) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        Collection<User> users = dao.searchUsers(firstName, lastName, username);
        return Response.ok(users).build();
    }

    @GET
    @Path("/sort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortUsers(@QueryParam("sortBy") String sortBy, @QueryParam("ascending") boolean ascending) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        Collection<User> users = dao.sortUsers(sortBy, ascending);
        return Response.ok(users).build();
    }
    
    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterUsers(@QueryParam("role") String role, @QueryParam("userType") String userType) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        Collection<User> users = dao.filterUsers(role, userType);
        return Response.ok(users).build();
    }
    @PUT
    @Path("/{username}/toggleBlock")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toggleBlock(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setBlocked(updatedUser.isBlocked());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User login(User user) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        return dao.getByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @POST
    @Path("/signupworker/{factoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpWorker(@PathParam("factoryId") int factoryId, User user) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        FactoryDAO factoryDao = (FactoryDAO) ctx.getAttribute("factoryDAO");
        if (dao.getById(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("Username exists.").build();
        }

        Factory factory = factoryDao.getById(factoryId);
        if (factory == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Factory not found.").build();
        }

        user.setRole("Radnik");
        user.setFactory(factory);

        dao.create(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
    }
    @POST
    @Path("/signupmanager")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpManager(User user) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");

        if (dao.getById(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("Username exists.").build();
        }

        user.setRole("Menadžer");
        dao.create(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @POST
    @Path("/signupuser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpUser(User user) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");

        if (dao.getById(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("Username exists.").build();
        }

        user.setRole("Kupac");
        dao.create(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{username}/assignFactory/{factoryId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignFactoryToManager(@PathParam("username") String username, @PathParam("factoryId") int factoryId) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");

        User manager = userDAO.getById(username);
        Factory factory = factoryDAO.getById(factoryId);

        if (manager != null && factory != null) {
            manager.setFactory(factory);
            userDAO.updateUser(manager);
            return Response.status(Response.Status.OK).entity(manager).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Manager or Factory not found").build();
        }
    }

    @GET
    @Path("/managersWithoutFactory")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> getManagersWithoutFactory() {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        return dao.getManagersWithoutFactory();
    }

    @PUT
    @Path("/{username}/changePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserPassword(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setPassword(updatedUser.getPassword());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @PUT
    @Path("/{username}/changeFirstName")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserFirstName(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setFirstName(updatedUser.getFirstName());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @PUT
    @Path("/{username}/changeLastName")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserLastName(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setLastName(updatedUser.getLastName());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @PUT
    @Path("/{username}/changeGender")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserGender(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setGender(updatedUser.getGender());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @PUT
    @Path("/{username}/changeBirthDate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserDateBirth(@PathParam("username") String username, User updatedUser) {
        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDAO.getById(username);

        if (user != null) {
            user.setBirthDate(updatedUser.getBirthDate());
            userDAO.updateUser(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }
}
