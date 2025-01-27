# Calendar

## A calendar app that can tell whether your friend is valid

### Description
When I joined a study group for CPSC 110, I found it 
difficult to efficiently track when the members of the group were
available. Each person had a different schedule, and I struggled to
find an ideal app for sharing schedules. This is why I decided to 
create this calendar project. 

In this calendar project, users can not only *view*, *modify*, and *delete* 
their own calendars but also search for *shared free time* among other
users and themselves.


### User Stories
- As a user, I want to be able to load and view my calendar, including the newly added one and previous one.
- As a user, I want to be able to add an myEvent to my calendar, and choose to save it or not.
- As a user, I want to be able to delete an myEvent from my calendar, and choose to remove it from the data or not.
- As a user, I want to be able to check my friend's available time.

(calendar is a list of myEvents in this project)

### Phase 4: Task 2
Logged Events:

Sun Mar 31 18:55:31 PDT 2024
Added to Calendar: test, test1, 5, 3, 1200, 1400

Sun Mar 31 18:55:35 PDT 2024
Added to Calendar: test, test1, 5, 3, 1200, 1400

Sun Mar 31 18:55:54 PDT 2024
Added to Calendar: han, study, 5, 2, 1200, 1300

Sun Mar 31 18:55:59 PDT 2024
Added to Calendar: test, test1, 5, 3, 1200, 1400

Sun Mar 31 18:55:59 PDT 2024
Added to Calendar: han, study, 5, 2, 1200, 1300

Sun Mar 31 18:56:20 PDT 2024
Added to Calendar: han, lunch, 4, 5, 1400, 1500

Sun Mar 31 18:56:39 PDT 2024
Removed from Calendar: han, study, 5, 2, 1200, 1300

### Phase 4: Task 3
By drawing out the UML graph of my project, I noticed that there are many classes in my project has a field of Calendar
and the methods in the calendar are also called in these classes to modify the calendar, which will increase the direct
coupling between calendar and all these classes.

To improve the design of my project, I think I might be able to integrate the Observer design pattern with the hope of 
reduce the coupling. When the calendar status changed, all the classes should be notified and update the status instead
of making the changes to the same object in each class.