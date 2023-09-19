
import java.util.ArrayList;
import java.util.List;

public class KnownTracks {

    private static KnownTracks theKnownTracks;
    private static List<Long> theTracks = new ArrayList<>();

    private KnownTracks() {
        // Avoid default constructor - by setting this private

    }

    static void add(Long trackId) {
        theTracks.add(trackId);
    }

    public static KnownTracks getInstance() {
        KnownTracks result = theKnownTracks;
        if (result != null) {
            return result;
        }
        synchronized (KnownTracks.class) {
            if (theKnownTracks == null) {
                theKnownTracks = new KnownTracks();
            }
            return theKnownTracks;
        }
    }

    public static boolean hasId(Long trackId) {
        return theTracks.contains(trackId);
    }

}
