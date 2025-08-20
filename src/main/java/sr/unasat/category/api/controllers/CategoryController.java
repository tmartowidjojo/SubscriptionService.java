package sr.unasat.category.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sr.unasat.category.api.dto.CategoryDTO;
import sr.unasat.category.api.entity.Category;
import sr.unasat.category.api.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
public class CategoryController {
    private final CategoryService service = new CategoryService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CategoryDTO> getAllCategories() {
        return service.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("id") Long id) {
        Category category = service.findById(id);
        if (category != null) {
            return Response.ok(toDTO(category)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryDTO dto) {
        Category category = toEntity(dto);
        Category saved = service.save(category);
        return Response.status(Response.Status.CREATED).entity(toDTO(saved)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("id") Long id, CategoryDTO dto) {
        Category existing = service.findById(id);
        if (existing != null) {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            Category updated = service.update(existing);
            return Response.ok(toDTO(updated)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        Category existing = service.findById(id);
        if (existing != null) {
            service.delete(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private Category toEntity(CategoryDTO dto) {
        return new Category(
                dto.getName(),
                dto.getDescription()
        );
    }
}