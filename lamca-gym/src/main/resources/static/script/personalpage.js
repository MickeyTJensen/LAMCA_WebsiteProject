 var calendar; // Global referens till kalenderinstansen
 var userId = localStorage.getItem('userId');
 document.addEventListener('DOMContentLoaded', function() {
     var calendarEl = document.getElementById('calendar');
     calendar = new FullCalendar.Calendar(calendarEl, {
         initialView: 'timeGridWeek',
         firstDay: 1,
         slotMinTime: '07:00:00',
         slotMaxTime: '22:00:00',
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
             var currentEvent = info.event;
             document.getElementById('modalTitle').textContent = currentEvent.title;
             document.getElementById('modalDescription').textContent = `Instructor: ${currentEvent.extendedProps.instructor}`;
             var bookBtn = document.getElementById('bookBtn');
             var cancelBtn = document.getElementById('cancelBtn');
             bookBtn.setAttribute('data-sessionid', currentEvent.id);
             cancelBtn.setAttribute('data-sessionid', currentEvent.id);
             bookBtn.onclick = () => bookSession(currentEvent.id);
             cancelBtn.onclick = () => cancelBooking(currentEvent.id,userId);
             bookBtn.style.display = currentEvent.extendedProps.isBooked ? 'none' : 'inline-block';
             cancelBtn.style.display = currentEvent.extendedProps.isBooked ? 'none':'inline-block' ;
             document.getElementById('eventModal').style.display = 'block';
             document.querySelector('.close').addEventListener('click', function() {
             document.getElementById('eventModal').style.display = 'none';
             });
         },
         eventContent: function(arg) {

             // Skapa en övergripande container
             var eventElement = document.createElement('div');
             eventElement.classList.add('fc-event-custom');

             // Kontrollera om evenemanget är fullbokat
             var isFull = arg.event.extendedProps.booked >= arg.event.extendedProps.capacity;
             // Kontrollera om användaren har bokat detta pass
             var isUserBooked = arg.event.extendedProps.isUserBooked;

             // Lägg till klasser baserat på bokningsstatus
             eventElement.classList.add(isFull ? 'full' : 'available');
             if (isUserBooked) {
                 eventElement.classList.add('userBooked');  // Denna klass kan ha en speciell stil i CSS
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
             instructorElement.textContent = `Instructor: ${arg.event.extendedProps.instructor}`;
             eventElement.appendChild(instructorElement);

             // Bokade / Kapacitet
             var bookedElement = document.createElement('div');
             var availableSeats = arg.event.extendedProps.capacity - arg.event.extendedProps.booked;
             bookedElement.textContent = `Available: ${availableSeats >= 0 ? availableSeats : 0} / ${arg.event.extendedProps.capacity}`;
             eventElement.appendChild(bookedElement);

             return { domNodes: [eventElement] };
         }
     });

     calendar.render();
     loadUserProfile(); // Laddar användarprofil när sidan har laddats
 });

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
                     booked: session.booked,
                     isUserBooked: session.isUserBooked
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
        alert("You must be logged in to book a session.");
        return;
    }

    fetch(`/bookings/book`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: parseInt(userId), sessionId: parseInt(sessionId) })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            alert(data.message);
            // Uppdatera antalet bokade platser lokalt
            const event = calendar.getEventById(sessionId);
            event.setExtendedProp('booked', event.extendedProps.booked + 1);
            event.setExtendedProp('isUserBooked', true);
            calendar.addEvent(event);
            calendar.render();
        } else {
            throw new Error(data.message || 'An error occurred.');
        }
    })
    .catch(error => {
        console.error('Error booking session:', error);
        alert(error.message);
    });
}

 function cancelBooking(sessionId, userId) {
    // Kontrollera om användar-ID är definierat och inte är undefined eller null
    if (userId !== undefined && userId !== null) {
        fetch(`/bookings/book/${sessionId}/${userId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Session cancelled.');
                calendar.refetchEvents();
            } else {
                throw new Error('Failed to cancel booking.');
            }
        })
        .catch(error => {
            console.error('Error cancelling booking:', error);
            alert('Network or server error occurred.');
        });
    } else {
        console.error('User ID is undefined or null.');
        alert('User ID is undefined or null.');
    }
}

 function calculateEndTime(startTime, duration) {
     let start = new Date(startTime);
     return new Date(start.getTime() + duration * 60000).toISOString();
 }

 function loadUserProfile() {
     const userId = localStorage.getItem('userId');
     if (userId) {
         fetch(`/user/${userId}`)
             .then(response => response.json())
             .then(user => {
                 document.getElementById('fullName').value = user.name;
                 document.getElementById('email').value = user.email;
                 document.getElementById('phonenumber').value = user.phoneNumber;
                 document.getElementById('password').value = user.password;
             })
             .catch(error => {
                 console.error('Error loading user profile:', error);
             });
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
             loadUserProfile(); // Reload user profile after successful update
         } else {
             alert('Error updating profile.');
         }
     })
     .catch(error => {
         console.error('Error updating user profile:', error);
     });
 });
 