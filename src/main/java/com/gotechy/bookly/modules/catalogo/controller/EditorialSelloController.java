import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.service.EditorialSelloService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/editorial-sello")
@RequiredArgsConstructor
public class EditorialSelloController {

    private final EditorialSelloService editorialSelloService;

    @GetMapping
    public ResponseEntity<List<EditorialSello>> getAllEditorialSello() {
        List<EditorialSello> editorialSelloList =
            editorialSelloService.listarEditorialesSelloActivas();
        return ResponseEntity.ok(editorialSelloList);
    }

    @PostMapping
    public ResponseEntity<EditorialSello> createEditorialSello(
        @RequestBody EditorialSello editorialSello
    ) {
        EditorialSello createdEditorialSello =
            editorialSelloService.crearEditorialSello(editorialSello);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            createdEditorialSello
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEditorialSello(
        @PathVariable Integer id
    ) {
        editorialSelloService.eliminarEditorialSello(id);
        return ResponseEntity.noContent().build();
    }
}
