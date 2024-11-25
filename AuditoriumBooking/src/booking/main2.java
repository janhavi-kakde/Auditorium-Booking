package booking;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;



//login, signup


public class main2 {
  static Scanner sc = new Scanner(System.in);
  public static void main(String[] args) {
    //clubs are the nodes arranged in the form of a BST, ClubTree is the BST
    System.out.println();
    System.out.println();
    System.out.println("\t\t\t\t***Welcome to Cummins College Event management System***");
    System.out.println();
    System.out.println();
    ClubTree objClubTree = new ClubTree();
    Event eventManager = new Event(null);
    objClubTree.clubCreate("tnp cell", "tnp@cumminscollege.in", "tnp@123");
    homepage(objClubTree, eventManager);
    events(objClubTree, eventManager);
  } // main method ends
  public static void homepage(ClubTree objClubList, Event eventManager) {
    Club clubInstance;
    System.out.println("***Homepage***");
    System.out.println();
    System.out.print("choose 1. Signup  2. Login: ");
    System.out.println();
    int choiceSignIn = sc.nextInt();
    sc.nextLine(); // Consume newline
    switch (choiceSignIn) {
    case 1:
      objClubList.signup();
			
      homepage(objClubList, eventManager);
      break;
    case 2:
      clubInstance = objClubList.login();
      if (clubInstance == null) {
        System.out.println();
        System.out.println("Signup first");
        objClubList.signup();
        homepage(objClubList, eventManager);
      } else {
        System.out.println();
        System.out.println("Login successful");
        eventManager = new Event(clubInstance);
      }
      break;
    }
  }// homepage ends;
  public static void events(ClubTree objClubList, Event eventManager) {
    System.out.println("***Events page***");
    int ch = 0;
    do {
      System.out.println("1. Book event slot\n2. Display events\n3. View notifications\n0. Exit");
      ch = sc.nextInt();
      sc.nextLine(); // Consume newline
      switch (ch) {
      case 1:
        System.out.println("Enter event name:");
        String name = sc.nextLine();
        System.out.println("Type of your project: 1 for Club Event, 2 for TNP or Company Event");
        String type = sc.nextLine();
        // if(type!="1" &&type !="2" ){
        //   System.out.println("please enter valid choice");
        //   return;
        // }
        System.out.println("Enter date (DD/MM/YYYY):");
        String date = sc.nextLine();
        System.out.println("Enter start time (HH:MM):");
        String startTime = sc.nextLine();
        System.out.println("Enter end time (HH:MM):");
        String endTime = sc.nextLine();
        eventManager.addEvent(name, type, date, startTime, endTime);
        break;
      case 2:
        System.out.println("Displaying all scheduled events:");
        eventManager.displayEvents();
        break;
      case 3:
        // System.out.println("***Notifications of club: "+eventManager.club+ "***");
        // eventManager.club.viewNotifications();
        // break;
      case 0:
        homepage(objClubList, eventManager);
      default:
        break;
      }
    } while (ch != 5);
  }
}// main block class ends
// Club node class
class Club {
  Event eventManager = new Event(null);
 
  Scanner sc = new Scanner(System.in);
  String name;
  String lead;
  String password;
  Club next;
  Stack<Event.EventNode> notifications;
  Club(String name, String lead, String password) {
    this.name = name;
    this.lead = lead;
    this.password = password;
    notifications = new Stack<>();
  }
 
 
  void viewNotifications() {
    System.out.println("You have "+notifications.size()+" new notifications");
    int i=1;
    while(!notifications.isEmpty()) {
      Event.EventNode replacedEvent=notifications.peek();
      System.out.println(i+". " + replacedEvent.name+" is removed from "+replacedEvent.auditorium+" on "+replacedEvent.date);
      System.out.println("This time slot is already booked.");
      System.out.println(" 1. Change auditorium  2. Change date/time slot");
      int changech=sc.nextInt();
      sc.nextLine();
     
      switch(changech) {
      case 1:
         eventManager.addEvent(replacedEvent.name, replacedEvent.type, replacedEvent.date, replacedEvent.startTime, replacedEvent.endTime);
         break;
      case 2:
        System.out.println("Enter a new date (DD/MM/YYYY): ");
              String date = sc.nextLine();
              System.out.println("Enter a new start time (HH:MM): ");
              String startTime = sc.nextLine();
              System.out.println("Enter a new start time (HH:MM): ");


              String endTime = sc.nextLine();
             
              eventManager.addEvent(replacedEvent.name, replacedEvent.type, date, startTime, endTime);
      }
      notifications.pop();
    }
  }
 
 
}
// ClubList to manage clubs
class ClubTree {
  Club head;
  Scanner sc = new Scanner(System.in);
  void clubCreate(String name, String lead, String password) {
    Club club = new Club(name, lead, password);
    if (head == null) {
      head = club;
    } else {
      Club ptr = head;
      while (ptr.next != null) {
        ptr = ptr.next;
      }
      ptr.next = club;
    }
  }
  void signup() {
    System.out.print("Enter Club name: ");
    String name = sc.nextLine();
    System.out.print("Enter Club lead email: ");
    String lead = sc.nextLine();
    System.out.print("Enter password: ");
    String password = sc.nextLine();
    System.out.println();
		System.out.println("Signed up sucessesfully , now login");
    System.out.println();
    clubCreate(name, lead, password);
  }
  Club search(String name, String lead, String password) {
    Club ptr = head;
    while (ptr != null) {
      if (ptr.name.equals(name) && ptr.lead.equals(lead) && ptr.password.equals(password)) {
        return ptr;
      }
      ptr = ptr.next;
    }
    return null;
  }
  Club login() {
    System.out.print("Enter Club name: ");
    String name = sc.nextLine();
    System.out.print("Enter Club lead email: ");
    String lead = sc.nextLine();
    System.out.print("Enter password: ");
    String password = sc.nextLine();
		
    return search(name, lead, password);
  }
 
}
//Event management class
class Event {
  Scanner sc = new Scanner(System.in);
  Club club;
  // Define auditorium capacities
  Map<String, Integer> auditoriumCapacities = Map.of("KB Joshi Audi", 100, "Mech Audi", 80, "Instru Audi", 50);
  class EventNode {
    String name;
    String type;
    int priority;
    String date;
    String startTime;
    String endTime;
    String auditorium;
    int capacity; // capacity required for the event
    EventNode next;
    EventNode(String name, String type, String date, String startTime, String endTime, String auditorium,int capacity) {
      this.name = name;
      this.type = type;
      this.date = date;
      this.startTime = startTime;
      this.endTime = endTime;
      this.auditorium = auditorium;
      this.capacity = capacity;
      this.priority = type.equals("2") ? 1 : 2;
      this.next = null;
    }
  }
  EventNode lastEvent = null;
  Map<String, Map<String, EventNode>> auditoriumSchedule = new HashMap<>();
  Event(Club clubInstance) {
    this.club = clubInstance;
    auditoriumSchedule.put("KB Joshi Audi", new HashMap<>());
    auditoriumSchedule.put("Mech Audi", new HashMap<>());
    auditoriumSchedule.put("Instru Audi", new HashMap<>());
  }
 
  public void addEvent(String name, String type, String date, String startTime, String endTime) {
    System.out.println("Enter the number of students for this event:");
    int eventCapacity = sc.nextInt();
    sc.nextLine(); // Consume newline


    // Prompt user to select an auditorium by number
    System.out.println("Choose an Auditorium: 1 for KB Joshi Audi, 2 for Mech Audi, 3 for Instru Audi");
    int auditoriumChoice = sc.nextInt();
    sc.nextLine(); // Consume newline
    String auditorium = "";


    switch (auditoriumChoice) {
        case 1: auditorium = "KB Joshi Audi"; break;
        case 2: auditorium = "Mech Audi"; break;
        case 3: auditorium = "Instru Audi"; break;
        default:
            System.out.println("Invalid choice. Please select a valid auditorium.");
            return;
    }


    // Check auditorium capacity
    int auditoriumCapacity = auditoriumCapacities.getOrDefault(auditorium, 0);
    if (eventCapacity > auditoriumCapacity) {
        System.out.println("The selected auditorium doesn't have enough capacity for this event.");
        System.out.println("Available auditoriums that can accommodate " + eventCapacity + " students:");
        for (Map.Entry<String, Integer> entry : auditoriumCapacities.entrySet()) {  
            if (entry.getValue() >= eventCapacity) {
                System.out.println(entry.getKey() + " with capacity " + entry.getValue());
            }
        }
        return;
    }


    // Proceed with event scheduling
    Map<String, EventNode> schedule = auditoriumSchedule.get(auditorium);
    String dateTimeKey = date + " " + startTime;


    // Check for time conflicts with existing events
    boolean conflict = false;
    for (EventNode event : schedule.values()) {
        if (event.date.equals(date) && (
                (startTime.compareTo(event.startTime) >= 0 && startTime.compareTo(event.endTime) < 0) ||
                (endTime.compareTo(event.startTime) > 0 && endTime.compareTo(event.endTime) <= 0) ||(startTime.compareTo(event.startTime)<=0 && (endTime.compareTo(event.endTime)>=0))
                // ||((!endTime.equals(event.startTime))&&(startTime.compareTo(event.endTime)<0))
        )) {
            // If event type 2 has higher priority, replace the existing event
            if (type.equals("2") && event.priority != 1) {
                schedule.remove(event.date + " " + event.startTime);
                System.out.println("Replaced lower-priority event: " + event.name);
                removeEventFromLinkedList(event);
            } else {
                conflict = true;
                System.out.println("This time slot is already booked by another event.");
                System.out.println(" 1. Change auditorium  2. Change date/time slot");
int changeChoice = sc.nextInt();
sc.nextLine();


switch(changeChoice) {
    case 1:
        addEvent(name, type, date, startTime, endTime);  // Recurse if auditorium is changed
        break;
    case 2:
        // Change the date/time and retry
        System.out.println("Enter a new date (DD/MM/YYYY): ");
        date = sc.nextLine();
        System.out.println("Enter a new start time (HH:MM): ");
        startTime = sc.nextLine();
        System.out.println("Enter a new end time (HH:MM): ");
        endTime = sc.nextLine();
        addEvent(name, type, date, startTime, endTime);  // Recurse if date/time is changed
        break;
    default:
        System.out.println("Invalid choice. Event could not be scheduled.");
        return;
}
                return;
            }
        }
    }
		if (!isValidDate(date) || !isValidTime(startTime) || !isValidTime(endTime)) {
			System.out.println("Invalid date or time format.");
			return;
	}


    // If no conflict, proceed with scheduling the event
    if (!conflict) {
        EventNode newEvent = new EventNode(name, type, date, startTime, endTime, auditorium, eventCapacity);
        schedule.put(dateTimeKey, newEvent);


        // Create and add the new event to the circular linked list
        if (lastEvent == null) {
            lastEvent = newEvent;
            lastEvent.next = lastEvent; // Circular link
        } else if (newEvent.priority == 1) {
            newEvent.next = lastEvent.next;
            lastEvent.next = newEvent;
            lastEvent = newEvent;
        } else {
            EventNode current = lastEvent;
            while (current.next != lastEvent.next && current.next.priority == 1) {
                current = current.next;
            }
            newEvent.next = current.next;
            current.next = newEvent;
            if (current == lastEvent)
                lastEvent = newEvent;
        }
    }
		System.out.println("Event successfully added to schedule.");
}

private boolean isValidDate(String date) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	dateFormat.setLenient(false);
	try {
			Date eventDate = dateFormat.parse(date);
			return !eventDate.before(new Date());
	} catch (ParseException e) {
			return false;
	}
}
private boolean isValidTime(String time) {
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	timeFormat.setLenient(false);
	try {
			timeFormat.parse(time);
			return true;
	} catch (ParseException e) {
			return false;
	}
}


  private void removeEventFromLinkedList(EventNode eventToRemove) {
    if (lastEvent == null)
      return;
    EventNode current = lastEvent;
    do {
      if (current.next == eventToRemove) {
        current.next = eventToRemove.next;
        if (eventToRemove == lastEvent) {
          lastEvent = (eventToRemove == lastEvent.next) ? null : current;
        }
        break;
      }
      current = current.next;
    } while (current != lastEvent);
  }
  private void notifyClub(EventNode event) {
    if (club != null) {  // Check if club is initialized
        System.out.println("Notification: The event '" + event.name + "' has been removed due to higher-priority booking.");
        // Add actual notification handling logic here, e.g., club.notifications.add(event.name);
    } else {
        System.out.println("Notification system is not set up. Could not notify the club about the removal of event '" + event.name + "'.");
    }
}


//
public void displayEvents() {
  if (lastEvent == null) {
      System.out.println("No events available.");
      return;
  }

  // Print the table header
  System.out.println("Event Name\t\tType\t\tDate\t\tTime\t\t\tAuditorium\tCapacity");
  System.out.println("-----------------------------------------------------------------------------------------------------------------");

  EventNode temp = lastEvent.next;
  do {
      // Print each row, separating columns with tabs
      System.out.println(
          temp.name + "\t\t\t" +
          temp.type + "\t\t" +
          temp.date + "\t" +
          temp.startTime + " - " + temp.endTime + "\t\t" +
          temp.auditorium + "\t\t" +
          temp.capacity
      );
      temp = temp.next;
  } while (temp != lastEvent.next);
}
}


