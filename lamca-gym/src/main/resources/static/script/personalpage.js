
  // Hämta evenemang från servern
  function fetchEvents(fetchInfo, successCallback, failureCallback) {
      fetch('/sessions/all')
          .then(response => response.json())
          .then(sessions => {
              const events = sessions.map(session => {
                  return {
                      id: session.sessionId,
                      title: session.sessionType,
                      start: session.time,
                      end: calculateEndTime(session.time, session.duration),
                      instructor: session.instructor,
                      capacity: session.capacity,
                      booked: session.booked,  // Antal bokade platser
                      isUserBooked: session.isUserBooked  // Boolean värde om användaren har bokat
                  };
              });
              successCallback(events);
          })
          .catch(error => {
              console.error('Error fetching sessions:', error);
              failureCallback(error);
          });
  }


  function bookSession(sessionId) {
      const userId = localStorage.getItem('userId');
      if (!userId) {
          alert("Du måste vara inloggad för att boka en session.");
          return;
      }

      const currentSession = calendar.getEventById(sessionId);  // Antag att du kan få tag på event-objektet
      if (currentSession.extendedProps.isUserBooked) {
          alert("Du har redan bokat detta pass.");
          return;
      }

      fetch(`/bookings/book`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId: parseInt(userId), sessionId: parseInt(sessionId) })
      })
      .then(response => {
          if (!response.ok) {
              throw new Error(`Server returned status: ${response.status}`);
          }
          return response.json();
      })
      .then(data => {
          if (data.status === "success") {
              alert(data.message);
              currentSession.setProp('isUserBooked', true);  // Uppdatera kalendern direkt
              calendar.refetchEvents();  // Återladda evenemangen för att visa uppdateringar
          } else {
              throw new Error(data.message || "Ett fel inträffade.");
          }
      })
      .catch(error => {
          console.error('Error booking session:', error);
          alert(error.message);
      });
  }


  function calculateEndTime(startTime, duration) {
      // Antag att 'duration' är i minuter och 'startTime' är en sträng i ISO 8601-format
      let start = new Date(startTime);
      return new Date(start.getTime() + duration * 60000).toISOString(); // Lägg till 'duration' i minuter till starttiden
  }



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
        // Kontrollera om användaren har bokat detta pass
        var isUserBooked = arg.event.extendedProps.isUserBooked;

        // Lägg till klasser baserat på bokningsstatus
        eventElement.classList.add(isFull ? 'full' : 'available');
        if (isUserBooked) {
            eventElement.classList.add('user-booked');  // Denna klass kan ha en speciell stil i CSS
        }

        // Titel
        var titleElement = document.createElement('div');
        titleElement.classList.add('fc-event-title');
        titleElement.innerHTML = `<strong>${arg.event.title}</strong>`;
        eventElement.appendChild(titleElement);

        // ID
        var idElement = document.createElement('div');
        idElement.classList.add('fc-event-id');
        idElement.textContent = `ID: ${arg.event.id}`;
        eventElement.appendChild(idElement);

        // Tid
        var timeElement = document.createElement('div');
        timeElement.classList.add('fc-event-time');
        var startTime = FullCalendar.formatDate(arg.event.start, { hour: '2-digit', minute: '2-digit', hour12: false });
        var endTime = FullCalendar.formatDate(arg.event.end, { hour: '2-digit', minute: '2-digit', hour12: false });
        timeElement.textContent = `${startTime} - ${endTime}`;
        eventElement.appendChild(timeElement);

        // Instruktör
        var instructorElement = document.createElement('div');
        instructorElement.classList.add('fc-event-instructor');
        instructorElement.textContent = `Instruktör: ${arg.event.extendedProps.instructor}`;
        eventElement.appendChild(instructorElement);

        // Bokade / Kapacitet
        var bookedElement = document.createElement('div');
        bookedElement.classList.add('fc-event-booked');
        bookedElement.textContent = `Lediga platser: ${arg.event.extendedProps.capacity - arg.event.extendedProps.booked} / ${arg.event.extendedProps.capacity}`;
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
