// doctorCard.js

// Imports for booking and API functions (implement these separately)
import { showBookingOverlay } from "/js/loggedPatient.js";
import { deleteDoctor } from "/js/services/doctorServices.js";
import { getPatientData } from "/js/services/patientServices.js";

/**
 * Creates a doctor card element.
 * @param {Object} doctor - The doctor object containing:
 *                          {id, name, specialization, email, availableTimes}
 * @returns {HTMLElement} - The doctor card DOM element.
 */
export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Get user role
  const role = localStorage.getItem("userRole");

  // Doctor Info Section
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;
  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialty}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Available Times: ${doctor.availableTimes.join(", ")}`;

  infoDiv.append(name, specialization, email, availability);

  // Action Buttons
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Delete";
    removeBtn.classList.add("adminBtn");
    removeBtn.addEventListener("click", async () => {
      if (!confirm(`Delete Dr. ${doctor.name}?`)) return;

      const token = localStorage.getItem("token");
      if (!token) return alert("Admin token missing.");

      try {
        await deleteDoctor(doctor.id, token);
        alert("Doctor deleted successfully");
        card.remove();
      } catch (err) {
        alert("Error deleting doctor: " + err.message);
      }
    });
    actionsDiv.appendChild(removeBtn);
  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", () => alert("Patient needs to login first."));
    actionsDiv.appendChild(bookNow);
  } else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      const patientData = await getPatientData(token);
      showBookingOverlay(doctor, patientData);
    });
    actionsDiv.appendChild(bookNow);
  }

  // Assemble card
  card.append(infoDiv, actionsDiv);

  return card;
}