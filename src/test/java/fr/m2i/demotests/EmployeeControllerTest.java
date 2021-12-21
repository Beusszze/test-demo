package fr.m2i.demotests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import fr.m2i.demotests.employees.EmployeeController;
import fr.m2i.demotests.employees.EmployeeService;
import fr.m2i.demotests.employees.dtos.EmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.Optional;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;


    private EmployeeDTO employeeDTO () {
        return new EmployeeDTO(
                1L,
                "benoit",
                "benoit@benoit.com",
                new Date(),
                1,
                5486F
        );

    }

    private EmployeeDTO employeeDTOToUpdate () {
        return new EmployeeDTO(
                1L,
                "benny",
                "benoit@benoit.com",
                new Date(),
                1,
                5486F
        );

    }


    @Test
    public void testFindAllEmployees() throws Exception {
        this.mockMvc
                .perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    public void testSaveEmployee() throws Exception {
        // Mock employé
        EmployeeDTO employeeDTO = this.employeeDTO();
        // Transformé en json
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String body = json.toJson(employeeDTO);
        this.mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // Mock employé
        EmployeeDTO employeeDTO = this.employeeDTO();
        BDDMockito.given(service.findById(1L))
                .willReturn(Optional.of(employeeDTOToUpdate()));
        // Transformé en json
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String body = json.toJson(employeeDTO);

        this.mockMvc.perform(put("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }


    @Test
    public void testFindOneEmployeeWhereWrongIdOrInexistantEmployee() throws Exception {
        this.mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindOneEmployee() throws Exception {
        EmployeeDTO employeeDTO = this.employeeDTO();

        // Mock de la bdd avec BDDMockito
        BDDMockito.given(service.findById(1L))
                .willReturn(Optional.of(employeeDTO));

        MvcResult resultat = this.mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andReturn();
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        EmployeeDTO body = json.fromJson(resultat.getResponse()
                        .getContentAsString(), EmployeeDTO.class);
        Assertions.assertEquals(body.getUsername(), this.employeeDTO().getUsername());
        System.out.println(body.getUsername());
    }
}
