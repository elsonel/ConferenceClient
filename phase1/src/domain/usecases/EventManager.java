package domain.usecases;

import domain.entities.User;
import domain.entities.Event;

import java.io.Serializable;
import java.util.ArrayList;

public class EventManager implements Serializable {

    private HashMapManager registered;
    private Integer eventId = 1;
    private int maxPeople = 2;

    public EventManager() {
        this.registered = new HashMapManager();
    }

    /**
     * Gets the Event object given the eventID
     * @param id    the id of the Event
     * @return      the Event object
     */

    public Event getEventByID(int id) {
        if(!this.registered.getKeySet().contains(id)){
            return null;
        }
        return this.registered.getEventById(id);
    }

    /**
     * Check the time conflict between the existing Events and the time given.
     * @param eventDuration    the time of duration
     * @param  eventTime       the starting time
     * @return      true if there is no conflict, false if there is conflict
     */

    public boolean checkConflict(int eventDuration, int eventTime){
        for (Integer i: this.registered.getKeySet()){
            Event e = this.registered.getEventById(i);
            int start = e.getEventTime();
            int end = start + e.getEventDuration();
            if (checkConflictHelper(start, end, eventDuration, eventTime)){
                return false;
            }
        }
        return true;
    }

    private boolean checkConflictHelper(int start, int end, int eventDuration, int eventTime){
        if (start <= eventTime && eventTime <= end){
            return true;
        }
        if (start <= eventTime + eventDuration && eventTime + eventDuration <= end){
            return true;
        }
        return false;
    }

    /**
     * Creates an Event holding the given name, starting time, and duration.
     * @param eventDuration    the length of time of the Event
     * @param eventTime    the starting time of the Event
     * @param eventName    the name of the Event
     * @return      true if creating Event is successful, false if there is time conflict
     */

    public boolean createEvent(int eventDuration, int eventTime, String eventName){
        if(this.checkConflict(eventDuration, eventTime)) {
            Event e = new Event(this.eventId, eventName, eventDuration, eventTime );
            this.registered.addEvent(this.eventId, eventDuration, eventTime, e);
            this.eventId += 1;
            return true;
        }
        return false;
    }

    /**
     * Reschedules an Event to the given time and given duration, given the eventID.
     * The old Event will get deleted and a new Event will get created, the eventID will change.
     * @param id    the id of the Event
     * @param eventDuration    the new length of time of the Event
     * @param eventTime the new starting time of tje Event
     * @return      true if rescheduling the target Event is successful, false if the eventId does not exist,
     *              or there is time conflict.
     */

    public boolean rescheduleEvent(int id, int eventDuration, int eventTime){
        if (!this.registered.ifInKeySet(id)){
            return false;
        }
        Event e = this.registered.getEventById(id);
        String eventName = e.getEventName();
        this.cancelEvent(id);
        if(this.createEvent(eventDuration, eventTime, eventName) == false){
            return false;
        }
        return true;
    }

    /**
     * Gets the list of all Events
     * @return      an ArrayList of all Events
     */

    public ArrayList<Event> getAllEvents(){
        ArrayList<Event> listEvent = new ArrayList<>();
        for (Integer i: this.registered.getKeySet()){
            Event e = this.registered.getEventById(i);
            listEvent.add(e);
        }
        return listEvent;
    }

    /**
     * Adds the given User to the Event's attendee list corresponding to the given eventID
     * @param id    the id of the Event
     * @param u    an User object
     * @return      true if booking is successful, false if the eventID does not exist, or the
     *              User is already on the attendee list, or the room is full.
     */

    public boolean bookForAttendee(User u, int id){
        if (!this.registered.ifInKeySet(id)){
            return false;
        }
        ArrayList<User> booked = this.registered.getAttendeeById(id);
        if (booked.size() < this.maxPeople){
            if (booked.contains(u)){
                return false;
            }
            booked.add(u);
            this.registered.updateAttendee(id, booked);
            return true;
        }
        return false;
    }

    /**
     * Removes the given User from the Event's attendee list corresponding to the given eventID
     * @param id    the id of the Event
     * @param u    an User object
     * @return      true if unbooking is successful, false if the eventID does not exist,
     *              or the User is not in the attendee's list.
     */

    public boolean unBookForAttendee(User u, int id){
        if (!this.registered.ifInKeySet(id)){
            return false;
        }
        ArrayList<User> booked = this.registered.getAttendeeById(id);
        if (!booked.contains(u)){
            return false;
        }
        booked.remove(u);
        this.registered.updateAttendee(id, booked);
        return true;
    }

    /**
     * Adds the given User to the Event's speaker list corresponding to the given eventID
     * @param id    the id of the Event
     * @param u    an User object
     * @return      true if booking is successful, false if the eventID does not exist,
     *              or the User's usertype is not speaker, or there is already a speaker for this Event.
     */

    public boolean bookSpeaker(User u, int id){
        if (!this.registered.ifInKeySet(id)){
            return false;
        }
        if (!u.getUserType().equals("SPEAKER")){
            return false;
        }
        if (this.registered.updateSpeaker(id, u)){
            return true;
        }
        return false;
    }

    /**
     * cancels the Event corresponding to the given eventID
     * @param id    the id of the Event
     * @return      true if cancelling is successful, false if the eventID does not exist.
     */

    public boolean cancelEvent(int id){
        if (!this.registered.ifInKeySet(id)){
            return false;
        }
        this.registered.cancelEventById(id);
        return true;
    }

    /**
     * gets the eventID corresponding to the given Event object
     * @param e     an Event object
     * @return      the eventID in Integer
     */

    public Integer getIdByEvent(Event e){
        for (int i: this.registered.getKeySet()){
            if (this.registered.getEventById(i) == e){
                return i;
            }
        }
        return 0;
    }

    /**
     * Gets all the booked attendees corresponding to the given eventID
     * @param id    the id of the Event
     * @return      an ArrayList of all Users attending the Event, null if the eventID does not exist
     */

    public ArrayList<User> getAttendeesById(int id){
        if (!this.registered.getKeySet().contains(id)){
            return null;
        }
        return this.registered.getAttendeeById(id);
    }

    /**
     * Gets all the booked speakers corresponding to the given eventID
     * @param id    the id of the Event
     * @return      an ArrayList of all Users speaking at the Event, null if the eventID does not exist
     */

    public ArrayList<User> getSpeakerById(int id){
        if (!this.registered.getKeySet().contains(id)){
            return null;
        }
        return this.registered.getSpeakerById(id);
    }

    /**
     * Gets the eventIDs of all the Events that the given User will attend
     * @param u    an User object
     * @return      an ArrayList of eventIDs corresponding to the Events that the User will attend
     */

    public ArrayList<Integer> getEventIdsByUser(User u){
        ArrayList<Integer> listOfEventId = new ArrayList<>();
        for (Integer i: this.registered.getKeySet()){
            if (this.registered.getAttendeeById(i).contains(u)){
                listOfEventId.add(i);
            }
        }
        return listOfEventId;
    }

    /**
     * Gets the eventName of the Event corresponding to the given eventID
     * @param id    the id of the Event
     * @return      a String that contains the eventName, null if the eventID does not exist
     */

    public String getEventNameById(int id){
        if (!this.registered.getKeySet().contains(id)){
            return null;
        }
        return this.registered.getEventById(id).getEventName();
    }

    /**
     * Gets the eventID of the Event corresponding to the given eventName
     * @param name    the name of the Event
     * @return      an Integer that contains the eventID, null if the eventName does not exist
     */

    public Integer getEventIdByName(String name){
        for (Integer id: this.registered.getKeySet()){
            if (this.registered.getEventById(id).getEventName() == name){
                return id;
            }
        }
        return null;
    }

}
