import { openModal } from './modal.js';
import { BASE_URL } from './config.js';

const ADMIN_API = `${BASE_URL}/admin/login`;
const DOCTOR_API = `${BASE_URL}/doctor/login`;

window.onload = function () {
  const adminBtn = document.getElementById('adminLogin');
  const doctorBtn = document.getElementById('doctorLogin');

  if (adminBtn) {
    adminBtn.addEventListener('click', () => openModal('adminLogin'));
  }

  if (doctorBtn) {
    doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
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
      const data = await response.json();
      const token = data.token;

      localStorage.setItem('token', token);

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

    const doctor = { email, password };

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