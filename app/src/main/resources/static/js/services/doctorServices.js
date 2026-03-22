import { API_BASE_URL } from '/js/config/config.js';

const DOCTOR_API = `${API_BASE_URL}doctor`;

export async function getDoctors() {
  try {
    const response = await fetch(`${DOCTOR_API}/all`, {
      method: 'GET'
    });

    const data = await response.json();

    return data.doctors || [];

  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
}

export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/delete/${id}/${token}`, {
      method: 'DELETE'
    });

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || 'Delete operation completed'
    };

  } catch (error) {
    console.error('Error deleting doctor:', error);

    return {
      success: false,
      message: 'Failed to delete doctor'
    };
  }
}

export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/save/${token}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || 'Doctor saved successfully'
    };

  } catch (error) {
    console.error('Error saving doctor:', error);

    return {
      success: false,
      message: 'Failed to save doctor'
    };
  }
}

export async function filterDoctors(name, time, specialty) {
  try {
    const url = new URL(`${DOCTOR_API}/filter`);

    if (name) url.searchParams.append('name', name);
    if (time) url.searchParams.append('time', time);
    if (specialty) url.searchParams.append('speciality', specialty);

    const response = await fetch(url, {
      method: 'GET'
    });

    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error('Error filtering doctors:', response.status);
      return { doctors: [] };
    }

  } catch (error) {
    console.error('Filter error:', error);
    alert('Error fetching filtered doctors');

    return { doctors: [] };
  }
}