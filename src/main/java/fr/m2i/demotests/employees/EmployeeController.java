package fr.m2i.demotests.employees;

import fr.m2i.demotests.employees.dtos.EmployeeDTO;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public List<EmployeeDTO> findAll() {
        return this.service.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        try {
            Optional<EmployeeDTO> employeeDTO = this.service.findById(id);
            return ResponseEntity.ok(employeeDTO.get());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().header(e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> save(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO response = this.service.save(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<EmployeeDTO> update(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO response = this.service.save(employeeDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestBody EmployeeDTO employeeDTO){
        this.service.delete(employeeDTO);
        return ResponseEntity.ok(true);
    }




}
