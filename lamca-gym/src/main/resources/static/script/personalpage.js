<<<<<<< HEAD
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
    //localStorage.setItem('events', JSON.stringify(events));
    return events;
  }
=======

>>>>>>> Master
  // Hämta evenemang från servern
  function fetchEvents(fetchInfo, successCallback, failureCallback) {
      fetch('/sessions/all')
          .then(response => response.json())
          .then(sessions => {
              const events = sessions.map(session => {
                  return {
                      id: session.sessionId, // Antag att din backend använder 'sessionId' som fält
                      title: session.sessionType, // Anpassa enligt ditt datamodell, kanske 'sessionType'
                      start: session.time, // Formatet måste matcha ISO 8601, t.ex. "2020-09-01T12:00:00"
                      end: calculateEndTime(session.time, session.duration), // Du behöver beräkna slutet baserat på starttid och varaktighet
                      instructor: session.instructor,
                      capacity: session.capacity,
                  };
              });
              successCallback(events); // Använd successCallback för att skicka eventen till kalendern
          })
          .catch(error => {
              console.error('Error fetching sessions:', error);
              failureCallback(error); // Använd failureCallback vid fel
          });
  }

  function calculateEndTime(startTime, duration) {
      // Antag att 'duration' är i minuter och 'startTime' är en sträng i ISO 8601-format
      let start = new Date(startTime);
      return new Date(start.getTime() + duration * 60000).toISOString(); // Lägg till 'duration' i minuter till starttiden
  }

<<<<<<< HEAD
  // Boka evenemang
  /*function bookSession(sessionId) {
    var sessionId = event.target.getAttribute('bookBtn');
    fetch(`/sessions/book/${sessionId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // Inkludera headers för autentisering om så krävs
      }
    })
    .then(response => {
      if (response.ok) {
        alert('Session bokad.');
        // Uppdatera kalendern här
      } else {
        alert('Kunde inte boka sessionen.');
      }
    });
  }*/
  function bookSession(sessionId) {
       if (!currentEvent) {
          console.error('Not such event.');
          return;
      }
      var sessionId = currentEvent.id;
      fetch(`/bookings/?sessionId=${sessionId}`, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
              // Inkludera headers för autentisering om så krävs
          }
      })
      .then(response => {
          if (response.ok) {
              alert('Session bokad.');
              // Uppdatera kalendern här
          } else {
              alert('Kunde inte boka sessionen.');
          }
=======
  function bookSession(sessionId) {
      const userId = localStorage.getItem('userId');
      if (!userId) {
          alert("Du måste vara inloggad för att boka en session.");
          return;
      }

      fetch(`/bookings/book`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId: parseInt(userId), sessionId: parseInt(sessionId) })
      })
      .then(response => {
          if (!response.ok) {
              // Hanterar serverfel (t.ex. 400 eller 500 felstatus)
              throw new Error(`Server returned status: ${response.status}`);
          }
          return response.json();
      })
      .then(data => {
          if (typeof data === 'object' && 'status' in data && 'message' in data) {
              // Kontrollerar att datat innehåller förväntade fält
              if (data.status === "success") {
                  alert(data.message);
              } else {
                  throw new Error(data.message || "Ett fel inträffade.");
              }
          } else {
              // Hanterar oväntat svarsformat
              throw new Error("Unexpected response format");
          }
      })
      .catch(error => {
          // Hanterar både nätverksfel och fel från tidigare kastade undantag
          console.error('Error booking session:', error);
          alert(error.message);
>>>>>>> Master
      });
  }


<<<<<<< HEAD
=======



>>>>>>> Master
  // Avboka evenemang
  function cancelBooking(sessionId) {
    fetch(`/sessions/cancel/${sessionId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // Inkludera headers för autentisering om så krävs
      }
    })
    .then(response => {
      if (response.ok) {
        alert('Session avbokad.');
        // Uppdatera kalendern här
      } else {
        alert('Kunde inte avboka sessionen.');
      }
    });
  }

  // Skapa kalendern
 document.addEventListener('DOMContentLoaded', function() {
     var calendarEl = document.getElementById('calendar');
     var calendar = new FullCalendar.Calendar(calendarEl, {
        columnWidth: 100,
         initialView: 'timeGridWeek',
         firstDay: 1, // Första dagen i veckan, 0: Söndag, 1: Måndag, etc.
         slotMinTime: '07:00:00', // Starttid för dagens slots
         slotMaxTime: '22:00:00', // Sluttid för dagens slots
         slotDuration: "00:10:00",
         headerToolbar: {
             left: 'prev,next today',
             center: 'title',
             right: 'dayGridMonth,timeGridWeek,timeGridDay'
         },
         events: function(fetchInfo, successCallback, failureCallback) {
             fetchEvents(fetchInfo, successCallback, failureCallback);
         },
    eventClick: function(info) {
      currentEvent = info.event; // Sätt nuvarande event till det klickade eventet
      document.getElementById('modalTitle').textContent = currentEvent.title;
      document.getElementById('modalDescription').textContent = `Instruktör: ${currentEvent.extendedProps.instructor}`;

      let bookBtn = document.getElementById('bookBtn');
      let cancelBtn = document.getElementById('cancelBtn');

      // Sätta sessionID som ett attribut på bokningsknappen
      bookBtn.setAttribute('data-sessionid', currentEvent.id);
      cancelBtn.setAttribute('data-sessionid', currentEvent.id);

      // Använda arrow-funktion för att behålla rätt this-referens
      bookBtn.onclick = () => bookSession(bookBtn.getAttribute('data-sessionid'));

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
      bookedElement.textContent = `Lediga platser: ${arg.event.extendedProps.capacity}`;
      eventElement.appendChild(bookedElement);

      return { domNodes: [eventElement] };
    }

  });

  calendar.render();

  // Eventlyssnare för bokningsknappar
  document.getElementById('bookBtn').addEventListener('click', bookSession);
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



// Exempel på hur du använder dessa funktioner
document.addEventListener('DOMContentLoaded', function() {
    // Kontrollera om användaren är inloggad
    if (localStorage.getItem('userToken')) {
        // Användaren är inloggad
        document.getElementById('loginRegisterLinks').style.display = 'none';
        document.getElementById('logoutContainer').style.display = 'block';
    } else {
        // Användaren är inte inloggad
        document.getElementById('loginRegisterLinks').style.display = 'block';
        document.getElementById('logoutContainer').style.display = 'none';
    }
});

// Funktion för att hämta och visa den inloggade användarens information
function loadUserProfile() {
    const userId = localStorage.getItem('userId');
    if(userId) {
        fetch(`/user/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('fullName').value = user.name; // Använder egenskapen 'name'
            document.getElementById('email').value = user.email; // Använder egenskapen 'email'
            document.getElementById('phonenumber').value = user.phoneNumber; // Använder egenskapen 'phoneNumber'
            document.getElementById('password').value = user.password; // Använder egenskapen 'password'

        })
        .catch(error => console.error('Error loading user profile:', error));
    } else {
        console.error('No user ID found in localStorage');
    }
}

document.getElementById('userForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const userId = localStorage.getItem('userId');
    const updatedUser = {
        id: userId,
        name: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        phoneNumber: document.getElementById('phonenumber').value,
        password: document.getElementById('password').value,
    };

    fetch(`/user/updateUser/${userId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
    .then(response => {
        if (response.ok) {
            alert('Profile updated successfully.');
            loadUserProfile(); // Laddar om användarprofilen efter en lyckad uppdatering
        } else {
            alert('Error updating profile.');
        }
    })
    .catch(error => console.error('Error updating user profile:', error));
});


document.addEventListener('DOMContentLoaded', function() {
    loadUserProfile(); // Laddar användarprofil när sidan har laddats
});
