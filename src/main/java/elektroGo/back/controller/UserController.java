package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderUser;
import elektroGo.back.data.Gateways.GatewayUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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


}
