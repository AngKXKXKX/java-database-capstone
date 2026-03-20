import { openModal } from './modal.js';
import { getDoctors, saveDoctor, filterDoctors } from './doctorService.js';
import { createDoctorCard } from './doctorCard.js';

const contentDiv = document.getElementById('content');
const addDoctorBtn = document.getElementById('addDoctorBtn');
const searchInput = document.getElementById('searchBar');
const timeFilter = document.getElementById('filterTime');
const specialtyFilter = document.getElementById('filterSpecialty');

if (addDoctorBtn) {
  addDoctorBtn.addEventListener('click', () => {
    openModal('addDoctor');
  });
}

window.addEventListener('DOMContentLoaded', () => {
  loadDoctorCards();
});

async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();

    contentDiv.innerHTML = '';

    doctors.forEach((doctor) => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });

  } catch (error) {
    console.error('Error loading doctors:', error);
  }
}

if (searchInput) {
  searchInput.addEventListener('input', filterDoctorsOnChange);
}

if (timeFilter) {
  timeFilter.addEventListener('change', filterDoctorsOnChange);
}

if (specialtyFilter) {
  specialtyFilter.addEventListener('change', filterDoctorsOnChange);
}

async function filterDoctorsOnChange() {
  try {
    let name = searchInput.value.trim();
    let time = timeFilter.value;
    let specialty = specialtyFilter.value;

    name = name === '' ? null : name;
    time = time === '' ? null : time;
    specialty = specialty === '' ? null : specialty;

    const data = await filterDoctors(name, time, specialty);

    if (data.doctors && data.doctors.length > 0) {
      renderDoctorCards(data.doctors);
    } else {
      contentDiv.innerHTML = `<p>No doctors found with the given filters.</p>`;
    }

  } catch (error) {
    console.error(error);
    alert('Error filtering doctors');
  }
}

function renderDoctorCards(doctors) {
  contentDiv.innerHTML = '';

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

window.adminAddDoctor = async function () {
  try {
    const name = document.getElementById('doctorName').value;
    const email = document.getElementById('doctorEmail').value;
    const phone = document.getElementById('doctorPhone').value;
    const password = document.getElementById('doctorPassword').value;
    const specialty = document.getElementById('doctorSpecialty').value;
    const availableTimes = document.getElementById('doctorTimes').value;

    const token = localStorage.getItem('token');

    if (!token) {
      alert('You are not authenticated!');
      return;
    }

    const doctor = {
      name,
      email,
      phone,
      password,
      specialty,
      availableTimes
    };

    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert('Doctor added successfully!');

   
      location.reload();
    } else {
      alert(result.message || 'Failed to add doctor');
    }

  } catch (error) {
    console.error(error);
    alert('Error adding doctor');
  }
};