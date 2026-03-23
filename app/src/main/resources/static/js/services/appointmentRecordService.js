// appointmentRecordService.js
import { API_BASE_URL } from "/js/config/config.js";
const APPOINTMENT_API = `${API_BASE_URL}/appointments`;

export async function getAllAppointments(token, patientName, start, end) {
  // Build optional query parameters
  const params = new URLSearchParams();
  if (patientName) params.append("patientName", patientName);
  if (start) params.append("start", start); // ISO string: "2026-03-23T10:00"
  if (end) params.append("end", end);

  // URL now does NOT include doctorId
  const url = `${APPOINTMENT_API}/appointments/${token}?${params.toString()}`;

  const response = await fetch(url);
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(`Failed to fetch appointments: ${errText}`);
  }

  return await response.json();
}

export async function bookAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(appointment)
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong"
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later."
    };
  }
}

export async function updateAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(appointment)
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong"
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later."
    };
  }
}
