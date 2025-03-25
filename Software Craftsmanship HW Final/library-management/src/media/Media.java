package media;

import java.util.HashMap;

public interface Media {
    String getID();
    String getTitle();
    String getAuthorID();
    // Hash map of information type (String, e.g. "genre") to information value (String, e.g. horror")
    HashMap<String, String> getInformation();
}
