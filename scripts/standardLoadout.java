import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public class standardLoadout {

    public static void main(String[] args) throws Exception {
        Loadout standard = createStandard();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = mapper.writeValueAsString(standard);
        System.out.println(json);

        java.nio.file.Files.writeString(
                java.nio.file.Path.of("src/main/resources/Loadouts/Standard.json"),
                json
        );
    }

    private static Loadout createStandard() {
        java.util.List<LoadoutEntry> entries = new java.util.ArrayList<>();
        String packId = "StandardChessPieces";
        int backLines = 7;
        int pawnsLines = 6;

        entries.add(new LoadoutEntry(new Point2D(0, backLines), packId, "rook"));
        entries.add(new LoadoutEntry(new Point2D(1, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(2, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(3, backLines), packId, "queen"));
        entries.add(new LoadoutEntry(new Point2D(4, backLines), packId, "king"));
        entries.add(new LoadoutEntry(new Point2D(5, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(6, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(7, backLines), packId, "rook"));   

        for (int x = 0; x < 8; x++) {
            entries.add(new LoadoutEntry(new Point2D(x, pawnsRank), packId, "pawn"));
        }

        return Loadout.create("Standard Chess", entries);
    }
}
