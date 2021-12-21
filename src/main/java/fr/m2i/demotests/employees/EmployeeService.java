package fr.m2i.demotests.employees;

import fr.m2i.demotests.employees.dtos.EmployeeDTO;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EmployeeService {

    private EmployeeRepository repository;
    private ModelMapper mapper;

    public EmployeeService(EmployeeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Retourne une liste d'employés
     * @return List <EmployeeDTO>
     */
    public List<EmployeeDTO> findAll () {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        this.repository.findAll().forEach(
                employee -> {
                    employeeDTOList.add(mapper.map(employee, EmployeeDTO.class));
                }
        );
        return employeeDTOList;
    }


    /**
     * Permet de retrouver un employé par son ID
     * @param id Long
     * @return Optional<EmployeeDTO>
     */
    public Optional<EmployeeDTO> findById (final Long id) throws NoSuchElementException {
        Optional<Employee> employee = this.repository.findById(id);
        return Optional.of(mapper.map(employee.get(), EmployeeDTO.class));
    }

    /**
     * Permet de persister un employé
     * @param employeeDTO EmployeeDTO
     * @return EmployeeDTO
     */
    public EmployeeDTO save (EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        EmployeeDTO response = mapper.map(this.repository.save(employee), EmployeeDTO.class);
        return response;
    }

    /**
     * Permet de supprimer un employé
     * @param employeeDTO EmployeeDTO
     */
    public void delete (EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        this.repository.delete(employee);
    }
}
