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
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
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

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Metode 'GET' que retorna tots els missatges de la base de dades
     * @return Retorna el llistat de tots els missatges
     */
    @GetMapping("/messages")
    public ArrayList<GatewayChats> getAll() throws SQLException {
        logger.log("\nStarting Chats.getAll method...", logType.TRACE);
        FinderChats fC = FinderChats.getInstance();
        logger.log("Ending Chats.getAll method...", logType.TRACE);
        return fC.findAll();
    }

    /**
     * @brief Metode 'GET' que retorna tots els missatges entre dos usuaris de la base de dades
     * @param userA nom de l'usuari A
     * @param userB nom de l'usuari B
     * @return Retorna el llistat de missatges entre els dos usuaris ordenats per data
     */
    @GetMapping("/messages/{userA}/{userB}")
    public ArrayList<GatewayChats> getChatByConversation(@PathVariable String userA, @PathVariable String userB) throws SQLException {
        logger.log("\nStarting getChatByConversation method of user '" + userA + "' and '" + userB + "'", logType.TRACE);
        FinderChats fC = FinderChats.getInstance();
        ArrayList<GatewayChats> aL = fC.findByConversation(userA, userB);
        logger.log("Returning this messages...", logType.TRACE);
        String s = "";
        for (GatewayChats gC : aL) s = s + gC.json() + "\n";
        logger.log(s, logType.TRACE);
        logger.log("End of method", logType.TRACE);
        return fC.findByConversation(userA, userB);
    }

    /**
     * @brief Metode 'GET' que retorna tots els chats que l'usuari te
     * @param user nom de l'usuari
     * @return Retorna el llistat de chats
     */
    @GetMapping("/{user}")
    public ArrayList<String> getChatByConversation(@PathVariable String user) throws SQLException {
        logger.log("\nStarting getChatByConversation method with user '" + user + "'...", logType.TRACE);
        FinderChats fC = FinderChats.getInstance();
        DeletedChats dCS = new DeletedChats();
        ArrayList<String> usersChats = fC.findByUser(user);
        ArrayList<String> deletedChats = dCS.getDeletedChatsFromUser(user);
        usersChats.removeAll(deletedChats);
        logger.log("Returning this chats:", logType.TRACE);
        String log = "";
        for (String s : usersChats ) log = log + s + " ";
        logger.log(log, logType.TRACE);
        return usersChats;
    }

    /**
     * @brief Metode 'GET' que retorna tots els missatges d'un usuari de la base de dades
     * @param user nom de l'usuari
     * @return Retorna el llistat de missatges de l'usuari ordenats per data
     */
    @GetMapping("/messages/to/{user}")
    public ArrayList<GatewayChats> getChatByReceived(@RequestParam String user) throws SQLException {
        logger.log("Starting getChatByReceived method with user '" + user + "'", logType.TRACE);
        FinderChats fC = FinderChats.getInstance();
        ArrayList<GatewayChats> aL = fC.findByReceived(user);
        String log = "";
        for (GatewayChats gC : aL) log += gC.json() + "\n";
        logger.log(log, logType.TRACE);
        return aL;
    }

    /**
     * @brief Metode 'POST' que crea un missatge entre dos usuaris
     * @param sender nom de l'usuari que envia el missatge
     * @param receiver nom de l'usuari que rep el missatge
     * @param message contingut del missatge en si
     * @post El missatge s'ha creat correctament
     */
    @PostMapping("/messages/{sender}/{receiver}")
    public void postSendMessage(@PathVariable String sender, @PathVariable String receiver, @RequestParam String message) throws SQLException {
        logger.log("Starting sendMessage method with sender '" + sender + "' and reciever '" + receiver + "'...", logType.TRACE);
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        timestamp = timestamp.substring(0, timestamp.length() - 4);
        GatewayChats gC = new GatewayChats(sender, receiver, message,timestamp);
        logger.log("Inserting this chat: " + gC.json(), logType.TRACE);
        gC.insert();
        // TODO check if there are blocks between each other
        DeletedChats dCS = new DeletedChats();
        dCS.messageSent(sender, receiver);
    }

    /**
     * @brief Metode 'DELETE' que crea elimina el chat entre dos usuaris
     * @param userA Nom de l'usuari que elimina el xat
     * @param userB Nom de l'usuari sobre el que s'elimina el xat
     * @post El xat s'ha eliminat correctament
     */
    @DeleteMapping("/{userA}/{userB}")
    public void deleteChat(@PathVariable String userA, @PathVariable String userB) throws SQLException {
        logger.log("Starting deleteChat method with userA '" + userA + "' and userB '" + userB + "'", logType.TRACE);
        GatewayDeletedChats gDC = new GatewayDeletedChats(userA, userB);
        logger.log("Deleting this chats: " + gDC.json() ,logType.TRACE ) ;
        DeletedChats dCS = new DeletedChats();
        gDC.insert();
        dCS.deleteMessagesIfNeeded(userA, userB);
        logger.log("End of method", logType.TRACE);
    }



}
