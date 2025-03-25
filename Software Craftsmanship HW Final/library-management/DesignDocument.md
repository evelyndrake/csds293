# Assignment 12 Design Document
- Evelyn Drake
- Software Craftsmanship
- 12/6/24
## Architecture
### Library package
#### Library
- This class is responsible for storing the software's data structures, which are all `ConcurrentHashMaps`
  - `Media` - Books, audiobooks, etc.
  - `Patrons` - Members of the library
  - `Authors`- Authors corresponding to books
  - `MediaInstances` - Physical copies of media (e.g. the library can have multiple copies of one book)
  - `Loans` - Instances that are currently unavailable, as they have been loaned to a patron
  - `Holds` - Instances that are currently unavailable, as they have been held for a patron
- It uses an `ExecutorService` to perform put/remove operations in a thread-safe, concurrent manner
- The methods that add or remove data to the library all return `Futures`
- Notably, these methods are protected, and they should only be accessed through the `TransactionManager` class
- Manages overdue loans and expired holds
- Data persistence
  - Because I used hashmaps to store all of the data, the data can (theoretically) be saved and restored by writing/reading the maps to a file using `ObjectOutputStream` and `ObjectInputStream`
  - However, I didn't implement this as it was outside the scope of the assignment
#### TransactionManager
- This class actually performs the library class's thread-safe operations
  - Handles media, patrons, authors, media instances, loans, and holds
  - Create, update, and delete operations
- It also handles authentication, preventing certain users from performing certain operations
  - `loginAs(LoginType.LIBRARIAN or LoginType.PATRON)`
#### Loan
- Represents a physical copy of a book that has been loaned to a specific patron
- Implements `Expires`, an interface that handles:
  - `isOverdue`
  - `getDaysOverdue`
  - `getDateCreated`
  - `getDateOfReturn`
- These methods are used for the fee system
#### Hold
- Represents a physical copy of a book that is on hold to a specific person
- Also implements `Expires`
- Very similar to `Loan`
#### Patron
- Represents a patron of the library
- First name, last name, date of birth, date of registration
- Has a `HashMap` of `Fees`, which are added using `addFee`, which takes an object implementing `Expires`
- Can be suspended, preventing the patron from checking out books
- Can pay off the fees, find the total amount owed, and find the total amount paid off
- Could be extended to automatically suspend patrons with a specific outstanding balance
- Could be extended to dynamically generate objects when a user enters their information in the software
#### Fee
- Represents a fee placed on a patron
- Has a method to calculate the fee based on the `Expires` object's number of days overdue up to a specific max fee
### Media package
#### Author
- Represents the author of a specific instance of `Media`
- Name, biography, birthdate
- Activity status, defaults to true but could be extended to update to false if their last release was past a certain date
#### Media
- An interface where classes implementing it are specific types of media (book, audiobook, etc.)
- All media must have a title and author ID
- It also includes a `HashMap` of information, allowing specific media types to store additional data
  - Books have a genre and description field stored here
  - Audiobooks have a narrator, length, and genre field stored here
#### Book and AudioBook
- Again, just implementations of the `Media` interface
- These are not physical copies of the media, just an entry corresponding to its information
#### MediaInstance
- Represents a physical copy of a piece of media
- Can have a `MediaInstanceCondition` (good, medium, or poor)
- Refers to a `Media` class's ID
### Info package
#### LoggerSystem
- Can log and display information, errors, or warnings, which are called from other classes
- Can also handle and display exceptions
- Has booleans to enable/disable info and error logging
#### LibraryObserver
- When transactions are performed from `TransactionManager`, calls are made to this class to track analytics
- Logs each piece of media's popularity and its availability (number of `MediaInstances`)
- Displays information when a book's popularity increases past a threshold or its number of copies is too low
- Tracks changes on adding/removing copies and checking out media
#### Notification, NotificationManager, NotificationEmail/NotificationSMS
- `Notification` - Interface that describes a type of notification that can be sent
- `NotificationManager`
  - Manages the library's entire notification system
  - Uses a `ConcurrentLinkedQueue` of notifications for thread safety
  - Can add notifications with a specific message and recipient
  - This is used to alert administrators about issues and inform patrons about fees/cancellations
- `NotificationEmail` and `NotificationSMS`
  - Just implement the `sendNotification` method of the `Notification` interface
- Right now, the notification system sends an SMS and an email for all notifications
- Could be extended to have patrons define their contact preferences and send notifications based on those
### Exceptions package
#### HoldException, LoanException, MediaException
- Represent exceptions that are thrown and passed up until they can be handled gracefully
- It is self-explanatory where these are called from
- Some classes will pass up other types of exceptions, such as `IllegalArgumentExceptions`
- These are sent to the `LoggerSystem` to be handled
## Tests
- The tests achieve 100% instruction coverage and 100% branch coverage when running `ant report/test` locally
- However, when pushing to the remote server, I only get 99.92% instruction coverage and 99.65% branch coverage
- I'm not sure if that's a rounding issue or what, but all the tests pass either way
- Tests all methods with good data and bad data
- Also performs stress tests
- Because almost all of the components rely on each other to function, my design lent itself to integration testing in addition to regular unit tests