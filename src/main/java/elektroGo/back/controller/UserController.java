package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderUser;
import elektroGo.back.data.Gateways.GatewayUser;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class UserController {

    @GetMapping("/user/{userName}")
    public String getUser(@PathVariable String userName) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUserName(userName);
        return gU.json();
    }

    @GetMapping("/users")
    public String getUsers() throws SQLException, JsonProcessingException {
        FinderUser fU = FinderUser.getInstance();
        ArrayList<GatewayUser> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    @PostMapping("/users/create")
    public void createVehicle(@RequestBody GatewayUser gU) {
        try {
            gU.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/users/delete")
    public void deleteVehicle(@RequestParam String userName) {
        FinderUser fU = FinderUser.getInstance();
        try {
            GatewayUser gU = fU.findByUserName(userName);
            if (gU != null) gU.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
