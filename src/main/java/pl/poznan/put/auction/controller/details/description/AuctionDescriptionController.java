package pl.poznan.put.auction.controller.details.description;

import com.sandec.mdfx.MDFXNode;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import lombok.extern.slf4j.Slf4j;
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

        var sample   = Main.class.getResource("markdown/sample.md").toURI();
        var markdown = Files.readString(Paths.get(sample));
        var node     = new MDFXNode(markdown);
        node.getStylesheets().add("pl/poznan/put/css/markdown.css");
        descriptionScrollPane.setContent(node);
    }

    public void hello() {
        log.info("hello");
    }
}
