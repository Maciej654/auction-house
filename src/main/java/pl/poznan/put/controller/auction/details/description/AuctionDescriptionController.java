package pl.poznan.put.controller.auction.details.description;

import com.sandec.mdfx.MDFXNode;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class AuctionDescriptionController {
    @FXML
    private ScrollPane descriptionScrollPane;

    @FXML
    private void initialize() throws IOException, URISyntaxException {
        log.info("initialize");

        val sample   = Main.class.getResource("markdown/sample.md").toURI();
        val markdown = Files.readString(Paths.get(sample));
        setDescription(markdown);
    }

    public void setDescription(String description) {
        val node = new MDFXNode(description);
        node.getStylesheets().add("pl/poznan/put/css/markdown.css");
        descriptionScrollPane.setContent(node);
    }
}
