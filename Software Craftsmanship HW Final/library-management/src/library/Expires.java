package library;

import java.util.Date;

// This interface represents an object that can expire (has a created date and a due date), e.g. a loan or hold
public interface Expires {
    boolean isOverdue();
    int getDaysOverdue();
    Date getDateCreated();
    Date getDateOfReturn();
}
