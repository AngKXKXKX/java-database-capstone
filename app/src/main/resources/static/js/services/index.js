import { openModal } from '/js/components/modals.js';
import { API_BASE_URL } from '/js/config/config.js';

const ADMIN_API = `${API_BASE_URL}admin/login`;
const DOCTOR_API = `${API_BASE_URL}doctor/login`;
const PATIENT_API = `${API_BASE_URL}patient/login`;

window.onload = function () {
  const adminBtn = document.getElementById('adminLogin');
  const doctorBtn = document.getElementById('doctorLogin');
  const patientBtn = document.getElementById('patientLogin');

  if (adminBtn) {
    adminBtn.addEventListener('click', () => openModal('adminLogin'));
  }

  if (doctorBtn) {
    doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
  }
  
  if (patientBtn) {
    patientBtn.addEventListener('click', () => openModal('patientLogin'));
  }
};

window.adminLoginHandler = async function () {
  try {
    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    const admin = { username, password };

    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(admin)
    });

    if (response.ok) {
        console.log("ok")
      const data = await response.json();
      const token = data.token;

      localStorage.setItem('token', token);
      console.log("set token")

      selectRole('admin');

    } else {
      alert('Invalid admin credentials');
    }

  } catch (error) {
    console.error(error);
    alert('Something went wrong. Please try again.');
  }
};

window.doctorLoginHandler = async function () {
  try {
    const email = document.getElementById('doctorEmail').value;
    const password = document.getElementById('doctorPassword').value;
    const role = "doctor"

    const doctor = { identifier:email, password,role};

    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(doctor)
    });

    if (response.ok) {
      const data = await response.json();
      const token = data.token;

      localStorage.setItem('token', token);

      selectRole('doctor');
    } else {
      alert('Invalid doctor credentials');
    }

  } catch (error) {
    console.error(error);
    alert('Something went wrong. Please try again.');
  }
};

window.patientLoginHandler = async function () {
    try {
      const email = document.getElementById('patientEmail').value;
      const password = document.getElementById('patientPassword').value;
      const role = "patient"
  
      const patient = { identifier:email, password,role};
  
      const response = await fetch(PATIENT_API, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(patient)
      });
  
      if (response.ok) {
        const data = await response.json();
        const token = data.token;
  
        localStorage.setItem('token', token);
  
        selectRole('patient');
      } else {
        alert('Invalid patient credentials');
      }
  
    } catch (error) {
      console.error(error);
      alert('Something went wrong. Please try again.');
    }
  };

    function selectRole(role) {
    setRole(role);
    const token = localStorage.getItem('token');
    if (role === "admin") {
        if (token) {
        window.location.href = `/adminDashboard/${token}`;
        }
    } if (role === "patient") {
        window.location.href = "/pages/patientDashboard.html";
    } else if (role === "doctor") {
        if (token) {
        window.location.href = `/doctorDashboard/${token}`;
        } else if (role === "loggedPatient") {
        window.location.href = "loggedPatientDashboard.html";
        }
    }
}
