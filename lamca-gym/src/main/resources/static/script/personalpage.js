document.addEventListener('DOMContentLoaded', function() {
  var currentEvent = null; // Håller reda på det aktuella eventet för bokning/avbokning

  // Generera evenemang
  function generateEvents() {
    let startDate = new Date();
    let events = [];
    let eventTypes = [
      { title: 'Yoga', startHour: 7, endHour: 8, endMinute: 30, maxCapacity: 20, instructor: 'Arta'  },
          { title: 'Yoga', startHour: 8, startMinute: 30, endHour: 10, endMinute: 0, maxCapacity: 20,  instructor: 'Chung'  },
          { title: 'Yoga', startHour: 10, endHour: 11, endMinute: 30, maxCapacity: 20,  instructor: 'Arta'  },
          { title: 'Gruppträning', startHour: 11, startMinute: 30,  endHour: 13, endMinute: 0, maxCapacity: 20,  instructor: 'Lars'  },
          { title: 'Gruppträning', startHour: 13, endHour: 14, endMinute: 30, maxCapacity: 20,  instructor: 'Lars'  },
          { title: 'Gruppträning', startHour: 14, startMinute: 30, endHour: 16, endMinute: 0, maxCapacity: 20,  instructor: 'Anders'  },
          { title: 'Tabata', startHour: 16, startMinute: 0, endHour: 17, endMinute: 0, maxCapacity: 20,  instructor: 'Chung'  },
          { title: 'Tabata', startHour: 17, startMinute: 0, endHour: 18, endMinute: 0, maxCapacity: 20,  instructor: 'Anders'  },
          { title: 'Spinning', startHour: 18, startMinute: 0, endHour: 19, endMinute: 0, maxCapacity: 20,  instructor: 'Mickey'  },
          { title: 'Spinning', startHour: 19, startMinute: 0, endHour: 20, endMinute: 0, maxCapacity: 20,  instructor: 'Mickey'  },
    ];
    let daysToAdd = 7;
    let eventIdCounter = 1;

    for (let day = 0; day < daysToAdd; day++) {
      let date = new Date(startDate);
      date.setDate(startDate.getDate() + day);

      eventTypes.forEach((eventType) => {
        let start = new Date(date);
        start.setHours(eventType.startHour, eventType.startMinute || 0, 0);

        let end = new Date(date);
        end.setHours(eventType.endHour, eventType.endMinute || 0, 0);

        events.push({
          id: `event-${eventIdCounter++}`,
          title: eventType.title,
          start: start.toISOString(),
          end: end.toISOString(),
          instructor: eventType.instructor,
          maxCapacity: eventType.maxCapacity,
          booked: Math.floor(Math.random() * (eventType.maxCapacity + 1)), // För demonstration
          isBooked: false
        });
      });
    }

    localStorage.setItem('events', JSON.stringify(events));
    return events;
  }

  // Hämta evenemang
  function getEvents() {
    let storedEvents = localStorage.getItem('events');
    if (storedEvents) {
      return JSON.parse(storedEvents);
    } else {
      return generateEvents();
    }
  }

  // Boka evenemang
  function bookEvent() {
    let events = getEvents();
    let event = events.find(e => e.id === currentEvent.id);
    if (event) {
      event.isBooked = true;
      localStorage.setItem('events', JSON.stringify(events));
      alert("Passet har bokats.");
      window.location.reload();
    }
  }

  // Avboka evenemang
  function cancelBooking() {
    let events = getEvents();
    let event = events.find(e => e.id === currentEvent.id);
    if (event) {
      event.isBooked = false;
      localStorage.setItem('events', JSON.stringify(events));
      alert("Passet har avbokats.");
      window.location.reload();
    }
  }

  // Skapa kalendern
  var calendarEl = document.getElementById('calendar');
  var calendar = new FullCalendar.Calendar(calendarEl, {
    columnWidth: 100,
    initialView: 'timeGridWeek',
    firstDay: 1,
    slotMinTime: "07:00:00",
    slotMaxTime: "20:15:00",
    slotDuration: "00:10:00",
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'timeGridDay,timeGridWeek,dayGridMonth'
    },
    events: getEvents(),
    eventClick: function(info) {

      currentEvent = info.event; // Sätt nuvarande event till det klickade eventet
      document.getElementById('modalTitle').textContent = currentEvent.title;
      document.getElementById('modalDescription').textContent = `Instruktör: ${currentEvent.extendedProps.instructor}`;
      let bookBtn = document.getElementById('bookBtn');
      let cancelBtn = document.getElementById('cancelBtn');
      bookBtn.style.display = currentEvent.extendedProps.isBooked ? 'none' : 'inline-block';
      cancelBtn.style.display = currentEvent.extendedProps.isBooked ? 'none':'inline-block' ;
      document.getElementById('eventModal').style.display = 'block';
    },

    eventClassNames: function(arg) {
      return [arg.event.extendedProps.booked >= arg.event.extendedProps.maxCapacity ? 'full' : 'available'];
  },

    eventContent: function(arg) {
      // Skapa en övergripande container
      var eventElement = document.createElement('div');
      eventElement.classList.add('fc-event-custom');
    
      // Kontrollera om evenemanget är fullbokat
      var isFull = arg.event.extendedProps.booked >= arg.event.extendedProps.maxCapacity;
      eventElement.classList.add(isFull ? 'full' : 'available');
    
      var titleElement = document.createElement('div');
      titleElement.classList.add('fc-event-title');
      titleElement.innerHTML = `<strong>${arg.event.title}</strong>`;
      eventElement.appendChild(titleElement);
    
      var idElement = document.createElement('div');
      idElement.classList.add('fc-event-id');
      idElement.textContent = `ID: ${arg.event.id}`;
      eventElement.appendChild(idElement);
    
      var timeElement = document.createElement('div');
      timeElement.classList.add('fc-event-time');
      var startTime = FullCalendar.formatDate(arg.event.start, { hour: '2-digit', minute: '2-digit', hour12: false });
      var endTime = FullCalendar.formatDate(arg.event.end, { hour: '2-digit', minute: '2-digit', hour12: false });
      timeElement.textContent = `${startTime} - ${endTime}`;
      eventElement.appendChild(timeElement);
    
      var instructorElement = document.createElement('div');
      instructorElement.classList.add('fc-event-instructor');
      instructorElement.textContent = `Instruktör: ${arg.event.extendedProps.instructor}`;
      eventElement.appendChild(instructorElement);
    
      var bookedElement = document.createElement('div');
      bookedElement.classList.add('fc-event-booked');
      bookedElement.textContent = `Bokade: ${arg.event.extendedProps.booked}/${arg.event.extendedProps.maxCapacity}`;
      eventElement.appendChild(bookedElement);
    
      return { domNodes: [eventElement] };
    }
    
  });

  calendar.render();

  // Eventlyssnare för bokningsknappar
  document.getElementById('bookBtn').addEventListener('click', bookEvent);
  document.getElementById('cancelBtn').addEventListener('click', cancelBooking);

  // Hantera stängning av modal
  document.querySelector('.close').addEventListener('click', function() {
    document.getElementById('eventModal').style.display = 'none';
  });

  // Stäng modalen om användaren klickar utanför den
  window.addEventListener('click', function(event) {
    var modal = document.getElementById('eventModal');
    if (event.target == modal) {
      modal.style.display = 'none';
    }
  });
});

