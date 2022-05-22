/**
 * @file ChatsController.java
 * @author Adria Abad
 * @date 13/04/2022
 * @brief Implementacio de la classe ChatsController.
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderChats;
import elektroGo.back.data.gateways.GatewayChats;
import elektroGo.back.data.gateways.GatewayDeletedChats;
import elektroGo.back.model.DeletedChats;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * @brief La classe ChatsController és la classe que comunicarà front-end i back-end a l'hora de gestionar el chat
 */
@RestController
@RequestMapping("/chats")
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

    /**
     * @brief Metode 'GET' que retorna tots els chats que l'usuari te
     * @param user nom de l'usuari
     * @return Retorna el llistat de chats
     */
    @GetMapping("/findByUser")
    public ArrayList<String> getChatByConversation(@RequestParam String user) throws SQLException {
        FinderChats fC = FinderChats.getInstance();
        DeletedChats dCS = new DeletedChats();
        ArrayList<String> usersChats = fC.findByUser(user);
        ArrayList<String> deletedChats = dCS.getDeletedChatsFromUser(user);
        usersChats.removeAll(deletedChats);
        return usersChats;
    }

    /**
     * @brief Metode 'GET' que retorna tots els missatges d'un usuari de la base de dades
     * @param user nom de l'usuari
     * @return Retorna el llistat de missatges de l'usuari ordenats per data
     */
    @GetMapping("/findByReceived")
    public ArrayList<GatewayChats> getChatByReceived(@RequestParam String user) throws SQLException {
        FinderChats fC = FinderChats.getInstance();
        return fC.findByReceived(user);
    }

    /**
     * @brief Metode 'POST' que crea un missatge entre dos usuaris
     * @param sender nom de l'usuari que envia el missatge
     * @param receiver nom de l'usuari que rep el missatge
     * @param message contingut del missatge en si
     * @post El missatge s'ha creat correctament
     */
    @PostMapping("/sendMessage")
    public void postSendMessage(@RequestParam String sender, @RequestParam String receiver, @RequestParam String message) throws SQLException {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        timestamp = timestamp.substring(0, timestamp.length() - 4);
        GatewayChats gC = new GatewayChats(sender, receiver, message,timestamp);
        gC.insert();
    }

    /**
     * @brief Metode 'DELETE' que crea elimina el chat entre dos usuaris
     * @param userA Nom de l'usuari que elimina el xat
     * @param userB Nom de l'usuari sobre el que s'elimina el xat
     * @post El xat s'ha eliminat correctament
     */
    @DeleteMapping
    public void deleteChat(@RequestParam String userA, @RequestParam String userB) throws SQLException {
        GatewayDeletedChats gDC = new GatewayDeletedChats(userA, userB);
        DeletedChats dCS = new DeletedChats();
        gDC.insert();
        dCS.deleteMessagesIfNeeded(userA, userB);
    }



}
