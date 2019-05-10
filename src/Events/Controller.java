package Events;

class EventSet {
	private Event[] events = new Event[300];
	private int index = 0;
	private int next = 0;

	public void add(Event event) {
		if (index >= events.length)
			return;
		events[index++] = event;
	}

	public Event getNext() {
		boolean looped = false;
		int start = next;
		do {
			next = (next + 1) % events.length;

			if (start == next)
				looped = true;

			if ((next == (start + 1) % events.length) && looped)
				return null;
		} while (events[next] == null);
		return events[next];
	}

	public void removeCurrent() {
		events[next] = null;
	}
}

public class Controller {
	private EventSet eventSetter = new EventSet();

	public void addEvent(Event newEvent) {
		eventSetter.add(newEvent);
	}

	public void run() {
		Event event;
		while ((event = eventSetter.getNext()) != null) {
			if (event.ready()) {
				event.action();
				System.out.println(event.description());
				eventSetter.removeCurrent();
			}
		}
	}
}
