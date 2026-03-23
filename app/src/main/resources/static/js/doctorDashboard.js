import { getAllAppointments } from '/js/services/appointmentRecordService.js';
import { createPatientRow } from '/js/components/patientRows.js';

const tableBody = document.getElementById('tableBody');
const searchInput = document.getElementById('searchInput');
const todayBtn = document.getElementById('todayBtn');
const datePicker = document.getElementById('datePicker');

let selectedDate = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
let token = localStorage.getItem('token');
let patientName = null;

if (searchInput) {
  searchInput.addEventListener('input', () => {
    const value = searchInput.value.trim();

    if (value !== '') {
      patientName = value;
    } else {
      patientName = null;
    }

    loadAppointments();
  });
}

if (todayBtn) {
  todayBtn.addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];

    if (datePicker) {
      datePicker.value = selectedDate;
    }

    loadAppointments();
  });
}

if (datePicker) {
  datePicker.addEventListener('change', () => {
    selectedDate = datePicker.value;
    loadAppointments();
  });
}

async function loadAppointments() {
    try {
      // Fetch appointments directly using token + optional filters
      const data = await getAllAppointments(token, patientName, selectedDate, null); 
      // data should now have the format: { count, appointments }
      const appointments = data.appointments;
  
      tableBody.innerHTML = '';
  
      if (!appointments || appointments.length === 0) {
        tableBody.innerHTML = `
          <tr>
            <td colspan="5">No Appointments found for today.</td>
          </tr>
        `;
        return;
      }
  
      appointments.forEach((appointment) => {
        const patient = {
          id: appointment.patient?.id || '',          // fallback if patient object exists
          name: appointment.patientName || 'N/A',
          phone: appointment.patientPhone || 'N/A',
          email: appointment.patientEmail || 'N/A'
        };
  
        const row = createPatientRow(patient, appointment);
        tableBody.appendChild(row);
      });
  
    } catch (error) {
      console.error('Error loading appointments:', error);
  
      tableBody.innerHTML = `
        <tr>
          <td colspan="5">Error loading appointments. Try again later.</td>
        </tr>
      `;
    }
  }
  
window.addEventListener('DOMContentLoaded', () => {
  if (typeof renderContent === 'function') {
    renderContent();
  }

  loadAppointments();
});