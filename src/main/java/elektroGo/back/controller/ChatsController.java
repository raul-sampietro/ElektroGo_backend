/**
 * @file ChatsController.java
 * @author Adria Abad
 * @date 13/04/2022
 * @brief Implementacio de la classe ChatsController.
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderChats;
import elektroGo.back.data.gateways.GatewayChats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

//todo exceptions

/**
 * @brief La classe ChatsController és la classe que comunicarà front-end i back-end a l'hora de gestionar el chat
 */
@RestController
@RequestMapping("/chat")
public class ChatsController {

    /**
     * @brief Metode 'GET' que retorna tots els missatges de la base de dades
     * @return Retorna el llistat de tots els missatges
     */
    @GetMapping("/findAll")
    public ArrayList<GatewayChats> getAll() throws SQLException {
        FinderChats fC = FinderChats.getInstance();
        return fC.findAll();
    }

    /**
     * @brief Metode 'GET' que retorna tots els missatges entre dos usuaris de la base de dades
     * @param userA nom de l'usuari A
     * @param userB nom de l'usuari B
     * @return Retorna el llistat de missatges entre els dos usuaris ordenats per data
     */
    @GetMapping("/findByConversation")
    public ArrayList<GatewayChats> getChatByConversation(@RequestParam String userA, @RequestParam String userB) throws SQLException {
        FinderChats fC = FinderChats.getInstance();
        return fC.findByConversation(userA, userB);
    }

}
