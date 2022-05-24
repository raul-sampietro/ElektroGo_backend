/**
 * @file BlocksController.java
 * @author Raül Sampietro
 * @date 24/05/2022
 * @brief Implementació de la classe BlocksController
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderBlock;
import elektroGo.back.data.gateways.GatewayBlock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RequestMapping("blocks")
@RestController
public class BlocksController {
    @GetMapping("/blocks/from/{username}")
    public List<GatewayBlock> blocksUser(@PathVariable String username) throws SQLException {
        System.out.println("\nStarting allBlocks method...");
        FinderBlock fR = FinderBlock.getInstance();
        return fR.findByUserBlocking(username);
    }
    @GetMapping("/blocks/to/{username}")
    public List<GatewayBlock> UserIsBlocked(@PathVariable String username) throws SQLException {
        System.out.println("\nStarting allBlocks method...");
        FinderBlock fR = FinderBlock.getInstance();
        return fR.findByBlockUser(username);
    }
}
