/**
 * @file BlocksController.java
 * @author Raül Sampietro
 * @date 24/05/2022
 * @brief Implementació de la classe BlocksController
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderBlock;
import elektroGo.back.data.gateways.GatewayBlock;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RequestMapping("blocks")
@RestController
public class BlocksController {
    @GetMapping("/from/{username}")
    public List<GatewayBlock> blocksUser(@PathVariable String username) throws SQLException {
        System.out.println("\nStarting allBlocks method...");
        FinderBlock fR = FinderBlock.getInstance();
        return fR.findByUserBlocking(username);
    }
    @GetMapping("/to/{username}")
    public List<GatewayBlock> UserIsBlocked(@PathVariable String username) throws SQLException {
        System.out.println("\nStarting allBlocks method...");
        FinderBlock fR = FinderBlock.getInstance();
        return fR.findByBlockUser(username);
    }


    @PostMapping("/{username}/block/{userBlock}")
    public void BlockUser(@PathVariable String username,@PathVariable String userBlock) throws SQLException {
        System.out.println("\nBlocking method...");
        GatewayBlock gD = new GatewayBlock(username, userBlock);
        gD.insert();
    }
}
