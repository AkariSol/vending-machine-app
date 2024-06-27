import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(FORMATTER);
    }
}
